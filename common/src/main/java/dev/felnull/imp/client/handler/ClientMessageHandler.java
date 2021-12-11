package dev.felnull.imp.client.handler;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.client.music.MusicSyncManager;
import dev.felnull.imp.networking.IMPPackets;

import java.util.Collections;

public class ClientMessageHandler {
    public static void onMusicSyncResponseMessage(IMPPackets.MusicSyncResponseMessage message, NetworkManager.PacketContext packetContext) {
        packetContext.queue(() -> {
            var msm = MusicSyncManager.getInstance();
            switch (message.syncType) {
                case UPDATE -> msm.reset();
                case PLAYLIST_MY_LIST -> msm.myPlayList = Collections.unmodifiableList(message.playLists);
                case PLAYLIST_CAN_JOIN -> msm.canJoinPlayList = Collections.unmodifiableList(message.playLists);
            }
        });
    }
}
