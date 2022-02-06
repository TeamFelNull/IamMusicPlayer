package dev.felnull.imp.inventory;

import dev.architectury.registry.registries.DeferredRegister;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.otyacraftengine.util.OEMenuUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class IMPMenus {
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(IamMusicPlayer.MODID, Registry.MENU_REGISTRY);
    public static final MenuType<MusicManagerMenu> MUSIC_MANAGER = registerBlockMenu("music_manager", (i, inventory, blockPos, container) -> new MusicManagerMenu(i, inventory, container, blockPos));
    public static final MenuType<CassetteDeckMenu> CASSETTE_DECK = registerBlockMenu("cassette_deck", (i, inventory, blockPos, container) -> new CassetteDeckMenu(i, inventory, container, blockPos));
    public static final MenuType<BoomboxMenu> BOOMBOX = registerItemAndBlockMenu("boombox", (i, inventory, blockPos, container) -> new BoomboxMenu(i, inventory, container, blockPos, ItemStack.EMPTY, null), (i, inventory, itemStack, iPlayerItemLocation, container) -> new BoomboxMenu(i, inventory, container, BlockPos.ZERO, itemStack, iPlayerItemLocation));

    private static <T extends AbstractContainerMenu> MenuType<T> registerItemAndBlockMenu(String name, OEMenuUtil.OEBlockMenuFactory<T> factoryBlock, OEMenuUtil.OEItemMenuFactory<T> factoryItem) {
        MenuType<T> menuType = OEMenuUtil.createMenuType(factoryBlock, factoryItem);
        MENUS.register(name, () -> menuType);
        return menuType;
    }

    private static <T extends AbstractContainerMenu> MenuType<T> registerBlockMenu(String name, OEMenuUtil.OEBlockMenuFactory<T> factory) {
        MenuType<T> menuType = OEMenuUtil.createMenuType(factory);
        MENUS.register(name, () -> menuType);
        return menuType;
    }

    public static void init() {
        MENUS.register();
    }
}
