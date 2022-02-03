package dev.felnull.imp.client.gui.screen;

import dev.architectury.registry.menu.MenuRegistry;
import dev.felnull.imp.inventory.IMPMenus;

public class IMPScreenFactorys {
    public static void init() {
        MenuRegistry.registerScreenFactory(IMPMenus.MUSIC_MANAGER, MusicManagerScreen::new);
        MenuRegistry.registerScreenFactory(IMPMenus.BOOMBOX, BoomboxScreen::new);
    }
}
