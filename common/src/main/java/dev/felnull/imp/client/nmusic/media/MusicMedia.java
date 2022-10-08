package dev.felnull.imp.client.nmusic.media;

import dev.felnull.otyacraftengine.client.gui.TextureSpecify;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 音楽の再生メディア
 */
public interface MusicMedia {
    Component getMediaName();

    Component getEnterText();

    TextureSpecify getIcon();

    boolean isSearchable();

    MusicMediaResult load(String sourceName);

    default List<MusicMediaResult> search(String searchText) {
        return new ArrayList<>();
    }
}
