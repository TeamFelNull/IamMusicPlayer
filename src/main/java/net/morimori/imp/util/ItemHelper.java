package net.morimori.imp.util;

import net.minecraft.item.ItemStack;
import net.morimori.imp.item.CassetteTapeItem;
import net.morimori.imp.item.IMPItems;

public class ItemHelper {
	public static boolean isAntenna(ItemStack stack) {
		return stack.getItem() == IMPItems.PARABOLIC_ANTENNA;
	}

	public static boolean isCassette(ItemStack stack) {
		return stack.getItem() instanceof CassetteTapeItem;
	}

}
