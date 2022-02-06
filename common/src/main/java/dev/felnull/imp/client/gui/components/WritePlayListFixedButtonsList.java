package dev.felnull.imp.client.gui.components;

import dev.felnull.imp.music.resource.MusicPlayList;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

public class WritePlayListFixedButtonsList extends PlayListFixedButtonsList {
    public WritePlayListFixedButtonsList(int x, int y,  List<MusicPlayList> list, PressEntry<MusicPlayList> onPressEntry) {
        super(x, y, 68, 42, 6, new TranslatableComponent("imp.fixedList.myPlaylist"), list, onPressEntry);
    }
}
