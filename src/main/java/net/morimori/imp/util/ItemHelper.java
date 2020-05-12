package net.morimori.imp.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.morimori.imp.item.CassetteTapeItem;
import net.morimori.imp.item.IMPItems;
import net.morimori.imp.sound.WorldPlayListSoundData;

public class ItemHelper {
	public static boolean isAntenna(ItemStack stack) {
		return stack.getItem() == IMPItems.PARABOLIC_ANTENNA;
	}

	public static boolean isCassette(ItemStack stack) {
		return stack.getItem() instanceof CassetteTapeItem;
	}

	public static String getCassetteMusicName(ItemStack stack) {
		if (!isWritedSound(stack))
			return stack.getDisplayName().getString();

		return WorldPlayListSoundData.getWorldPlayListData(stack).getName();
	}

	public static boolean canWriteCassette(ItemStack stack) {
		if (isCassette(stack)) {
			return isWritedSound(stack)
					? ((CassetteTapeItem) stack.getItem()).canOverwrite()
							? ((CassetteTapeItem) stack.getItem()).canWrite(stack)
							: false
					: ((CassetteTapeItem) stack.getItem()).canWrite(stack);
		}
		return false;
	}

	public static boolean isWritedSound(ItemStack stack) {
		CompoundNBT tag = stack.getOrCreateTag();
		return tag.contains("WorldPlayListSoundData");

	}

	public static ItemStack writeSoundOnCassette(ItemStack stack, WorldPlayListSoundData WPLSD) {

		if (!isCassette(stack))
			return stack;

		if (!isWritedSound(stack) || ((CassetteTapeItem) stack.getItem()).canOverwrite()) {

			stack = ((CassetteTapeItem) stack.getItem()).afterWriting(stack);

			WorldPlayListSoundData.setWorldPlayList(stack, WPLSD);
		}
		return stack;
	}

	public static boolean canPlay(ItemStack stack) {

		return isWritedSound(stack);

	}
}
