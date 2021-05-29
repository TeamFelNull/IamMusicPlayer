package red.felnull.imp.client.gui.components.monitor;

import net.minecraft.network.chat.TranslatableComponent;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;

public class MSDOffMonitor extends MSDBaseMonitor {
    public MSDOffMonitor(MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TranslatableComponent("imp.msdscreen.off.title"), msdScreen, parentScreen, x, y, width, height);
        this.renderBackGround = false;
        this.renderHeader = false;
    }
}
