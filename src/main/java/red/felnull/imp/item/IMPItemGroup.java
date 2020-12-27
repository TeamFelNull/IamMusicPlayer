package red.felnull.imp.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.block.IMPBlocks;

public class IMPItemGroup {
    public static final ItemGroup MOD_TAB = new ItemGroup(IamMusicPlayer.MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(IMPBlocks.BOOMBOX);
        }
    };
}
