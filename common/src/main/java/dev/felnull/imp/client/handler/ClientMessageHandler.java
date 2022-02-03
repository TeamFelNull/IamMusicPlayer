package dev.felnull.imp.client.handler;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.client.music.MusicSyncManager;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.networking.IMPPackets;

import java.util.Collections;

public class ClientMessageHandler {
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
