package red.felnull.imp.client.data;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import red.felnull.imp.music.resource.simple.SimpleMusicPlayList;
import red.felnull.imp.packet.SyncResourceRequestMessage;
import red.felnull.imp.packet.SyncType;
import red.felnull.otyacraftengine.util.IKSGPacketUtil;

import java.util.ArrayList;
import java.util.List;

public class IMPSyncClientManager {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final IMPSyncClientManager INSTANCE = new IMPSyncClientManager();
    public final List<SimpleMusicPlayList> myPlayLists = new ArrayList<>();
    public final List<SimpleMusicPlayList> publicPlayLists = new ArrayList<>();
    private long lastMyPlayListSyncTime;
    private long lastPublicPlayListSyncTime;

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


}
