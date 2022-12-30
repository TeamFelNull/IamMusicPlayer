package dev.felnull.imp.item;

import dev.architectury.registry.CreativeTabRegistry;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.block.IMPBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class IMPCreativeModeTab {
    public static final CreativeTabRegistry.TabSupplier MOD_TAB = CreativeTabRegistry.create(new ResourceLocation(IamMusicPlayer.MODID, IamMusicPlayer.MODID), () -> new ItemStack(IMPBlocks.BOOMBOX.get()));
}
