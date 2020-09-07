package red.felnull.imp.data;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import red.felnull.imp.musicplayer.PlayList;
import red.felnull.imp.packet.PacketHandler;
import red.felnull.imp.packet.PlayListCreateRequestMessage;
import red.felnull.otyacraftengine.api.DataSendReceiverManager;
import red.felnull.otyacraftengine.api.ResponseSender;
import red.felnull.otyacraftengine.data.WorldDataManager;
import red.felnull.otyacraftengine.util.IKSGPictuerUtil;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;
import red.felnull.otyacraftengine.util.IKSGStringUtil;

import java.awt.image.BufferedImage;
import java.util.List;
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
    public void createPlayListRequest(String name, byte[] image, boolean anyone) {
        String imageUUID = UUID.randomUUID().toString();
        BufferedImage images = IKSGPictuerUtil.geBfftImage(image);
        DataSendReceiverManager.instance().sendToServer(IMPWorldData.PLAYLIST_IMAGE, imageUUID, image);
        PacketHandler.INSTANCE.sendToServer(new PlayListCreateRequestMessage(name, imageUUID, images.getWidth(), images.getHeight(), anyone));
    }

    public void createPlayList(ServerPlayerEntity player, String name, String imageID, int w, int h, boolean anyone) {
        String plUUID = UUID.randomUUID().toString();
        PlayList playList = new PlayList(plUUID, name, imageID, w, h, IKSGPlayerUtil.getUserName(player), IKSGPlayerUtil.getUUID(player), IKSGStringUtil.getTimeStamp(), anyone);
        PlayList.addPlayList(playList);
        playList.addPlayer(player);
    }

    @OnlyIn(Dist.CLIENT)
    public void joinPlayListRequest(String uuid) {
        ResponseSender.sendToServer(IMPWorldData.PLAYLIST_REQUEST, 0, uuid, new CompoundNBT());
    }

    public void joinPlayList(ServerPlayerEntity player, String uuid) {

        if (PlayList.getJoinedPlayLists(player).stream().anyMatch(n -> n.getUUID().equals(uuid)))
            return;

        PlayList pl = PlayList.getPlayListByUUID(uuid);
        if (pl != null) {
            pl.addPlayer(player);
        }
    }

    public CompoundNBT getAllPlayListNBT(ServerPlayerEntity player, boolean anyone) {
        CompoundNBT tag = new CompoundNBT();
        CompoundNBT pltag = WorldDataManager.instance().getWorldData(IMPWorldData.PLAYLIST_DATA).getCompound("playlists");

        List<PlayList> jpl = PlayList.getJoinedPlayLists(player);

        pltag.keySet().forEach(n -> {
            PlayList pl = new PlayList(n, pltag.getCompound(n));
            if (!jpl.contains(pl)) {
                if (anyone) {
                    tag.put(n, pltag.getCompound(n));
                } else {
                    if (pl.isAnyone()) {
                        tag.put(n, pltag.getCompound(n));
                    }
                }
            }
        });
        return tag;
    }

    public CompoundNBT getJoinedPlayListsNBT(ServerPlayerEntity player) {
        CompoundNBT tag = new CompoundNBT();
        List<PlayList> jpl = PlayList.getJoinedPlayLists(player);
        jpl.forEach(n -> tag.put(n.getUUID(), n.write(new CompoundNBT())));
        return tag;
    }
}
