package dev.felnull.imp.client.music.media;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 音楽の再生メディア
 */
public interface MusicMedia {
    Component getMediaName();

    Component getEnterText();

    ResourceLocation getIcon();

    boolean isSearchable();

    MusicMediaResult load(String sourceName);

    default List<MusicMediaResult> search(String searchText) {
        return new ArrayList<>();
    }

    default String getName() {
        for (Map.Entry<String, MusicMedia> entry : IMPMusicMedias.MEDIAS.entrySet()) {
            if (entry.getValue().equals(this))
                return entry.getKey();
        }
        return null;
    }
}
