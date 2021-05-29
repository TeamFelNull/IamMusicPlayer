package red.felnull.imp.client.gui.components.monitor;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.components.PlayLisFixedButtonsList;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.music.resource.MusicPlayList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MSDPlayListMonitor extends MSDBaseMonitor {
    private final List<MusicPlayList> playList = new ArrayList<>();


    public MSDPlayListMonitor(MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TranslatableComponent("imp.msdscreen.playlist.title"), msdScreen, parentScreen, x, y, width, height);
        this.renderHeader = false;
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
    }

    @Override
    public void init() {
        super.init();
        this.addRenderableWidget(new PlayLisFixedButtonsList(x + 1, y + 21, 29, 100, 5, new TextComponent("Play List"), this.playList, n -> new TextComponent(n.getName()), (n) -> {
            System.out.println(n.item().getName());
        }));
    }

}
