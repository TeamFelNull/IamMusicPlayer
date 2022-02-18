package dev.felnull.imp.client.music;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.networking.IMPPackets;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public final Map<UUID, List<Music>> musics = new HashMap<>();
    private final Map<UUID, Long> musicUpdateTimes = new HashMap<>();

    public static MusicSyncManager getInstance() {
        return INSTANCE;
    }

    @Nullable
    public List<Music> getMusics(@Nullable UUID playList) {
        if (playList == null) return null;
        long time = 0;
        if (musicUpdateTimes.containsKey(playList)) {
            time = musicUpdateTimes.get(playList);
        } else {
            musicUpdateTimes.put(playList, 0L);
        }
        if (System.currentTimeMillis() - time >= 1000 * 60) {
            sendRequest(IMPPackets.MusicSyncType.MUSIC_BY_PLAYLIST, playList);
            musicUpdateTimes.put(playList, System.currentTimeMillis());
        }
        return musics.get(playList);
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
        musicUpdateTimes.clear();

        myPlayList = null;
        canJoinPlayList = null;
        musics.clear();
    }

    private void sendRequest(IMPPackets.MusicSyncType type, UUID uuid) {
        if (uuid == null) return;
        NetworkManager.sendToServer(IMPPackets.MUSIC_SYNC, new IMPPackets.MusicSyncRequestMessage(type, uuid).toFBB());
    }

    public static record PlayListInfo(int playerCount, int playListCount, int musicCount) {
    }
}
