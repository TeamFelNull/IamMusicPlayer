package dev.felnull.imp.client.music.loadertypes;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface IMusicLoaderType {

    public Component getName();

    public boolean isSearchable();

    public ResourceLocation getIcon();

    public MusicLoadResult load(String sourceName) throws InterruptedException;
}
