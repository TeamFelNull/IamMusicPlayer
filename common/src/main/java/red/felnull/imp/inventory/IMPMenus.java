package red.felnull.imp.inventory;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.util.IKSGMenuUtil;

public class IMPMenus {
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(IamMusicPlayer.MODID, Registry.MENU_REGISTRY);
    public static final MenuType<MusicSharingDeviceMenu> MUSIC_SHARING_DEVICE = register("music_sharing_device", (i, inventory, blockPos, container) -> new MusicSharingDeviceMenu(i, blockPos, container, inventory));


    private static <T extends AbstractContainerMenu> MenuType<T> register(String name, IKSGMenuUtil.IKSGMenuFactory<T> factory) {
        MenuType<T> menuType = IKSGMenuUtil.createMenuType(factory);
        MENUS.register(name, () -> menuType);
        return menuType;
    }

    public static void init() {
        MENUS.register();
    }
}
