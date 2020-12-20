package red.felnull.imp.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MusicDiscItem;
import red.felnull.imp.item.IMPItemTags;

public class ItemHelper {
    public static boolean isAntenna(ItemStack stack) {
        return IMPItemTags.PARABOLIC_ANTENNA.contains(stack.getItem());
    }

    public static boolean isCassetteTape(ItemStack stack) {
        return stack.getItem() == Items.APPLE;
    }

    public static boolean isWrittenCassetteTape(ItemStack stack) {
        return stack.getItem() == Items.GOLDEN_APPLE;
    }

    public static boolean isMusicItem(ItemStack stack) {
        return stack.getItem() instanceof MusicDiscItem;
    }
}
