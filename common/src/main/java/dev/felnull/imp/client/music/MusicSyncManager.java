package dev.felnull.imp.client.music;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.networking.IMPPackets;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.UUID;

public class MusicSyncManager {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final MusicSyncManager INSTANCE = new MusicSyncManager();
    public List<MusicPlayList> myPlayList;
    private long myPlayListLastUpdateTime;
    public PlayListInfo myPlayListInfo;
    public List<MusicPlayList> canJoinPlayList;
    private long canJoinPlayListLastUpdateTime;
    public PlayListInfo canJoinPlayListInfo;

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

    public PlayListInfo getMyPlayListInfo() {
        return myPlayListInfo;
    }

    public List<MusicPlayList> getCanJoinPlayList() {
        if (System.currentTimeMillis() - canJoinPlayListLastUpdateTime >= 1000 * 60) {
            sendRequest(IMPPackets.MusicSyncType.PLAYLIST_CAN_JOIN, mc.player.getGameProfile().getId());
            canJoinPlayListLastUpdateTime = System.currentTimeMillis();
        }
        return canJoinPlayList;
    }

    public PlayListInfo getCanJoinPlayListInfo() {
        return canJoinPlayListInfo;
    }

    public void reset() {
        myPlayListLastUpdateTime = 0;
        canJoinPlayListLastUpdateTime = 0;

        myPlayList = null;
        canJoinPlayList = null;
    }

    private void sendRequest(IMPPackets.MusicSyncType type, UUID uuid) {
        NetworkManager.sendToServer(IMPPackets.MUSIC_SYNC, new IMPPackets.MusicSyncRequestMessage(type, uuid).toFBB());
    }

    public static record PlayListInfo(int playerCount, int playListCount, int musicCount) {
    }
}
