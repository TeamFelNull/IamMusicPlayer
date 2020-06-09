package net.morimori.imp.util;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.morimori.imp.client.screen.IMPSoundSlider;
import net.morimori.imp.item.CassetteTapeItem;
import net.morimori.imp.item.IMPItems;
import net.morimori.imp.sound.ClientSoundPlayer;
import net.morimori.imp.sound.INewSoundPlayer;
import net.morimori.imp.sound.SoundPos;
import net.morimori.imp.sound.WorldPlayListSoundData;

public class SoundHelper {
	private static Minecraft mc = Minecraft.getInstance();

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

	public static float getVolumeFromSoundPos(SoundPos pos, float motovol) {

		float v = (16f * motovol - (float) (pos.distance(mc.player.func_226277_ct_(), mc.player.func_226278_cu_() + 1,
				mc.player.func_226281_cx_()))) / 16f * motovol;

		return v <= 0 ? 0 : v;
	}

	public static double getDistanceAndSoundPosFromVolume(float motovol) {
		return 16 * motovol;
	}

	public static float getOptionVolume() {

		float master = mc.gameSettings.getSoundLevel(SoundCategory.MASTER);

		return IMPSoundSlider.AllSoundVolume <= master ? IMPSoundSlider.AllSoundVolume : master;
	}

	public static void soundPlayerTick(INewSoundPlayer isp) {

		if (!ClientSoundPlayer.INSTANS.playdSounds.containsValue(isp)) {
			ClientSoundPlayer.INSTANS.playdSounds.put(UUID.randomUUID().toString(), isp);
		}

	}
}
