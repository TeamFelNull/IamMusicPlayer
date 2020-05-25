package net.morimori.imp.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.morimori.imp.file.DwonloadMusic;

public class MusicItem extends Item {

	public MusicItem(Properties properties) {
		super(properties);

	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
		ItemStack item = playerIn.getHeldItem(hand);

		if (worldIn.isRemote) {
			/*
							SoundStreamPlayer ssp = new SoundStreamPlayer(
									"http://musicbird.leanstream.co/JCB019-MP3");

							ssp.startSound();
			*/
		} else {
			DwonloadMusic.dwonloadSoundFromWorldPlayLists(worldIn.getServer());
		}

		return ActionResult.func_226248_a_(item);
	}
}
