package dev.felnull.imp.client.music.subtitle;

import net.minecraft.network.chat.Component;

import java.util.List;

public interface IMusicSubtitle {
    boolean isExist();

    void load() throws Exception;

    List<Component> getSubtitle(long last, long current);
}
