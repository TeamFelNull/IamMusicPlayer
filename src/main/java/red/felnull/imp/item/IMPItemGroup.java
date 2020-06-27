package red.felnull.imp.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.block.IMPBlocks;

public class IMPItemGroup {
    public static final ItemGroup MOD_TAB = new ItemGroup(IamMusicPlayer.MODID) {
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(IMPBlocks.BOOMBOX);
        }
    };

}
