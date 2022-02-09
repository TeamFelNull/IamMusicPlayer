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
            if (!mm.isPlaying(message.uuid) && mm.isLoad(message.uuid))
                mm.playMusicPlayer(message.uuid, message.elapsed);
        } else if (message.num == 1) {
            if (mm.isPlaying(message.uuid))
                mm.stopMusicPlayer(message.uuid);
            if (mm.isLoad(message.uuid))
                mm.stopLoadMusicPlayer(message.uuid);
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
            if (mm.isPlaying(uuid))
                mm.stopMusicPlayer(uuid);
            if (mm.isLoad(uuid))
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
