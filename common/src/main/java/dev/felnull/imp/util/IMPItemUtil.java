package dev.felnull.imp.util;

import dev.felnull.imp.item.AntennaItem;
import dev.felnull.imp.item.CassetteTapeItem;
import dev.felnull.imp.item.IMPItems;
import net.minecraft.world.item.ItemStack;

public class IMPItemUtil {
    public static boolean isCassetteTape(ItemStack itemStack) {
        return itemStack.getItem() instanceof CassetteTapeItem;
    }

    public static boolean isAntenna(ItemStack itemStack) {
        return itemStack.getItem() instanceof AntennaItem;
    }

    public static boolean isRemotePlayBackAntenna(ItemStack stack) {
        return isAntenna(stack) && stack.is(IMPItems.PARABOLIC_ANTENNA);
    }
}
