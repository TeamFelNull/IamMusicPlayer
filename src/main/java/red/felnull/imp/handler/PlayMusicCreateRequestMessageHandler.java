package red.felnull.imp.handler;

import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.data.PlayMusicManeger;
import red.felnull.imp.music.resource.PlayList;
import red.felnull.imp.packet.PlayMusicCreateRequestMessage;

import java.util.function.Supplier;

public class PlayMusicCreateRequestMessageHandler {
    public static void reversiveMessage(PlayMusicCreateRequestMessage message, Supplier<NetworkEvent.Context> ctx) {
        PlayList list = PlayList.getPlayListByUUID(message.playListUUID);

        if (list == null || list.equals(PlayList.ALL))
            return;

        PlayMusicManeger.instance().createPlayMusic(ctx.get().getSender(), list, message.name, message.image, message.musicLocation, message.artist, message.album, message.year, message.genre, message.lethInSecond);
    }
}
