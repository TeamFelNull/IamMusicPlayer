package red.felnull.imp.client.handler;

import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.client.data.MusicDownloader;
import red.felnull.imp.packet.WorldMusicSendByteMessage;

import java.util.UUID;
import java.util.function.Supplier;

public class WorldMusicSendByteMessageHandler {
    public static void reversiveMessage(WorldMusicSendByteMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);
        MusicDownloader.instance().byteResponse(UUID.fromString(message.byteUUID), message.data);
    }
}
