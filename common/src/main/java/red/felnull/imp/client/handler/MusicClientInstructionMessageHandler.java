package red.felnull.imp.client.handler;

import net.minecraft.client.multiplayer.ClientPacketListener;
import red.felnull.imp.client.music.MusicEngine;
import red.felnull.imp.client.music.loader.MusicLoaderThread;
import red.felnull.imp.packet.MusicClientInstructionMessage;
import red.felnull.imp.packet.MusicResponseMessage;
import red.felnull.otyacraftengine.packet.IPacketMessageClientHandler;
import red.felnull.otyacraftengine.util.IKSGPacketUtil;

public class MusicClientInstructionMessageHandler implements IPacketMessageClientHandler<MusicClientInstructionMessage> {
    @Override
    public boolean reversiveMessage(MusicClientInstructionMessage musicInstructionMessage, ClientPacketListener clientPacketListener) {
        if (musicInstructionMessage.type == MusicClientInstructionMessage.Type.PLAY) {
            MusicEngine.getInstance().play(musicInstructionMessage.uuid, musicInstructionMessage.time, musicInstructionMessage.info);
        } else if (musicInstructionMessage.type == MusicClientInstructionMessage.Type.READY) {
            MusicEngine.getInstance().ready(musicInstructionMessage.uuid, musicInstructionMessage.location, musicInstructionMessage.time, (n, m, l) -> {
                IKSGPacketUtil.sendToServerPacket(new MusicResponseMessage(n == MusicLoaderThread.MusicLoadResult.FAILURE ? MusicResponseMessage.Type.READY_FAILURE : MusicResponseMessage.Type.READY_COMPLETE, m, l));
            });
        }
        return true;
    }
}
