package dev.felnull.imp.inventory;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.otyacraftengine.item.location.IPlayerItemLocation;
import dev.felnull.otyacraftengine.util.OEMenuUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class IMPMenus {
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(IamMusicPlayer.MODID, Registry.MENU_REGISTRY);
    public static final RegistrySupplier<MenuType<MusicManagerMenu>> MUSIC_MANAGER = registerBlockMenu("music_manager", MusicManagerMenu::new);
    public static final RegistrySupplier<MenuType<CassetteDeckMenu>> CASSETTE_DECK = registerBlockMenu("cassette_deck", CassetteDeckMenu::new);
    public static final RegistrySupplier<MenuType<BoomboxMenu>> BOOMBOX = registerItemAndBlockMenu("boombox", BoomboxMenu::new);

    private static <T extends AbstractContainerMenu> RegistrySupplier<MenuType<T>> registerItemAndBlockMenu(String name, OEItemAndBlockMenuFactory<T> factoryItemAndBlock ) {
        return MENUS.register(name, () -> OEMenuUtil.createMenuType(factoryItemAndBlock.getBlockMenuFactory(), factoryItemAndBlock.getItemMenuFactory()));
    }

    private static <T extends AbstractContainerMenu> RegistrySupplier<MenuType<T>> registerBlockMenu(String name, OEMenuUtil.OEBlockMenuFactory<T> factory) {
        return MENUS.register(name, () -> OEMenuUtil.createMenuType(factory));
    }

    public static void init() {
        MENUS.register();
    }

    public interface OEItemAndBlockMenuFactory<T extends AbstractContainerMenu> {
        T create(int i, Inventory playerInventory, Container container, BlockPos pos, ItemStack itemStack, IPlayerItemLocation location);

         default OEMenuUtil.OEBlockMenuFactory<T> getBlockMenuFactory(){
            return (i, inventory, blockPos, container) -> create(i, inventory, container, blockPos, ItemStack.EMPTY, null);
        }
         default OEMenuUtil.OEItemMenuFactory<T> getItemMenuFactory(){
            return (i, inventory, itemStack, iPlayerItemLocation, container) -> create(i, inventory, container, BlockPos.ZERO, itemStack, iPlayerItemLocation);
        }
    }
}
