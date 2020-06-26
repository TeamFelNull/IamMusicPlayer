package net.morimori.imp.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.morimori.imp.IamMusicPlayer;
import net.morimori.imp.block.IMPBlocks;

public class IMPItemGroup {
    public static final ItemGroup MOD_TAB = new ItemGroup(IamMusicPlayer.MODID) {
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(IMPBlocks.BOOMBOX);
        }
    };

}
