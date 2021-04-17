package red.felnull.imp.item;

import me.shedaniel.architectury.registry.CreativeTabs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import red.felnull.imp.IamMusicPlayer;

public class IMPCreativeModeTab {
    public static final CreativeModeTab MOD_TAB = CreativeTabs.create(new ResourceLocation(IamMusicPlayer.MODID, IamMusicPlayer.MODID), () -> new ItemStack(IMPItems.SPEAKER));
}
