package red.felnull.imp.handler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import red.felnull.imp.music.MusicManager;
import red.felnull.imp.music.resource.MusicPlayList;
import red.felnull.imp.packet.SyncResourceRequestMessage;
import red.felnull.imp.packet.SyncResourceResponseMessage;
import red.felnull.imp.packet.SyncType;
import red.felnull.imp.util.NbtUtils;
import red.felnull.otyacraftengine.packet.IPacketMessageServerHandler;
import red.felnull.otyacraftengine.util.IKSGPacketUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SyncResourceRequestMessageHandler implements IPacketMessageServerHandler<SyncResourceRequestMessage> {
    private static final Map<UUID, Long> lastMyPlayListSyncTime = new HashMap<>();
    private static final Map<UUID, Long> lastPublicPlayListSyncTime = new HashMap<>();

    @Override
    public boolean reversiveMessage(SyncResourceRequestMessage syncResourceRequestMessage, ServerPlayer serverPlayer, ServerGamePacketListenerImpl serverGamePacketListener) {
        SyncType type = syncResourceRequestMessage.type;
        MusicManager manager = MusicManager.getInstance();
        if (type == SyncType.MY_PLAYLISTS) {
            if (lastMyPlayListSyncTime.containsKey(serverPlayer.getGameProfile().getId())) {
                if (System.currentTimeMillis() - lastMyPlayListSyncTime.get(serverPlayer.getGameProfile().getId()) <= minSyncTime())
                    return true;
            }
            CompoundTag data = new CompoundTag();
            NbtUtils.writeSimpleMusicPlayLists(data, "PlayList", manager.getMyPlayList(serverPlayer.getUUID()).stream().map(MusicPlayList::getSimple).toList());
            IKSGPacketUtil.sendToClientPacket(serverPlayer, new SyncResourceResponseMessage(type, data));
            lastMyPlayListSyncTime.put(serverPlayer.getGameProfile().getId(), System.currentTimeMillis());
        } else if (type == SyncType.PUBLIC_PLAYLISTS) {
            if (lastPublicPlayListSyncTime.containsKey(serverPlayer.getGameProfile().getId())) {
                if (System.currentTimeMillis() - lastPublicPlayListSyncTime.get(serverPlayer.getGameProfile().getId()) <= minSyncTime())
                    return true;
            }
            CompoundTag data = new CompoundTag();
            NbtUtils.writeSimpleMusicPlayLists(data, "PlayList", manager.getFilterPublicPlayList(serverPlayer.getUUID()).stream().map(MusicPlayList::getSimple).toList());
            IKSGPacketUtil.sendToClientPacket(serverPlayer, new SyncResourceResponseMessage(type, data));
            lastPublicPlayListSyncTime.put(serverPlayer.getGameProfile().getId(), System.currentTimeMillis());
        }

        return true;
    }

    private long minSyncTime() {
        return 1000 * 3;
    }

}
