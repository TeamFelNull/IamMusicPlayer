package dev.felnull.imp.client.music.loaderold;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.music.loadertypes.IMPMusicLoaderTypes;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IMPMusicLoadersOld {
    protected static final Map<ResourceLocation, IMusicLoaderOld> LOADERS = new LinkedHashMap<>();
    public static final ResourceLocation LAVA_YOUTUBE = new ResourceLocation(IamMusicPlayer.MODID, "lava_youtube");
   // public static final ResourceLocation YOUTUBE_DOWNLOADER = new ResourceLocation(IamMusicPlayer.MODID, "youtube_downloader");
    public static final ResourceLocation LAVA_SOUNDCLOUD = new ResourceLocation(IamMusicPlayer.MODID, "lava_soundcloud");
    public static final ResourceLocation LAVA_HTTP = new ResourceLocation(IamMusicPlayer.MODID, "lava_http");
    // public static final ResourceLocation LAVA_NICONICO = new ResourceLocation(IamMusicPlayer.MODID, "lava_niconico");

    public static void init() {
        //registerLoader(YOUTUBE_DOWNLOADER, new YoutubeDownloaderMusicLoader());
        registerLoader(LAVA_YOUTUBE, new LavaPlayerMusicLoaderOld(IMPMusicLoaderTypes.YOUTUBE));
        registerLoader(LAVA_SOUNDCLOUD, new LavaPlayerMusicLoaderOld(IMPMusicLoaderTypes.SOUNDCLOUD));
        registerLoader(LAVA_HTTP, new LavaPlayerMusicLoaderOld(IMPMusicLoaderTypes.HTTP));
        //    registerLoader(LAVA_NICONICO, new LavaPlayerMusicLoader(IMPMusicLoaderTypes.NICONICO));
    }

    public static void registerLoader(ResourceLocation location, IMusicLoaderOld loader) {
        LOADERS.put(location, loader);
    }

    public static IMusicLoaderOld getLoader(ResourceLocation location) {
        return LOADERS.get(location);
    }

    public static List<IMusicLoaderOld> getLoaders() {
        return new ArrayList<>(LOADERS.values());
    }
}
