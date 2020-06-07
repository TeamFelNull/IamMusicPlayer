package net.morimori.imp.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.morimori.imp.sound.ClientSoundPlayer;
import net.morimori.imp.sound.WorldPlayListSoundData;
import net.morimori.imp.sound.WorldSoundKey;
import net.morimori.imp.sound.WorldSoundRinger;

public class MusicItem extends Item {

	public MusicItem(Properties properties) {
		super(properties);

	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
		ItemStack item = playerIn.getHeldItem(hand);
		ItemStack hitem = playerIn.getHeldItemOffhand();

		if (worldIn.isRemote) {
			/*
							SoundStreamPlayer ssp = new SoundStreamPlayer(
									"http://musicbird.leanstream.co/JCB019-MP3");

							ssp.startSound();
			*/

			if (!playerIn.isCrouching()) {

				//		SoundRinger sr = new SoundRinger(Paths.get("C:\\Users\\MORI\\Music\\playlist\\01 茜空.mp3"));
				//	SoundRinger sr = new StreamSoundRinger(
				//			"https://www.dropbox.com/s/8y3mbuv3e6jy2vu/01%20%E8%8C%9C%E7%A9%BA.mp3?dl=1");
				//	SoundRinger sr = new StreamSoundRinger("http://musicbird.leanstream.co/JCB019-MP3");
				WorldSoundKey sd = new WorldSoundKey(WorldPlayListSoundData.getWorldPlayListData(hitem));

				//	playerIn.sendMessage(new StringTextComponent("test=" + sd.isClientExistence()));

				WorldSoundRinger sr = new WorldSoundRinger(sd);

				//			sr.setPotision(1000 * 30);
				ClientSoundPlayer.INSTANS.addRingSound("test", sr);

			} else {
				ClientSoundPlayer.INSTANS.removeRingSound("test");
			}

			//	SoundRinger sr = new StreamSoundRinger("http://musicbird.leanstream.co/JCB019-MP3");
			//	sr.setPotision(1000 * 30);
			//	ClientSoundPlayer.INSTANS.addRingSound("test", sr);
		}

		return ActionResult.func_226248_a_(item);
	}
}
