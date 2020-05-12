package net.morimori.imp.item;

import java.io.IOException;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class MusicItem extends Item {

	public MusicItem(Properties properties) {
		super(properties);

	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
		ItemStack item = playerIn.getHeldItem(hand);

		try {
			Mp3File test = new Mp3File("C:\\Users\\MORI\\Music\\playlist\\サカナクション  新宝島.mp3");
			playerIn.sendMessage(new StringTextComponent(test.getId3v2Tag().getArtistUrl()));
		} catch (UnsupportedTagException | InvalidDataException | IOException e) {
			System.out.println(e.getLocalizedMessage());
		}

		return ActionResult.func_226248_a_(item);
	}
}
