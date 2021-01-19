package red.felnull.imp.client.handler;

import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.client.music.ClientWorldMusicManager;
import red.felnull.imp.client.music.MusicRinger;
import red.felnull.imp.packet.MusicRingUpdateMessage;

import java.util.function.Supplier;

public class MusicRingUpdateMessageHandlerr {
    public static void reversiveMessage(MusicRingUpdateMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);
        MusicRinger ringer = ClientWorldMusicManager.instance().getMusicRinger(message.uuid);
        if (ringer != null) {
            ringer.setVolume(message.musicVolume);
        }
    }
}
