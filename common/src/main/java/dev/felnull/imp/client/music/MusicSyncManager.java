package dev.felnull.imp.client.music;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.networking.IMPPackets;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MusicSyncManager {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final MusicSyncManager INSTANCE = new MusicSyncManager();
    public final List<MusicPlayList> myPlayList = new ArrayList<>();
    private long myPlayListLastUpdateTime;
    public final List<MusicPlayList> canJoinPlayList = new ArrayList<>();
    private long canJoinPlayListLastUpdateTime;

    public static MusicSyncManager getInstance() {
        return INSTANCE;
    }

    public List<MusicPlayList> getMyPlayList() {
        if (System.currentTimeMillis() - myPlayListLastUpdateTime >= 1000 * 60) {
            sendRequest(IMPPackets.MusicSyncType.PLAYLIST_MY_LIST, mc.player.getGameProfile().getId());
            myPlayListLastUpdateTime = System.currentTimeMillis();
        }
        return myPlayList;
    }

    public List<MusicPlayList> getCanJoinPlayList() {
        if (System.currentTimeMillis() - canJoinPlayListLastUpdateTime >= 1000 * 60) {
            sendRequest(IMPPackets.MusicSyncType.PLAYLIST_CAN_JOIN, mc.player.getGameProfile().getId());
            canJoinPlayListLastUpdateTime = System.currentTimeMillis();
        }
        return canJoinPlayList;
    }

    public void reset() {
        myPlayListLastUpdateTime = 0;
        canJoinPlayListLastUpdateTime = 0;

        myPlayList.clear();
        canJoinPlayList.clear();
    }

    private void sendRequest(IMPPackets.MusicSyncType type, UUID uuid) {
        NetworkManager.sendToServer(IMPPackets.MUSIC_SYNC, new IMPPackets.MusicSyncRequestMessage(type, uuid).toFBB());
    }
}
