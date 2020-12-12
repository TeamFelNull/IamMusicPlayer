package red.felnull.imp.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.util.IKSGItemStackUtil;

public class IMPItemGroup {
    public static final ItemGroup MOD_TAB = new ItemGroup(IamMusicPlayer.MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(IMPItems.KAMESUTA_ANTENNA);
        }
    };
}
