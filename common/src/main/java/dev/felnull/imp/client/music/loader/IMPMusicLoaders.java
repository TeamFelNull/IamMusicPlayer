package dev.felnull.imp.client.music.loader;

import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import dev.felnull.imp.IamMusicPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IMPMusicLoaders {
    protected static final Map<ResourceLocation, IMusicLoader> LOADERS = new HashMap<>();
    public static final ResourceLocation LAVA_YOUTUBE = new ResourceLocation(IamMusicPlayer.MODID, "lava_youtube");

    public static void init() {
        registerLoader(LAVA_YOUTUBE, new LavaPlayerMusicLoader("youtube", new YoutubeAudioSourceManager()));
    }

    public static void registerLoader(ResourceLocation location, IMusicLoader loader) {
        LOADERS.put(location, loader);
    }

    public static IMusicLoader getLoader(ResourceLocation location) {
        return LOADERS.get(location);
    }

    public static List<IMusicLoader> getLoaders() {
        return new ArrayList<>(LOADERS.values());
    }
}
