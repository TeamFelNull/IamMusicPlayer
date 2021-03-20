package red.felnull.imp.handler;

import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.music.resource.PlayList;
import red.felnull.imp.packet.PlayListChangeRequestMessage;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;

import java.util.function.Supplier;

public class PlayListChangeRequestMessageHandler {
    public static void reversiveMessage(PlayListChangeRequestMessage message, Supplier<NetworkEvent.Context> ctx) {
        PlayList pl = PlayList.getPlayListByUUID(message.uuid);
        if (pl.getCreatePlayerUUID().equals(IKSGPlayerUtil.getUUID(ctx.get().getSender()))) {
            PlayListGuildManeger.instance().changePlayList(message.uuid, message.name, message.image, message.anyone);
        }
    }
}
