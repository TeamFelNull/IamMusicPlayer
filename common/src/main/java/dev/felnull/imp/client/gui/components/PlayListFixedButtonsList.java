package dev.felnull.imp.client.gui.components;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.otyacraftengine.client.gui.components.FixedButtonsList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class PlayListFixedButtonsList extends FixedButtonsList<MusicPlayList> {
    private static final ResourceLocation WIDGETS_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/widgets.png");

    public PlayListFixedButtonsList(int x, int y, int w, int h, int num, Component name, List<MusicPlayList> list, PressEntry<MusicPlayList> onPressEntry) {
        super(x, y, w, h, WIDGETS_TEXTURE, 0, 20, 256, 256, num, name, list, n -> new TextComponent(n.getName()), onPressEntry);
    }
}
