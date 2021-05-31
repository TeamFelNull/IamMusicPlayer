package red.felnull.imp.util;

import net.minecraft.world.item.ItemStack;
import red.felnull.imp.item.ParabolicAntennaItem;

public class ItemHelper {
    public static boolean isAntenna(ItemStack stack) {
        return stack.getItem() instanceof ParabolicAntennaItem;
    }
}
