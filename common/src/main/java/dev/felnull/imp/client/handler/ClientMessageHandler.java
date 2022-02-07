package dev.felnull.imp.client.handler;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.client.music.MusicEngine;
import dev.felnull.imp.client.music.MusicSyncManager;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.networking.IMPPackets;

import java.util.Collections;

public class ClientMessageHandler {

    public static void onMusicReadyResponseMessage(IMPPackets.MusicReadyMessage message, NetworkManager.PacketContext packetContext) {
        packetContext.queue(() -> loadMusic(message, 0));
    }

    private static void loadMusic(IMPPackets.MusicReadyMessage message, int tryCont) {
        if (tryCont >= 3) {
            NetworkManager.sendToServer(IMPPackets.MUSIC_READY_RESULT, new IMPPackets.MusicReadyResultMessage(message.waitId, message.uuid, false, false).toFBB());
        } else {
            MusicEngine.getInstance().loadAddMusicPlayer(message.uuid, message.playbackInfo, message.source, message.position, (result, time, player, retry) -> {
                if (!result && retry) {
                    Thread th = new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                            loadMusic(message, tryCont + 1);
                        } catch (InterruptedException ignored) {
                        }
                    });
                    th.start();
                } else {
                    NetworkManager.sendToServer(IMPPackets.MUSIC_READY_RESULT, new IMPPackets.MusicReadyResultMessage(message.waitId, message.uuid, result, retry).toFBB());
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
