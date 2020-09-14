package red.felnull.imp.data;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import red.felnull.imp.musicplayer.PlayMusic;
import red.felnull.otyacraftengine.api.DataSendReceiverManager;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;
import red.felnull.otyacraftengine.util.IKSGStringUtil;

import java.nio.file.Path;
import java.util.UUID;

public class PlayMusicManeger {
    private static PlayMusicManeger INSTANCE;

    public static void init() {
        INSTANCE = new PlayMusicManeger();
    }

    public static PlayMusicManeger instance() {
        return INSTANCE;
    }

    @OnlyIn(Dist.CLIENT)
    public void createPlayMusicRequest(String name, Path musicPath, int bitrate, boolean isServerIn, byte[] imageData, String artist, String album, String year, String genre) {
        String imageUUID = UUID.randomUUID().toString();
        String musicUUID = UUID.randomUUID().toString();
        DataSendReceiverManager.instance().sendToServer(IMPWorldData.PLAYLIST_IMAGE, imageUUID, imageData);

    }

    public void createPlayMusic(ServerPlayerEntity player, String name, String imageID, int imageWidth, int imageHeight, String musicUUID, String artist, String album, String year, String genre, int bitrate, long lethsec) {
        String plUUID = UUID.randomUUID().toString();
        PlayMusic pm = new PlayMusic(plUUID, name, imageID, imageWidth, imageHeight, IKSGPlayerUtil.getUserName(player), IKSGPlayerUtil.getUUID(player), IKSGStringUtil.getTimeStamp(), musicUUID, artist, album, year, genre, bitrate, lethsec);

    }
}
