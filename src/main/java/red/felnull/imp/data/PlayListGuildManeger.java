package red.felnull.imp.data;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import red.felnull.imp.musicplayer.PlayList;
import red.felnull.imp.packet.PacketHandler;
import red.felnull.imp.packet.PlayListCreateRequestMessage;
import red.felnull.otyacraftengine.api.DataSendReceiverManager;
import red.felnull.otyacraftengine.data.WorldDataManager;
import red.felnull.otyacraftengine.util.IKSGPictuerUtil;

import java.awt.image.BufferedImage;
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
        BufferedImage images = IKSGPictuerUtil.geBfftImage(image);
        DataSendReceiverManager.instance().sendToServer(IMPWorldData.PLAYLIST_IMAGE, imageUUID, image);
        PacketHandler.INSTANCE.sendToServer(new PlayListCreateRequestMessage(name, imageUUID, images.getWidth(), images.getHeight()));
    }

    public void createPlayList(ServerPlayerEntity player, String name, String imageID, int w, int h) {
        String plUUID = UUID.randomUUID().toString();
        PlayList playList = new PlayList(plUUID, name, imageID, w, h);
        PlayList.addPlayList(playList);
        playList.addPlayer(player);
    }

    public CompoundNBT getAllPlayListNBT(boolean anyone) {
        CompoundNBT tag = new CompoundNBT();
        CompoundNBT pltag = WorldDataManager.instance().getWorldData(IMPWorldData.PLAYLIST_DATA).getCompound("playlists");
        pltag.keySet().forEach(n -> {
            if (anyone) {
                tag.put(n, pltag.getCompound(n));
            } else {
                PlayList pl = new PlayList(n, pltag.getCompound(n));
                if (pl.isAnyone()) {
                    tag.put(n, pltag.getCompound(n));
                }
            }
        });
        return tag;
    }
}
