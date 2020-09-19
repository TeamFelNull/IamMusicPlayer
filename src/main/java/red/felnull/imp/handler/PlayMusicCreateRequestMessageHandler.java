package red.felnull.imp.handler;

import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.data.PlayMusicManeger;
import red.felnull.imp.packet.PlayMusicCreateRequestMessage;

import java.util.function.Supplier;

public class PlayMusicCreateRequestMessageHandler {
    public static void reversiveMessage(PlayMusicCreateRequestMessage message, Supplier<NetworkEvent.Context> ctx) {
        PlayMusicManeger.instance().createPlayMusic(ctx.get().getSender(), message.name, message.image, message.musicUUID, message.artist, message.album, message.year, message.genre, message.bitrate, message.lethsec);
    }
}
