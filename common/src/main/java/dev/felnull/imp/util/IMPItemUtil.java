package dev.felnull.imp.util;

import dev.felnull.imp.item.CassetteTapeItem;
import dev.felnull.imp.item.ParabolicAntennaItem;
import net.minecraft.world.item.ItemStack;

public class IMPItemUtil {
    public static boolean isCassetteTape(ItemStack itemStack) {
        return itemStack.getItem() instanceof CassetteTapeItem;
    }

    public static boolean isAntenna(ItemStack itemStack) {
        return itemStack.getItem() instanceof ParabolicAntennaItem;
    }
}
