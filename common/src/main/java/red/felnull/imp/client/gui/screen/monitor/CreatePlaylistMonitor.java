package red.felnull.imp.client.gui.screen.monitor;

import net.minecraft.network.chat.TranslatableComponent;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;

public class CreatePlaylistMonitor extends MSDCreateBaseMonitor {
    public CreatePlaylistMonitor(MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TranslatableComponent("imp.msdmonitor.createplaylist"), msdScreen, parentScreen, x, y, width, height);
    }

    @Override
    public void init() {
        super.init();
    }
}
