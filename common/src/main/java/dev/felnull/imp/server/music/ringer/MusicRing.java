package dev.felnull.imp.server.music.ringer;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.advancements.IMPCriteriaTriggers;
import dev.felnull.imp.music.tracker.MusicTrackerEntry;
import dev.felnull.imp.networking.IMPPackets;
import dev.felnull.otyacraftengine.advancement.ModInvolvementTrigger;
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
    private long pauseTime = -1;
    private long lastTime;

    public MusicRing(ServerLevel level) {
        this.level = level;
        this.baseTime = System.currentTimeMillis();
        this.lastTime = getTime();
    }

    private ServerLevel getLevel() {
        return level;
    }

    protected void tick() {
        var stopRingers = new ArrayList<UUID>();
        for (IMusicRinger ringer : ringers.values()) {
            var uuid = ringer.getRingerUUID();
            if (!ringer.exists()) {
                stopRingers.add(uuid);
            } else {
                var ms = ringer.getRingerMusicSource();
                if (ms == null || !ringer.isRingerPlaying()) {
                    ringer.setRingerPlaying(false);
                    stopRingingPlayer(uuid);
                } else {
                    var rpi = playerInfos.get(uuid);
                    if (rpi == null) {
                        rpi = new RingedPlayerInfos(uuid, getTime());
                        rpi.sendFirstPackets();
                        playerInfos.put(uuid, rpi);
                        waitRingers.add(uuid);
                    }

                    if (rpi.tick(getTime())) {
                        waitRingers.remove(uuid);
                        long eq = getTime() - lastTime;
                        if (ms.getDuration() >= ringer.getRingerPosition() + eq || ringer.isRingerStream()) {
                            var sc = ringer.getRingerMusicSource();
                            ringer.setRingerPosition(clamp(ringer.getRingerPosition() + eq, 0, sc != null ? sc.getDuration() : 0));
                        } else {
                            ringer.setRingerPosition(0);
                            ringer.ringerEnd();
                            if (ringer.isRingerLoop()) {
                                stopRingingPlayer(uuid);
                            } else {
                                ringer.setRingerPlaying(false);
                            }
                        }
                    }
                }
            }
        }
        stopRingers.forEach(this::stopRinger);
        lastTime = getTime();
    }

    public static long clamp(long value, long max, long min) {
        return value < max ? max : Math.min(value, min);
    }

    protected boolean isWaitRinger(UUID ringer) {
        return waitRingers.contains(ringer);
    }

    protected void addRinger(IMusicRinger ringer) {
        if (!ringers.containsKey(ringer.getRingerUUID())) ringers.put(ringer.getRingerUUID(), ringer);
        //System.out.println("add: " + ringer.getRingerUUID() + ", " + ringer);
    }

    public boolean hasRinger(UUID ringer) {
        return ringers.containsKey(ringer);
    }

    public Map<UUID, IMusicRinger> getRingers() {
        return ringers;
    }

    protected void pause() {
        this.pauseTime = System.currentTimeMillis();
    }

    protected void resume() {
        if (this.pauseTime >= 0)
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
            stopRinger(uuid);
            addRinger(ringer);
        }
    }

    protected void stopRinger(UUID ringer) {
        //System.out.println("stop: " + ringer + ", " + ringers.get(ringer));
        ringers.remove(ringer);
        waitRingers.remove(ringer);
        if (playerInfos.containsKey(ringer)) {
            playerInfos.get(ringer).depose();
            playerInfos.remove(ringer);
        }
    }

    protected void stopRingingPlayer(UUID ringer) {
        waitRingers.remove(ringer);
        if (playerInfos.containsKey(ringer)) {
            playerInfos.get(ringer).depose();
            playerInfos.remove(ringer);
        }
    }

    private MusicTrackerEntry getMusicTracker(IMusicRinger ringer) {
        return ringer.getRingerTracker();
        //   return new MusicTrackerEntry(tr.location(), tr.tracker());
//        return new MusicPlaybackInfo(tr.getKey(), tr.getValue(), ringer.isRingerMute() ? 0 : ringer.getRingerVolume(), ringer.isRingerMute() ? 0 : ringer.getRingerRange());
    }

    protected void onUpdate(ServerPlayer player, UUID uuid, UUID waitUUID, IMPPackets.MusicRingResponseStateType state) {
        var pr = playerInfos.get(uuid);
        if (pr != null && pr.infoUUID.equals(waitUUID)) pr.responseUpdate(player, state);
    }

    protected void addReadyPlayer(ServerPlayer player, UUID uuid, UUID waitUUID, boolean result, boolean retry, long elapsed) {
        var pr = playerInfos.get(uuid);

        if (pr != null && pr.infoUUID.equals(waitUUID)) {
            long st = getTime() - pr.startTime;
            if (st > getMaxWaitTime())
                elapsed -= getMaxWaitTime();

            pr.addReadyPlayer(player, result, retry, elapsed);
        }
    }

    private class RingedPlayerInfos {
        private final UUID ringerUUID;
        private final UUID infoUUID = UUID.randomUUID();
        private final List<UUID> firstWaitPlayers = new ArrayList<>();
        private final List<UUID> firstReadyPlayers = new ArrayList<>();
        private final List<UUID> listenPlayers = new ArrayList<>();
        private final List<UUID> middleLoadPlayers = new ArrayList<>();
        private final Map<UUID, Long> failurePlayers = new HashMap<>();
        private final long startTime;
        private boolean notWait;

        public RingedPlayerInfos(UUID ringer, long startTime) {
            this.ringerUUID = ringer;
            this.startTime = startTime;
            firstWaitPlayers.addAll(getLevel().players().stream().filter(this::canListen).map(n -> n.getGameProfile().getId()).toList());
        }

        private void sendFirstPackets() {
            for (UUID firstWaitPlayer : firstWaitPlayers) {
                if (getLevel().getPlayerByUUID(firstWaitPlayer) instanceof ServerPlayer serverPlayer)
                    NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_READY, new IMPPackets.MusicReadyMessage(infoUUID, ringerUUID, getRinger().getRingerMusicSource(), getMusicTracker(getRinger()).saveToTag(), getRingerPosition()).toFBB());
            }
        }

        private void sendStopPackets(UUID player) {
            if (getLevel().getPlayerByUUID(player) instanceof ServerPlayer serverPlayer)
                NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_STATE, new IMPPackets.MusicRingStateMessage(ringerUUID, infoUUID, IMPPackets.MusicRingStateType.STOP).toFBB());
        }

        private void sendMiddleStartPacket(UUID player) {
            if (getLevel().getPlayerByUUID(player) instanceof ServerPlayer serverPlayer) {
                NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_READY, new IMPPackets.MusicReadyMessage(infoUUID, ringerUUID, getRinger().getRingerMusicSource(), getMusicTracker(getRinger()).saveToTag(), getRingerPosition()).toFBB());
                advancement(serverPlayer);
            }
        }

        private long getRingerPosition() {
            if (getRinger().isRingerStream())
                return 0;
            var sc = getRinger().getRingerMusicSource();
            return clamp(getRinger().getRingerPosition(), 0, sc != null ? sc.getDuration() : 0);
        }

        private void addReadyPlayer(ServerPlayer player, boolean result, boolean retry, long elapsed) {
            var id = player.getGameProfile().getId();
            if (notWait) {
                if (middleLoadPlayers.contains(id)) {
                    if (result) {
                        NetworkManager.sendToPlayer(player, IMPPackets.MUSIC_RING_STATE, new IMPPackets.MusicRingStateMessage(ringerUUID, infoUUID, IMPPackets.MusicRingStateType.PLAY, getRinger().isRingerStream() ? 0 : elapsed, getMusicTracker(getRinger()).saveToTag()).toFBB());
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
                if (getLevel().getPlayerByUUID(pl) instanceof ServerPlayer serverPlayer) {
                    NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_STATE, new IMPPackets.MusicRingStateMessage(ringerUUID, infoUUID, IMPPackets.MusicRingStateType.PLAY, 0, getMusicTracker(getRinger()).saveToTag()).toFBB());
                    advancement(serverPlayer);
                }
            }
        }

        private void responseUpdate(ServerPlayer player, IMPPackets.MusicRingResponseStateType state) {
            var id = player.getGameProfile().getId();
            if (listenPlayers.contains(id)) {
                if (state != IMPPackets.MusicRingResponseStateType.PLAYING)
                    listenPlayers.remove(id);

                //    if (state == IMPPackets.MusicRingResponseStateType.LOADING)
                //        sendStopPackets(id);
            }

            if (middleLoadPlayers.contains(id)) {
                if (state != IMPPackets.MusicRingResponseStateType.LOADING)
                    middleLoadPlayers.remove(id);
            }
        }

        private void sendUpdate() {
            for (UUID player : listenPlayers) {
                if (getLevel().getPlayerByUUID(player) instanceof ServerPlayer serverPlayer)
                    NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_STATE, new IMPPackets.MusicRingStateMessage(ringerUUID, infoUUID, IMPPackets.MusicRingStateType.UPDATE, 0, getMusicTracker(getRinger()).saveToTag()).toFBB());
            }
            for (UUID player : middleLoadPlayers) {
                if (getLevel().getPlayerByUUID(player) instanceof ServerPlayer serverPlayer)
                    NetworkManager.sendToPlayer(serverPlayer, IMPPackets.MUSIC_RING_STATE, new IMPPackets.MusicRingStateMessage(ringerUUID, infoUUID, IMPPackets.MusicRingStateType.UPDATE, 0, getMusicTracker(getRinger()).saveToTag()).toFBB());
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
                    if (!nl.contains(listenPlayer)) sendStopPackets(listenPlayer);
                }

                for (UUID middleLoadPlayer : middleLoadPlayers) {
                    if (!nl.contains(middleLoadPlayer)) sendStopPackets(middleLoadPlayer);
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

        private boolean isFailureCoolDown(UUID player) {
            Long fr = failurePlayers.get(player);
            if (fr != null) return (System.currentTimeMillis() - fr) <= getRetryTime();
            return false;
        }

        private boolean canPlayPlayersCheck(long currentTime) {
            long eq = currentTime - startTime;
            if (eq > getMaxWaitTime()) return true;

            List<UUID> removes = new ArrayList<>();
            for (UUID waiter : firstWaitPlayers) {
                var pl = getLevel().getPlayerByUUID(waiter);
                if (pl == null || !canListen(pl)) removes.add(waiter);
            }
            firstWaitPlayers.removeAll(removes);
            return firstWaitPlayers.isEmpty();
        }

        private void advancement(ServerPlayer player) {
            IMPCriteriaTriggers.LISTEN_TO_MUSIC.trigger(player, getRinger().isRingerStream(), getRinger().isRingerRemote(), isKamesuta());
            ModInvolvementTrigger.trigger(player, IamMusicPlayer.MODID);
        }

        private boolean isKamesuta() {
            var ath = getRinger().getRingerMusicAuthor();
            return getRinger().getRingerAntenna().getHoverName().getString().equalsIgnoreCase("kamesuta") && ath != null && (ath.equalsIgnoreCase("kamesuta") || ath.equalsIgnoreCase("かめすた") || ath.equalsIgnoreCase("カメスタ"));
        }

        private boolean canListen(Player player) {
            return player.level() == getLevel() && Math.sqrt(player.distanceToSqr(getRinger().getRingerSpatialPosition())) <= (getRinger().isRingerMute() ? 0 : (getRinger().getRingerRange() + 30));
        }

        private IMusicRinger getRinger() {
            return ringers.get(ringerUUID);
        }

        private void depose() {
            for (UUID listenPlayer : listenPlayers) {
                sendStopPackets(listenPlayer);
            }
            for (UUID middleLoadPlayer : middleLoadPlayers) {
                sendStopPackets(middleLoadPlayer);
            }
            for (UUID listenPlayer : firstReadyPlayers) {
                sendStopPackets(listenPlayer);
            }
            for (UUID listenPlayer : firstWaitPlayers) {
                sendStopPackets(listenPlayer);
            }
        }
    }


    public static long getMaxWaitTime() {
        return IamMusicPlayer.getConfig().maxWaitTime;
    }

    public static long getRetryTime() {
        return IamMusicPlayer.getConfig().retryTime;
    }
}
