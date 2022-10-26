package dev.felnull.imp.client.handler;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.client.music.IMPMusicTrackerFactory;
import dev.felnull.imp.client.music.MusicRingerEngineConnector;
import dev.felnull.imp.client.music.MusicSyncManager;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.networking.IMPPackets;
import net.minecraft.client.Minecraft;

import java.util.Collections;

public class ClientMessageHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void onMusicRingStateResponseMessage(IMPPackets.MusicRingStateMessage message, NetworkManager.PacketContext packetContext) {
        packetContext.queue(() -> {
            if (mc.getConnection() == null) return;
            switch (message.stateType()) {
                case PLAY -> MusicRingerEngineConnector.play(message.uuid(), message.elapsed());
                case STOP -> MusicRingerEngineConnector.stop(message.uuid());
                case UPDATE -> {
                    var ret = MusicRingerEngineConnector.update(message.uuid(), IMPMusicTrackerFactory.loadByTag(message.tracker()));
                    NetworkManager.sendToServer(IMPPackets.MUSIC_RING_UPDATE_RESULT, new IMPPackets.MusicRingUpdateResultMessage(message.uuid(), message.waitId(), ret).toFBB());
                }
            }
        });
      /*  packetContext.queue(() -> {
            if (mc.getConnection() == null) return;
            var mm = MusicEngine.getInstance();
            switch (message.stateType()) {
                case PLAY -> {
                    mm.play(message.uuid(), message.elapsed());
                }
                case STOP -> {
                    mm.stop(message.uuid());
                }
                case UPDATE -> {
                    var plFlg = IMPPackets.MusicRingResponseStateType.NONE;

                    if (mm.isPlaying(message.uuid())) {
                        mm.updateMusicTracker(message.uuid(), message.uuid(), IMPMusicTrackerFactory.loadByTag(message.tracker()));
                        plFlg = IMPPackets.MusicRingResponseStateType.PLAYING;
                    } else if (mm.isLoading(message.uuid())) {
                        plFlg = IMPPackets.MusicRingResponseStateType.LOADING;
                    }
                    NetworkManager.sendToServer(IMPPackets.MUSIC_RING_UPDATE_RESULT, new IMPPackets.MusicRingUpdateResultMessage(message.uuid(), message.waitId(), plFlg).toFBB());
                }
            }
        });*/
    }

    public static void onMusicRingReadyResponseMessage(IMPPackets.MusicReadyMessage message, NetworkManager.PacketContext packetContext) {
        packetContext.queue(() -> {
            MusicRingerEngineConnector.load(message.uuid(), IMPMusicTrackerFactory.loadByTag(message.tracker()), message.source(), message.position(), (success, time, error, retry) -> {
                NetworkManager.sendToServer(IMPPackets.MUSIC_RING_READY_RESULT, new IMPPackets.MusicRingReadyResultMessage(message.waitId(), message.uuid(), success, retry, time).toFBB());
            });
        });
        //  packetContext.queue(() -> loadMusic(message.waitId(), message.uuid(), IMPMusicTrackerFactory.loadByTag(message.tracker()), message.source(), message.position(), 0, false));
    }

   /* private static void loadMusic(UUID waitID, UUID uuid, MusicTracker musicTracker, MusicSource source, long position, int tryCont, boolean autoPlay) {
        if (mc.getConnection() == null) return;
        var mm = MusicEngine.getInstance();
        if (tryCont >= 3 || mm.getCurrentMusicLoad() >= mm.getMaxMusicLoad()) {
            if (!autoPlay) {
                NetworkManager.sendToServer(IMPPackets.MUSIC_RING_READY_RESULT, new IMPPackets.MusicRingReadyResultMessage(waitID, uuid, false, false, 0).toFBB());
            }
        } else {
            mm.stop(uuid);
            mm.load(uuid, source, position, (success, time, error, retry) -> {
                if (!success && retry) {
                    Thread th = new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                            MusicUtils.runOnMusicTick(() -> loadMusic(waitID, uuid, musicTracker, source, position, tryCont + 1, autoPlay));
                        } catch (InterruptedException ignored) {
                        }
                    });
                    th.start();
                } else {
                    if (autoPlay) {
                        if (success) {
                            mm.play(uuid, time);
                        }
                    } else {
                        NetworkManager.sendToServer(IMPPackets.MUSIC_RING_READY_RESULT, new IMPPackets.MusicRingReadyResultMessage(waitID, uuid, success, retry, time).toFBB());
                    }
                }
            });
            mm.addSpeaker(uuid, uuid, musicTracker);*/
/*
            mm.loadAddMusicPlayer(uuid, playbackInfo, source, position, (result, time, player, retry) -> {
                if (!result && retry) {
                    Thread th = new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                            loadMusic(waitID, uuid, playbackInfo, source, position, tryCont + 1, autoPlay);
                        } catch (InterruptedException ignored) {
                        }
                    });
                    th.start();
                } else {
                    if (autoPlay) {
                        if (result) {
                            mm.playMusicPlayer(uuid, time);
                        }
                    } else {
                        NetworkManager.sendToServer(IMPPackets.MUSIC_RING_READY_RESULT, new IMPPackets.MusicRingReadyResultMessage(waitID, uuid, result, retry, time).toFBB());
                    }
                }
            });*/
       /* }
    }*/

    public static void onMusicSyncResponseMessage(IMPPackets.MusicSyncResponseMessage
                                                          message, NetworkManager.PacketContext packetContext) {
        packetContext.queue(() -> {
            var msm = MusicSyncManager.getInstance();
            switch (message.syncType) {
                case UPDATE -> msm.reset();
                case PLAYLIST_MY_LIST -> {
                    msm.myPlayList = Collections.unmodifiableList(message.playLists);
                    int msmc = 0;
                    for (MusicPlayList playList : msm.myPlayList) {
                        msmc += playList.getMusicList().size();
                    }
                    msm.myPlayListInfo = new MusicSyncManager.PlayListInfo((int) msm.myPlayList.stream().map(n -> n.getAuthority().getOwner()).distinct().count(), msm.myPlayList.size(), msmc);
                }
                case PLAYLIST_CAN_JOIN -> {
                    msm.canJoinPlayList = Collections.unmodifiableList(message.playLists);
                    int msmc = 0;
                    for (MusicPlayList playList : msm.canJoinPlayList) {
                        msmc += playList.getMusicList().size();
                    }
                    msm.canJoinPlayListInfo = new MusicSyncManager.PlayListInfo((int) msm.canJoinPlayList.stream().map(n -> n.getAuthority().getOwner()).distinct().count(), msm.canJoinPlayList.size(), msmc);
                }
                case MUSIC_BY_PLAYLIST -> {
                    msm.musics.remove(message.syncId);
                    msm.musics.put(message.syncId, Collections.unmodifiableList(message.musics));
                }
            }
        });
    }
}
