package net.morimori.imp.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.morimori.imp.item.CassetteTapeItem;
import net.morimori.imp.item.IMPItems;
import net.morimori.imp.sound.WorldPlayListSoundData;

public class SoundHelper {
	public static boolean canPlay(ItemStack stack) {
		return isWritedSound(stack);
	}

	public static ItemStack writeSoundOnCassette(ItemStack stack, WorldPlayListSoundData WPLSD) {

		if (!ItemHelper.isCassette(stack))
			return stack;

		if (!isWritedSound(stack) || ((CassetteTapeItem) stack.getItem()).canOverwrite()) {

			stack = ((CassetteTapeItem) stack.getItem()).afterWriting(stack);

			WorldPlayListSoundData.setWorldPlayList(stack, WPLSD);
		}
		return stack;
	}

	public static ItemStack deleteSound(ItemStack stack) {

		if (!(stack.getItem() instanceof CassetteTapeItem))
			return ItemStack.EMPTY;

		if (((CassetteTapeItem) stack.getItem()).canOverwrite()) {

			ItemStack stacki = stack.copy();

			CompoundNBT tag = stacki.getOrCreateTag();

			tag.remove("WorldPlayListSoundData");

			return stacki;
		}

		return new ItemStack(IMPItems.MAGNETIC_TAPE, stack.getCount());
	}

	public static boolean isWritedSound(ItemStack stack) {
		CompoundNBT tag = stack.getOrCreateTag();
		return tag.contains("WorldPlayListSoundData");

	}

	public static String getCassetteSoundName(ItemStack stack) {
		if (!isWritedSound(stack))
			return stack.getDisplayName().getString();

		return StringHelper.deleteExtension(WorldPlayListSoundData.getWorldPlayListData(stack).getName());
	}

	public static boolean canWriteCassette(ItemStack stack) {
		if (ItemHelper.isCassette(stack)) {
			return isWritedSound(stack)
					? ((CassetteTapeItem) stack.getItem()).canOverwrite()
							? ((CassetteTapeItem) stack.getItem()).canWrite(stack)
							: false
					: ((CassetteTapeItem) stack.getItem()).canWrite(stack);
		}
		return false;
	}

}
