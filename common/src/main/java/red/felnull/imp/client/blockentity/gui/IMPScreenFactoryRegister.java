package red.felnull.imp.client.blockentity.gui;

import me.shedaniel.architectury.registry.MenuRegistry;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.inventory.IMPMenus;

public class IMPScreenFactoryRegister {
    public static void init() {
        MenuRegistry.registerScreenFactory(IMPMenus.MUSIC_SHARING_DEVICE, MusicSharingDeviceScreen::new);
    }
}
