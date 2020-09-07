package red.felnull.imp.handler;

import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.packet.PlayListCreateRequestMessage;

import java.util.function.Supplier;

public class PlayListCreateRequestMessageHandler {
    public static void reversiveMessage(PlayListCreateRequestMessage message, Supplier<NetworkEvent.Context> ctx) {
        PlayListGuildManeger.instance().createPlayList(ctx.get().getSender(), message.name, message.imageID, message.w, message.h, message.anyone);
    }
}
