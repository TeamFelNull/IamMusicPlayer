package red.felnull.imp.handler;

import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.data.PlayMusicManeger;
import red.felnull.imp.music.resource.PlayList;
import red.felnull.imp.music.resource.PlayMusic;
import red.felnull.imp.packet.PlayMusicChangeRequestMessage;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;

import java.util.function.Supplier;

public class PlayMusicChangeRequestMessageHandler {
    public static void reversiveMessage(PlayMusicChangeRequestMessage message, Supplier<NetworkEvent.Context> ctx) {
        PlayMusic music = PlayMusic.getPlayMusicByUUID(message.uuid);
        PlayList pl = PlayList.getPlayListByMusic(music);
        if (pl.getCreatePlayerUUID().equals(IKSGPlayerUtil.getUUID(ctx.get().getSender()))) {
            PlayMusicManeger.instance().changePlayMusic(message.uuid, message.name, message.image, message.artist, message.album, message.year, message.genre);
        }
    }
}
