package red.felnull.imp.util;

import net.minecraft.item.ItemStack;
import red.felnull.imp.item.CassetteTapeItem;
import red.felnull.imp.item.IMPItems;

public class ItemHelper {
    public static boolean isAntenna(ItemStack stack) {
        return stack.getItem() == IMPItems.PARABOLIC_ANTENNA;
    }

    public static boolean isCassette(ItemStack stack) {
        return stack.getItem() instanceof CassetteTapeItem;
    }

}
