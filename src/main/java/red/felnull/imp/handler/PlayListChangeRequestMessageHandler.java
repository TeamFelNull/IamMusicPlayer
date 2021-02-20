package red.felnull.imp.handler;

import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.packet.PlayListChangeRequestMessage;

import java.util.function.Supplier;

public class PlayListChangeRequestMessageHandler {
    public static void reversiveMessage(PlayListChangeRequestMessage message, Supplier<NetworkEvent.Context> ctx) {
        PlayListGuildManeger.instance().changePlayList(message.uuid,message.name,message.image,message.anyone);
    }
}
