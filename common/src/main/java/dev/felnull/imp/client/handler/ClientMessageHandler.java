package dev.felnull.imp.client.handler;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.client.music.MusicEngine;
import dev.felnull.imp.client.music.MusicSyncManager;
import dev.felnull.imp.music.MusicPlaybackInfo;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.networking.IMPPackets;

import java.util.Collections;
import java.util.UUID;

public class ClientMessageHandler {

    public static void onMusicRingStateResponseMessage(IMPPackets.MusicRingStateMessage message, NetworkManager.PacketContext packetContext) {
        var mm = MusicEngine.getInstance();
        if (message.num == 0) {
            mm.playMusicPlayer(message.uuid, message.elapsed);
        } else if (message.num == 1) {
            mm.stopMusicPlayer(message.uuid);
            mm.stopLoadMusicPlayer(message.uuid);
        } else if (message.num == 2) {
            int plFlg = 0;
            if (mm.isPlaying(message.uuid)) {
                mm.updateMusicPlaybackInfo(message.uuid, message.playbackInfo);
                plFlg = 1;
            } else if (mm.isLoad(message.uuid)) {
                plFlg = 2;
            }
            NetworkManager.sendToServer(IMPPackets.MUSIC_RING_UPDATE_RESULT, new IMPPackets.MusicRingUpdateResultMessage(message.uuid, message.waitId, plFlg).toFBB());
        }
    }

    public static void onMusicRingReadyResponseMessage(IMPPackets.MusicReadyMessage message, NetworkManager.PacketContext packetContext) {
        packetContext.queue(() -> loadMusic(message.waitId, message.uuid, message.playbackInfo, message.source, message.position, 0, false));
    }

    private static void loadMusic(UUID waitID, UUID uuid, MusicPlaybackInfo playbackInfo, MusicSource source, long position, int tryCont, boolean autoPlay) {
        if (tryCont >= 3) {
            if (!autoPlay) {
                NetworkManager.sendToServer(IMPPackets.MUSIC_RING_READY_RESULT, new IMPPackets.MusicRingReadyResultMessage(waitID, uuid, false, false, 0).toFBB());
            }
        } else {
            var mm = MusicEngine.getInstance();
            mm.stopMusicPlayer(uuid);
            mm.stopLoadMusicPlayer(uuid);
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
            });
        }
    }

    public static void onMusicSyncResponseMessage(IMPPackets.MusicSyncResponseMessage message, NetworkManager.PacketContext packetContext) {
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
