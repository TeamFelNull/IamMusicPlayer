package red.felnull.imp.client.handler;

import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.client.music.ClientWorldMusicManager;
import red.felnull.imp.client.music.MusicRinger;
import red.felnull.imp.packet.MusicRingMessage;

import java.util.function.Supplier;

public class MusicRingMessageHandler {
    public static void reversiveMessage(MusicRingMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);

        MusicRinger ringer = new MusicRinger(message.uuid, message.music, message.musicPos);
        ClientWorldMusicManager.instance().addMusicPlayer(message.uuid, ringer);
        ringer.playWait(message.startPos);
    }
}
