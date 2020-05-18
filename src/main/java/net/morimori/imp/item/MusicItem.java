package net.morimori.imp.item;

import java.util.Map;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class MusicItem extends Item {
	public static Map<ResourceLocation, IBakedModel> test;

	public MusicItem(Properties properties) {
		super(properties);

	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
		ItemStack item = playerIn.getHeldItem(hand);

		/*	if (worldIn.isRemote) {

				SoundStreamPlayer ssp = new SoundStreamPlayer(
						"http://musicbird.leanstream.co/JCB019-MP3");

				ssp.startSound();

			}*/

		playerIn.sendMessage(new StringTextComponent(test.toString()));

		return ActionResult.func_226248_a_(item);
	}
}
