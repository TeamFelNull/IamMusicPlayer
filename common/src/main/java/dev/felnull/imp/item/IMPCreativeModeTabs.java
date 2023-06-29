package dev.felnull.imp.item;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.block.IMPBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class IMPCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(IamMusicPlayer.MODID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> MOD_TAB = TABS.register("mod_tab", () -> CreativeTabRegistry.create(Component.literal("IMP TAB")/*new ResourceLocation(IamMusicPlayer.MODID, IamMusicPlayer.MODID)*/, () -> new ItemStack(IMPBlocks.BOOMBOX.get())));

    public static void init() {
        TABS.register();
    }
}
