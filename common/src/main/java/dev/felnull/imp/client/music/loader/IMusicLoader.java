package dev.felnull.imp.client.music.loader;

import dev.felnull.imp.client.music.player.IMusicPlayer;
import dev.felnull.imp.music.resource.MusicSource;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface IMusicLoader {
    IMusicPlayer createMusicPlayer(MusicSource source);

    boolean canLoad(MusicSource source) throws Exception;

    default ResourceLocation getRegistryName() {
        for (Map.Entry<ResourceLocation, IMusicLoader> entry : IMPMusicLoaders.LOADERS.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }
        return null;
    }
}
