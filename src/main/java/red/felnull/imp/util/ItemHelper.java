package red.felnull.imp.util;

import net.minecraft.item.ItemStack;
import red.felnull.imp.item.IMPItemTags;

public class ItemHelper {
    public static boolean isAntenna(ItemStack stack) {
        return IMPItemTags.PARABOLIC_ANTENNA.func_230235_a_(stack.getItem());
    }
}
