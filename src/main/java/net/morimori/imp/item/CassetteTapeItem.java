package net.morimori.imp.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.morimori.imp.sound.SoundData;
import net.morimori.imp.util.ItemHelper;

public class CassetteTapeItem extends Item {

	public CassetteTapeItem(Properties properties) {
		super(properties);

	}

	public boolean canWrite(ItemStack stack) {
		return true;
	}

	public boolean canOverwrite() {

		return true;
	}

	public ItemStack afterWriting(ItemStack befre) {
		return befre;
	}

	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		SoundData.addSoundDataTooltip(stack, tooltip);
	}

	public ITextComponent getDisplayName(ItemStack stack) {
		return ItemHelper.isWritedSound(stack)
				? new TranslationTextComponent("item.ikisugimusicplayer.cassette_tape.written",
						ItemHelper.getCassetteMusicName(
								stack))
				: super.getDisplayName(stack);
	}
}
