package dev.felnull.imp.inventory;

import dev.architectury.registry.registries.DeferredRegister;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.otyacraftengine.util.OEMenuUtil;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class IMPMenus {
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(IamMusicPlayer.MODID, Registry.MENU_REGISTRY);
    public static final MenuType<MusicManagerMenu> MUSIC_MANAGER = register("music_manager", (i, inventory, blockPos, container) -> new MusicManagerMenu(i, blockPos, container, inventory));


    private static <T extends AbstractContainerMenu> MenuType<T> register(String name, OEMenuUtil.OEMenuFactory<T> factory) {
        MenuType<T> menuType = OEMenuUtil.createMenuType(factory);
        MENUS.register(name, () -> menuType);
        return menuType;
    }

    public static void init() {
        MENUS.register();
    }
}
