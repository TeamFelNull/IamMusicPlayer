package red.felnull.imp.data;

import com.sun.webkit.network.URLs;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import red.felnull.imp.client.data.MusicSourceClientReferencesType;
import red.felnull.imp.client.data.MusicUploader;
import red.felnull.imp.musicplayer.PlayImage;
import red.felnull.imp.musicplayer.PlayLocation;
import red.felnull.imp.musicplayer.PlayMusic;
import red.felnull.imp.packet.PacketHandler;
import red.felnull.imp.packet.PlayMusicCreateRequestMessage;
import red.felnull.otyacraftengine.api.DataSendReceiverManager;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;
import red.felnull.otyacraftengine.util.IKSGStringUtil;

import java.nio.file.Paths;
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
    public void createPlayMusicRequest(String name, PlayImage image, byte[] imageData, PlayLocation location, MusicSourceClientReferencesType uploadtype, String pathOrURL, String artist, String album, String year, String genre) {

        if (location.getLocationType() == PlayLocation.LocationType.WORLD_FILE) {
            if (uploadtype == MusicSourceClientReferencesType.LOCAL_FILE) {
                MusicUploader.instance().startUpload(name, Paths.get(pathOrURL), location.getIdOrURL(), image, imageData);
            } else if (uploadtype == MusicSourceClientReferencesType.URL) {
                try {
                    MusicUploader.instance().startUpload(name, URLs.newURL(pathOrURL), location.getIdOrURL(), image, imageData);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
        
        if (image.getImageType() == PlayImage.ImageType.IMGAE)
            DataSendReceiverManager.instance().sendToServer(IMPWorldData.IMAGE, image.getName(), imageData);

        PacketHandler.INSTANCE.sendToServer(new PlayMusicCreateRequestMessage(name, image, location, artist, album, year, genre));

    }

    public void createPlayMusic(ServerPlayerEntity player, String name, PlayImage image, PlayLocation musicLocation, String artist, String album, String year, String genre) {
        String plUUID = UUID.randomUUID().toString();
        PlayMusic pm = new PlayMusic(plUUID, name, image, IKSGPlayerUtil.getUserName(player), IKSGPlayerUtil.getUUID(player), IKSGStringUtil.getTimeStamp(), musicLocation, artist, album, year, genre, 0);

    }
}
