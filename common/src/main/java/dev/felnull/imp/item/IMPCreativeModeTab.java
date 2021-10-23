package dev.felnull.imp.item;

import dev.architectury.registry.CreativeTabRegistry;
import dev.felnull.imp.IamMusicPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class IMPCreativeModeTab {
    public static final CreativeModeTab MOD_TAB = CreativeTabRegistry.create(new ResourceLocation(IamMusicPlayer.MODID, IamMusicPlayer.MODID), () -> new ItemStack(Items.APPLE));
}
