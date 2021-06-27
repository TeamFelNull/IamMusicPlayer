package red.felnull.imp.client.handler;

import net.minecraft.client.multiplayer.ClientPacketListener;
import red.felnull.imp.client.data.IMPSyncClientManager;
import red.felnull.imp.packet.SyncResourceResponseMessage;
import red.felnull.imp.packet.SyncType;
import red.felnull.imp.util.NbtUtils;
import red.felnull.otyacraftengine.packet.IPacketMessageClientHandler;

import java.util.ArrayList;

public class SyncResourceResponseMessageHandler implements IPacketMessageClientHandler<SyncResourceResponseMessage> {
    @Override
    public boolean reversiveMessage(SyncResourceResponseMessage syncResourceResponseMessage, ClientPacketListener clientPacketListener) {
        SyncType type = syncResourceResponseMessage.type;
        IMPSyncClientManager manager = IMPSyncClientManager.getInstance();
        if (type == SyncType.MY_PLAYLISTS) {
            NbtUtils.readSimpleMusicPlayLists(syncResourceResponseMessage.data, "PlayList", manager.myPlayLists);
        } else if (type == SyncType.PUBLIC_PLAYLISTS) {
            NbtUtils.readSimpleMusicPlayLists(syncResourceResponseMessage.data, "PlayList", manager.publicPlayLists);
        } else if (type == SyncType.MUSIC) {
            if (!manager.musics.containsKey(syncResourceResponseMessage.uuid))
                manager.musics.put(syncResourceResponseMessage.uuid, new ArrayList<>());
            NbtUtils.readMusics(syncResourceResponseMessage.data, "Music", manager.musics.get(syncResourceResponseMessage.uuid));
        } else if (type == SyncType.ALL_MUSIC) {
            NbtUtils.readMusics(syncResourceResponseMessage.data, "Music", manager.allMusics);
        }
        return true;
    }
}
