package red.felnull.imp.client.renderer.blockentity.gui;

import dev.architectury.registry.menu.MenuRegistry;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.inventory.IMPMenus;

public class IMPScreenFactoryRegister {
    public static void init() {
        MenuRegistry.registerScreenFactory(IMPMenus.MUSIC_SHARING_DEVICE, MusicSharingDeviceScreen::new);
    }
}
