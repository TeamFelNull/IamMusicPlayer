package dev.felnull.imp.client.music.loadertypes;

import dev.felnull.imp.client.gui.screen.monitor.music_manager.SearchMusicMMMonitor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public interface IMusicLoaderType {

    public Component getName();

    public boolean isSearchable();

    public ResourceLocation getIcon();

    public MusicLoadResult load(String sourceName) throws InterruptedException;

    default public List<SearchMusicMMMonitor.SearchMusicEntry> search(String name) throws InterruptedException {
        return new ArrayList<>();
    }

    public Component getEnterText();
}
