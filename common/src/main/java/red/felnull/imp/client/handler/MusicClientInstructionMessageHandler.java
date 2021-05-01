package red.felnull.imp.client.handler;

import net.minecraft.client.multiplayer.ClientPacketListener;
import red.felnull.imp.client.music.MusicEngine;
import red.felnull.imp.packet.MusicClientInstructionMessage;
import red.felnull.otyacraftengine.packet.IPacketMessageClientHandler;

public class MusicClientInstructionMessageHandler implements IPacketMessageClientHandler<MusicClientInstructionMessage> {
    @Override
    public boolean reversiveMessage(MusicClientInstructionMessage musicInstructionMessage, ClientPacketListener clientPacketListener) {
        if (musicInstructionMessage.type == MusicClientInstructionMessage.Type.PLAY) {
            MusicEngine.getInstance().play(musicInstructionMessage.uuid, musicInstructionMessage.time, musicInstructionMessage.info);
        } else if (musicInstructionMessage.type == MusicClientInstructionMessage.Type.READY) {
            MusicEngine.getInstance().ready(musicInstructionMessage.uuid, musicInstructionMessage.location, musicInstructionMessage.time);
        }
        return true;
    }
}
