package red.felnull.imp.handler;

import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.music.resource.PlayList;
import red.felnull.imp.packet.PlayListRemoveRequestMessage;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;

import java.util.function.Supplier;

public class PlayListRemoveRequestMessageHandler {
    public static void reversiveMessage(PlayListRemoveRequestMessage message, Supplier<NetworkEvent.Context> ctx) {
        PlayList pl = PlayList.getPlayListByUUID(message.name);
        if (pl.getCreatePlayerUUID().equals(IKSGPlayerUtil.getUUID(ctx.get().getSender()))) {
            PlayListGuildManeger.instance().removePlayList(message.name);
        }
    }
}
