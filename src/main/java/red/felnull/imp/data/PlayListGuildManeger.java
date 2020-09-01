package red.felnull.imp.data;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import red.felnull.imp.musicplayer.PlayList;
import red.felnull.imp.packet.PacketHandler;
import red.felnull.imp.packet.PlayListCreateRequestMessage;
import red.felnull.otyacraftengine.api.DataSendReceiverManager;

import java.util.UUID;

public class PlayListGuildManeger {

    private static PlayListGuildManeger INSTANCE;

    public PlayListGuildManeger() {
    }

    public static void init() {
        INSTANCE = new PlayListGuildManeger();
    }

    public static PlayListGuildManeger instance() {
        return INSTANCE;
    }

    @OnlyIn(Dist.CLIENT)
    public void createPlayListRequest(String name, byte[] image) {
        String imageUUID = UUID.randomUUID().toString();
        DataSendReceiverManager.instance().sendToServer(IMPWorldData.PLAYLIST_IMAGE, imageUUID, image);
        PacketHandler.INSTANCE.sendToServer(new PlayListCreateRequestMessage(name, imageUUID));
    }

    public void createPlayList(ServerPlayerEntity player, String name, String imageID) {
        String plUUID = UUID.randomUUID().toString();
        PlayList playList = new PlayList(plUUID, name, imageID);
        PlayList.addPlayList(playList);
        playList.addPlayer(player);
    }

}
