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
    private final ServerLevel level;
    private final Map<UUID, IMusicRinger> ringers = new HashMap<>();
    private final Map<UUID, RingedPlayerInfos> playerInfos = new HashMap<>();
    private final Set<UUID> waitRingers = new HashSet<>();
    private long baseTime;
    private long pauseTime;
    private long lastTime;

    public MusicRing(ServerLevel level) {
        this.level = level;
        this.baseTime = System.currentTimeMillis();
    }

    private ServerLevel getLevel() {
        return level;
    }

    protected void tick(ServerLevel level) {
        if (level != getLevel()) return;

        for (IMusicRinger value : ringers.values()) {
            if (!value.isRingerExist()) {
                stop(value.getRingerUUID());
                break;
            }
            var ms = value.getRingerMusicSource();
            boolean stopFlg = false;
            if (ms == null) {
                if (value.isRingerPlaying())
                    value.setRingerPlaying(false);
                stopFlg = true;
            } else {
                if (value.isRingerPlaying()) {
                    var pr = playerInfos.get(value.getRingerUUID());
                    if (pr == null) {
                        pr = new RingedPlayerInfos(value.getRingerUUID(), getTime());
                        pr.sendFirstPackets();
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
                    pr.depose();
                    playerInfos.remove(value.getRingerUUID());
                    waitRingers.remove(value.getRingerUUID());
                } else {
                    if (pr.tick(getTime())) {
                        waitRingers.remove(value.getRingerUUID());
                        long eq = getTime() - lastTime;
                        if (ms.getDuration() >= value.getRingerPosition() + eq || value.isRingerStream()) {
                            value.setRingerPosition(value.getRingerPosition() + eq);
                        } else {
                            value.setRingerPosition(0);
                            value.ringerEnd();
                            if (value.isRingerLoop()) {
                                pr.depose();
                                playerInfos.remove(value.getRingerUUID());
                            } else {
                                value.setRingerPlaying(false);
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

    protected void restart(UUID uuid) {
        var ringer = ringers.get(uuid);
        if (ringer != null) {
            stop(uuid);
            addRinger(ringer);
        }
    }

    protected void stop(UUID uuid) {
        ringers.remove(uuid);
        var pr = playerInfos.get(uuid);
        if (pr != null)
            pr.depose();
        playerInfos.remove(uuid);
        waitRingers.remove(uuid);
    }

    private MusicPlaybackInfo getPlaybackInfo(IMusicRinger ringer) {
        var tr = ringer.getRingerTracker();
        return new MusicPlaybackInfo(tr.getKey(), tr.getValue(), ringer.isRingerMute() ? 0 : ringer.getRingerVolume(), ringer.isRingerMute() ? 0 : ringer.getRingerRange());
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

        public RingedPlayerInfos(UUID uuid, long startTime) {
            this.uuid = uuid;
            this.startTime = startTime;
            firstWaitPlayers.addAll(getLevel().players().stream().filter(n -> canListen(n)).map(n -> n.getGameProfile().getId()).toList());
        }

        private void sendFirstPackets() {
            for (UUID firstWaitPlayer : firstWaitPlayers) {
                if (getLevel().getPlayerByUUID(firstWaitPlayer) instanceof ServerPlayer serverPlayer)
                    NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_READY, new IMPPackets.MusicReadyMessage(waitUUID, uuid, getRinger().getRingerMusicSource(), getPlaybackInfo(getRinger()), getRingerPosition()).toFBB());
            }
        }

        private void sendStopPackets(UUID player) {
            if (getLevel().getPlayerByUUID(player) instanceof ServerPlayer serverPlayer)
                NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_STATE, new IMPPackets.MusicRingStateMessage(uuid, waitUUID, 1).toFBB());
        }

        private void sendMiddleStartPacket(UUID player) {
            if (getLevel().getPlayerByUUID(player) instanceof ServerPlayer serverPlayer)
                NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_READY, new IMPPackets.MusicReadyMessage(waitUUID, uuid, getRinger().getRingerMusicSource(), getPlaybackInfo(getRinger()), getRingerPosition()).toFBB());
        }

        private long getRingerPosition() {
            if (getRinger().isRingerStream()) return 0;
            return getRinger().getRingerPosition();
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

        private void startReadyWaitPlayers() {
            for (UUID pl : firstReadyPlayers) {
                if (getLevel().getPlayerByUUID(pl) instanceof ServerPlayer serverPlayer)
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

        private void sendUpdate() {
            for (UUID player : listenPlayers) {
                if (getLevel().getPlayerByUUID(player) instanceof ServerPlayer serverPlayer)
                    NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_STATE, new IMPPackets.MusicRingStateMessage(uuid, waitUUID, 2, 0, getPlaybackInfo(getRinger())).toFBB());
            }
            for (UUID player : middleLoadPlayers) {
                if (getLevel().getPlayerByUUID(player) instanceof ServerPlayer serverPlayer)
                    NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_STATE, new IMPPackets.MusicRingStateMessage(uuid, waitUUID, 2, 0, getPlaybackInfo(getRinger())).toFBB());
            }
        }

        private boolean tick(long currentTime) {
            if (notWait) {
                List<UUID> nl = new ArrayList<>();
                for (ServerPlayer player : getLevel().players()) {
                    if (canListen(player)) {
                        var id = player.getGameProfile().getId();
                        if (!isFailureCoolDown(id)) {
                            nl.add(id);
                            if (!listenPlayers.contains(id) && !middleLoadPlayers.contains(id)) {
                                sendMiddleStartPacket(id);
                                middleLoadPlayers.add(id);
                            }
                        }
                    }
                }

                for (UUID listenPlayer : listenPlayers) {
                    if (!nl.contains(listenPlayer))
                        sendStopPackets(listenPlayer);
                }

                for (UUID middleLoadPlayer : middleLoadPlayers) {
                    if (!nl.contains(middleLoadPlayer))
                        sendStopPackets(middleLoadPlayer);
                }

                listenPlayers.clear();
                listenPlayers.addAll(nl.stream().filter(n -> !middleLoadPlayers.contains(n)).toList());

                middleLoadPlayers.clear();
                middleLoadPlayers.addAll(nl.stream().filter(n -> !listenPlayers.contains(n)).toList());

                sendUpdate();
                return true;
            }
            if (canPlayPlayersCheck(currentTime)) {
                startReadyWaitPlayers();
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

        private boolean canPlayPlayersCheck(long currentTime) {
            long eq = currentTime - startTime;
            if (eq > getMaxWaitTime())
                return true;

            List<UUID> removes = new ArrayList<>();
            for (UUID waiter : firstWaitPlayers) {
                var pl = getLevel().getPlayerByUUID(waiter);
                if (pl == null || !canListen(pl))
                    removes.add(waiter);
            }
            firstWaitPlayers.removeAll(removes);
            return firstWaitPlayers.isEmpty();
        }

        private boolean canListen(Player player) {
            return player.level == getLevel() && Math.sqrt(player.distanceToSqr(getRinger().getRingerSpatialPosition())) <= (getRinger().isRingerMute() ? 0 : (getRinger().getRingerRange() + 30));
        }

        private IMusicRinger getRinger() {
            return ringers.get(uuid);
        }

        private void depose() {
            for (UUID listenPlayer : listenPlayers) {
                sendStopPackets(listenPlayer);
            }
            for (UUID middleLoadPlayer : middleLoadPlayers) {
                sendStopPackets(middleLoadPlayer);
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
