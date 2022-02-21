package dev.felnull.imp.server.music.ringer;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.music.MusicPlaybackInfo;
import dev.felnull.imp.networking.IMPPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class MusicRing {
    private final Map<UUID, IMusicRinger> ringers = new HashMap<>();
    private final Map<UUID, RingedPlayerInfos> playerInfos = new HashMap<>();
    private final Set<UUID> waitRingers = new HashSet<>();
    private long baseTime;
    private long pauseTime;
    private long lastTime;

    public MusicRing() {
        this.baseTime = System.currentTimeMillis();
    }

    protected void tick(ServerLevel level) {

        for (IMusicRinger value : ringers.values()) {
            if (!value.isRingerExist(level)) {
                stop(level, value.getRingerUUID());
                break;
            }
            var ms = value.getRingerMusicSource(level);
            boolean stopFlg = false;
            if (ms == null) {
                if (value.isRingerPlaying(level))
                    value.setRingerPlaying(level, false);
                stopFlg = true;
            } else {
                if (value.isRingerPlaying(level)) {
                    var pr = playerInfos.get(value.getRingerUUID());
                    if (pr == null) {
                        pr = new RingedPlayerInfos(value.getRingerUUID(), level, getTime());
                        pr.sendFirstPackets(level);
                        playerInfos.put(value.getRingerUUID(), pr);
                        waitRingers.add(value.getRingerUUID());
                    }
                } else {
                    stopFlg = true;
                }
            }

            var pr = playerInfos.get(value.getRingerUUID());
            if (pr != null) {
                if (stopFlg) {
                    pr.depose(level);
                    playerInfos.remove(value.getRingerUUID());
                    waitRingers.remove(value.getRingerUUID());
                } else {
                    if (pr.tick(level, getTime())) {
                        waitRingers.remove(value.getRingerUUID());
                        long eq = getTime() - lastTime;
                        if (ms.getDuration() >= value.getRingerPosition(level) + eq || value.isRingerStream()) {
                            value.setRingerPosition(level, value.getRingerPosition(level) + eq);
                        } else {
                            value.setRingerPosition(level, 0);
                            if (value.isRingerLoop(level)) {
                                pr.depose(level);
                                playerInfos.remove(value.getRingerUUID());
                            } else {
                                value.setRingerPlaying(level, false);
                            }
                        }
                    }
                }
            }

        }
        lastTime = getTime();
    }

    protected boolean isWaitRinger(UUID uuid) {
        return waitRingers.contains(uuid);
    }

    protected void addRinger(IMusicRinger ringer) {
        if (!ringers.containsKey(ringer.getRingerUUID()))
            ringers.put(ringer.getRingerUUID(), ringer);
    }

    public Map<UUID, IMusicRinger> getRingers() {
        return ringers;
    }

    protected void pause() {
        this.pauseTime = System.currentTimeMillis();
    }

    protected void unPause() {
        this.baseTime += System.currentTimeMillis() - this.pauseTime;
        this.pauseTime = 0;
    }

    private long getTime() {
        return System.currentTimeMillis() - this.baseTime;
    }

    protected void depose() {
        ringers.clear();
        playerInfos.clear();
        waitRingers.clear();
    }

    protected void restart(ServerLevel level, UUID uuid) {
        var ringer = ringers.get(uuid);
        if (ringer != null) {
            stop(level, uuid);
            addRinger(ringer);
        }
    }

    protected void stop(ServerLevel level, UUID uuid) {
        ringers.remove(uuid);
        var pr = playerInfos.get(uuid);
        if (pr != null)
            pr.depose(level);
        playerInfos.remove(uuid);
        waitRingers.remove(uuid);
    }

    private MusicPlaybackInfo getPlaybackInfo(IMusicRinger ringer, ServerLevel level) {
        var tr = ringer.getRingerTracker(level);
        return new MusicPlaybackInfo(tr.getKey(), tr.getValue(), ringer.getRingerVolume(level), ringer.getRingerRange(level));
    }

    protected void onUpdate(ServerPlayer player, UUID uuid, UUID waitUUID, int state) {
        var pr = playerInfos.get(uuid);
        if (pr != null && pr.waitUUID.equals(waitUUID))
            pr.responseUpdate(player, state);
    }

    protected void addReadyPlayer(ServerPlayer player, UUID uuid, UUID waitUUID, boolean result, boolean retry, long elapsed) {
        var pr = playerInfos.get(uuid);
        if (pr != null && pr.waitUUID.equals(waitUUID))
            pr.addReadyPlayer(player, result, retry, elapsed);
    }

    private class RingedPlayerInfos {
        private final UUID uuid;
        private final UUID waitUUID = UUID.randomUUID();
        private final List<UUID> firstWaitPlayers = new ArrayList<>();
        private final List<UUID> firstReadyPlayers = new ArrayList<>();
        private final List<UUID> listenPlayers = new ArrayList<>();
        private final List<UUID> middleLoadPlayers = new ArrayList<>();
        private final Map<UUID, Long> failurePlayers = new HashMap<>();
        private final long startTime;
        private boolean notWait;

        public RingedPlayerInfos(UUID uuid, ServerLevel level, long startTime) {
            this.uuid = uuid;
            this.startTime = startTime;
            firstWaitPlayers.addAll(level.players().stream().filter(n -> canListen(n, level)).map(n -> n.getGameProfile().getId()).toList());
        }

        private void sendFirstPackets(ServerLevel level) {
            for (UUID firstWaitPlayer : firstWaitPlayers) {
                if (level.getPlayerByUUID(firstWaitPlayer) instanceof ServerPlayer serverPlayer)
                    NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_READY, new IMPPackets.MusicReadyMessage(waitUUID, uuid, getRinger().getRingerMusicSource(level), getPlaybackInfo(getRinger(), level), getRingerPosition(level)).toFBB());
            }
        }

        private void sendStopPackets(UUID player, ServerLevel level) {
            if (level.getPlayerByUUID(player) instanceof ServerPlayer serverPlayer)
                NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_STATE, new IMPPackets.MusicRingStateMessage(uuid, waitUUID, 1).toFBB());
        }

        private void sendMiddleStartPacket(UUID player, ServerLevel level) {
            if (level.getPlayerByUUID(player) instanceof ServerPlayer serverPlayer)
                NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_READY, new IMPPackets.MusicReadyMessage(waitUUID, uuid, getRinger().getRingerMusicSource(level), getPlaybackInfo(getRinger(), level), getRingerPosition(level)).toFBB());
        }

        private long getRingerPosition(ServerLevel level) {
            if (getRinger().isRingerStream()) return 0;
            return getRinger().getRingerPosition(level);
        }

        private void addReadyPlayer(ServerPlayer player, boolean result, boolean retry, long elapsed) {
            var id = player.getGameProfile().getId();
            if (notWait) {
                if (middleLoadPlayers.contains(id)) {
                    if (result) {
                        NetworkManager.sendToPlayer(player, IMPPackets.MUSIC_RING_STATE, new IMPPackets.MusicRingStateMessage(uuid, waitUUID, 0, elapsed, MusicPlaybackInfo.EMPTY).toFBB());
                        listenPlayers.add(id);
                    } else {
                        failurePlayers.put(id, System.currentTimeMillis());
                    }
                    middleLoadPlayers.remove(id);
                }
            } else {
                if (result) {
                    listenPlayers.add(id);
                    firstReadyPlayers.add(id);
                } else {
                    failurePlayers.put(id, System.currentTimeMillis());
                }
                firstWaitPlayers.remove(id);
            }
        }

        private void startReadyWaitPlayers(ServerLevel level) {
            for (UUID pl : firstReadyPlayers) {
                if (level.getPlayerByUUID(pl) instanceof ServerPlayer serverPlayer)
                    NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_STATE, new IMPPackets.MusicRingStateMessage(uuid, waitUUID, 0).toFBB());
            }
        }

        private void responseUpdate(ServerPlayer player, int state) {
            var id = player.getGameProfile().getId();
            if (listenPlayers.contains(id)) {
                if (state != 1)
                    listenPlayers.remove(id);
            }

            if (middleLoadPlayers.contains(id)) {
                if (state != 2)
                    middleLoadPlayers.remove(id);
            }
        }

        private void sendUpdate(ServerLevel level) {
            for (UUID player : listenPlayers) {
                if (level.getPlayerByUUID(player) instanceof ServerPlayer serverPlayer)
                    NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_STATE, new IMPPackets.MusicRingStateMessage(uuid, waitUUID, 2, 0, getPlaybackInfo(getRinger(), level)).toFBB());
            }
            for (UUID player : middleLoadPlayers) {
                if (level.getPlayerByUUID(player) instanceof ServerPlayer serverPlayer)
                    NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_STATE, new IMPPackets.MusicRingStateMessage(uuid, waitUUID, 2, 0, getPlaybackInfo(getRinger(), level)).toFBB());
            }
        }

        private boolean tick(ServerLevel level, long currentTime) {
            if (notWait) {
                List<UUID> nl = new ArrayList<>();
                for (ServerPlayer player : level.players()) {
                    if (canListen(player, level)) {
                        var id = player.getGameProfile().getId();
                        if (!isFailureCoolDown(id)) {
                            nl.add(id);
                            if (!listenPlayers.contains(id) && !middleLoadPlayers.contains(id)) {
                                sendMiddleStartPacket(id, level);
                                middleLoadPlayers.add(id);
                            }
                        }
                    }
                }

                for (UUID listenPlayer : listenPlayers) {
                    if (!nl.contains(listenPlayer))
                        sendStopPackets(listenPlayer, level);
                }

                for (UUID middleLoadPlayer : middleLoadPlayers) {
                    if (!nl.contains(middleLoadPlayer))
                        sendStopPackets(middleLoadPlayer, level);
                }

                listenPlayers.clear();
                listenPlayers.addAll(nl.stream().filter(n -> !middleLoadPlayers.contains(n)).toList());

                middleLoadPlayers.clear();
                middleLoadPlayers.addAll(nl.stream().filter(n -> !listenPlayers.contains(n)).toList());

                sendUpdate(level);
                return true;
            }
            if (canPlayPlayersCheck(level, currentTime)) {
                startReadyWaitPlayers(level);
                notWait = true;
                return true;
            }
            return false;
        }

        private boolean isFailureCoolDown(UUID uuid) {
            Long fr = failurePlayers.get(uuid);
            if (fr != null)
                return (System.currentTimeMillis() - fr) <= getRetryTime();
            return false;
        }

        private boolean canPlayPlayersCheck(ServerLevel level, long currentTime) {
            long eq = currentTime - startTime;
            if (eq > getMaxWaitTime())
                return true;

            List<UUID> removes = new ArrayList<>();
            for (UUID waiter : firstWaitPlayers) {
                var pl = level.getPlayerByUUID(waiter);
                if (pl == null || !canListen(pl, level))
                    removes.add(waiter);
            }
            firstWaitPlayers.removeAll(removes);
            return firstWaitPlayers.isEmpty();
        }

        private boolean canListen(Player player, ServerLevel level) {
            return player.level == level && Math.sqrt(player.distanceToSqr(getRinger().getRingerSpatialPosition(level))) <= getRinger().getRingerRange(level) + 30;
        }

        private IMusicRinger getRinger() {
            return ringers.get(uuid);
        }

        private void depose(ServerLevel level) {
            for (UUID listenPlayer : listenPlayers) {
                sendStopPackets(listenPlayer, level);
            }
            for (UUID middleLoadPlayer : middleLoadPlayers) {
                sendStopPackets(middleLoadPlayer, level);
            }
        }
    }


    public static long getMaxWaitTime() {
        return IamMusicPlayer.CONFIG.maxWaitTime;
    }

    public static long getRetryTime() {
        return IamMusicPlayer.CONFIG.retryTime;
    }
}
