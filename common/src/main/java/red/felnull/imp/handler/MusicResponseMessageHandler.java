package red.felnull.imp.handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import red.felnull.imp.music.ServerWorldMusicManager;
import red.felnull.imp.packet.MusicResponseMessage;
import red.felnull.otyacraftengine.packet.IPacketMessageServerHandler;

public class MusicResponseMessageHandler implements IPacketMessageServerHandler<MusicResponseMessage> {
    @Override
    public boolean reversiveMessage(MusicResponseMessage musicResponseMessage, ServerPlayer serverPlayer, ServerGamePacketListenerImpl serverGamePacketListener) {
        if (musicResponseMessage.type == MusicResponseMessage.Type.READY_COMPLETE) {
            ServerWorldMusicManager.getInstance().readyComplete(serverPlayer.getGameProfile().getId(), musicResponseMessage.uuid, musicResponseMessage.time);
        } else if (musicResponseMessage.type == MusicResponseMessage.Type.READY_FAILURE) {
            ServerWorldMusicManager.getInstance().readyFailure(serverPlayer.getGameProfile().getId(), musicResponseMessage.uuid);
        }
        return true;
    }
}
