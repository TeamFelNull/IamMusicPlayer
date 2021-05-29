package red.felnull.imp.client.gui.components;

import net.minecraft.network.chat.Component;
import red.felnull.imp.client.gui.components.monitor.MSDBaseMonitor;
import red.felnull.imp.music.resource.MusicPlayList;
import red.felnull.otyacraftengine.client.gui.components.FixedButtonsList;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlayLisFixedButtonsList extends FixedButtonsList<MusicPlayList> {
    public PlayLisFixedButtonsList(int x, int y, int w, int h, int num, Component name, List<MusicPlayList> list, Function<MusicPlayList, Component> listName, Consumer<PressState<MusicPlayList>> onPress) {
        super(x, y, w, h, MSDBaseMonitor.MSD_WIDGETS, 0, 32, 199, 199, num, name, list, listName, onPress);
    }

}
