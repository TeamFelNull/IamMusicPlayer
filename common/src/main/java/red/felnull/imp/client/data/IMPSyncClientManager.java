package red.felnull.imp.client.data;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import red.felnull.imp.music.resource.Music;
import red.felnull.imp.music.resource.simple.SimpleMusicPlayList;
import red.felnull.imp.packet.SyncResourceRequestMessage;
import red.felnull.imp.packet.SyncType;
import red.felnull.otyacraftengine.util.IKSGPacketUtil;

import java.util.*;

public class IMPSyncClientManager {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final IMPSyncClientManager INSTANCE = new IMPSyncClientManager();
    public final List<SimpleMusicPlayList> myPlayLists = new ArrayList<>();
    public final List<SimpleMusicPlayList> publicPlayLists = new ArrayList<>();
    public final Map<UUID, List<Music>> musics = new HashMap<>();
    public final List<Music> allMusics = new ArrayList<>();
    private long lastMyPlayListSyncTime;
    private long lastPublicPlayListSyncTime;
    private final Map<UUID, Long> lastMusicSyncTime = new HashMap<>();
    private long lastAllMusicSyncTime;

    public void resetMusicPlayListSync() {
        lastMyPlayListSyncTime = 0;
        lastPublicPlayListSyncTime = 0;
        lastMusicSyncTime.clear();
        lastAllMusicSyncTime = 0;
    }

    public static IMPSyncClientManager getInstance() {
        return INSTANCE;
    }

    public List<PlayerInfo> getOnlinePlayers() {
        ClientPacketListener cpl = mc.player.connection;
        return cpl.getOnlinePlayers().stream().toList();
    }

    public List<SimpleMusicPlayList> getMyPlayLists() {
        syncMyPlayLists();
        return myPlayLists;
    }

    public List<SimpleMusicPlayList> getPublicPlayLists() {
        syncPublicPlayLists();
        return publicPlayLists;
    }

    public List<Music> getMusics(UUID playlistID) {
        syncMusics(playlistID);

        if (musics.containsKey(playlistID))
            return musics.get(playlistID);

        return new ArrayList<>();
    }

    public List<Music> getAllMusics() {
        syncAllMusics();
        return allMusics;
    }

    private long minSyncTime() {
        return 1000 * 3;
    }

    public void syncMyPlayLists() {
        if (System.currentTimeMillis() - lastMyPlayListSyncTime >= minSyncTime()) {
            IKSGPacketUtil.sendToServerPacket(new SyncResourceRequestMessage(SyncType.MY_PLAYLISTS));
            lastMyPlayListSyncTime = System.currentTimeMillis();
        }
    }

    public void syncPublicPlayLists() {
        if (System.currentTimeMillis() - lastPublicPlayListSyncTime >= minSyncTime()) {
            IKSGPacketUtil.sendToServerPacket(new SyncResourceRequestMessage(SyncType.PUBLIC_PLAYLISTS));
            lastPublicPlayListSyncTime = System.currentTimeMillis();
        }
    }

    public void syncMusics(UUID playlistID) {
        long lst = 0;
        if (lastMusicSyncTime.containsKey(playlistID))
            lst = lastMusicSyncTime.get(playlistID);

        if (System.currentTimeMillis() - lst >= minSyncTime()) {
            IKSGPacketUtil.sendToServerPacket(new SyncResourceRequestMessage(SyncType.MUSIC, playlistID));
            lastMusicSyncTime.put(playlistID, System.currentTimeMillis());
        }
    }

    public void syncAllMusics() {
        if (System.currentTimeMillis() - lastAllMusicSyncTime >= minSyncTime()) {
            IKSGPacketUtil.sendToServerPacket(new SyncResourceRequestMessage(SyncType.ALL_MUSIC));
            lastAllMusicSyncTime = System.currentTimeMillis();
        }
    }

}
