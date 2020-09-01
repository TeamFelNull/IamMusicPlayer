package red.felnull.imp.data;

import net.minecraft.util.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.api.registries.OERegistries;

public class IMPWorldData {
    public static final ResourceLocation PLAYLIST_DATA = new ResourceLocation(IamMusicPlayer.MODID, "playlist_data");
    public static final ResourceLocation PLAYLIST_IMAGE = new ResourceLocation(IamMusicPlayer.MODID, "playlist_image");

    public static void register() {
        OERegistries.registrierWorldData(PLAYLIST_DATA, new PlayListWorldData());
        OERegistries.registrierServerRecevedPath(PLAYLIST_IMAGE, IamMusicPlayer.MODID + "\\playlist_image");
        OERegistries.registrierTextuerSendPath(PLAYLIST_IMAGE, IamMusicPlayer.MODID + "\\playlist_image");
    }
}
