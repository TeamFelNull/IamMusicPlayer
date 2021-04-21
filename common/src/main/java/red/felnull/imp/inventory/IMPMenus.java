package red.felnull.imp.inventory;

import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.MenuRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import red.felnull.imp.IamMusicPlayer;

public class IMPMenus {
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(IamMusicPlayer.MODID, Registry.MENU_REGISTRY);
    public static final MenuType<MusicSharingDeviceMenu> MUSIC_SHARING_DEVICE = register("music_sharing_device", (id, inventory, buf) -> {
        return new MusicSharingDeviceMenu(id, buf.readBlockPos(), new SimpleContainer(buf.readInt()), inventory);
    });

    private static <T extends AbstractContainerMenu> MenuType<T> register(String name, MenuRegistry.ExtendedMenuTypeFactory<T> factory) {
        MenuType<T> menuType = MenuRegistry.ofExtended(factory);
        MENUS.register(name, () -> menuType);
        return menuType;
    }

    public static void init() {
        MENUS.register();
    }
}
