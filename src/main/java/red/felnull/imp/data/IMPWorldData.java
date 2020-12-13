package red.felnull.imp.data;

import net.minecraft.util.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.api.registries.OERegistries;

import java.nio.file.Paths;

public class IMPWorldData {
    public static final ResourceLocation PLAYLIST_DATA = new ResourceLocation(IamMusicPlayer.MODID, "playlist_data");
    public static final ResourceLocation PLAYMUSIC_DATA = new ResourceLocation(IamMusicPlayer.MODID, "playmusic_data");
    public static final ResourceLocation IMAGE = new ResourceLocation(IamMusicPlayer.MODID, "image");
    public static final ResourceLocation PLAYLIST_REQUEST = new ResourceLocation(IamMusicPlayer.MODID, "playlist_request");
    public static final ResourceLocation SERVER_MUSIC_DATA = new ResourceLocation(IamMusicPlayer.MODID, "server_music_data");
    public static final ResourceLocation SEND_MUSIC_RESPONSE = new ResourceLocation(IamMusicPlayer.MODID, "send_music_response");
    public static final ResourceLocation WORLDMUSICFILEDATA = new ResourceLocation(IamMusicPlayer.MODID, "worldmusicfiledatainfo");

    public static void register() {
        OERegistries.registrierWorldData(PLAYLIST_DATA, new PlayListWorldData());
        OERegistries.registrierWorldData(PLAYMUSIC_DATA, new PlayMusicWorldData());
        OERegistries.registrierServerRecevedPath(IMAGE, Paths.get(IamMusicPlayer.MODID).resolve("image"));
        OERegistries.registrierTextuerSendPath(IMAGE, Paths.get(IamMusicPlayer.MODID).resolve("image"));
        OERegistries.registrierServerRecevedPath(SERVER_MUSIC_DATA, Paths.get(IamMusicPlayer.MODID).resolve("tmp"));
    }
}
