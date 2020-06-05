package net.morimori.imp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import net.morimori.imp.IamMusicPlayer;
import net.morimori.imp.item.BoomBoxItem;
import net.morimori.imp.item.IMPItemGroup;
import net.morimori.imp.item.WallOrFloorStateBlockItem;

public class IMPBlocks {
	public static final Block BOOMBOX = new BoomboxBlock(Block.Properties.create(Material.IRON)
			.sound(SoundType.LANTERN).hardnessAndResistance(1f, 0.5f).func_226896_b_()).setRegistryName(
					IamMusicPlayer.MODID, "boombox");
	public static final Block SOUNDFILE_UPLOADER = new SoundfileUploaderBlock(Block.Properties.create(Material.IRON)
			.sound(SoundType.METAL).hardnessAndResistance(1f, 0.5f).func_226896_b_()).setRegistryName(
					IamMusicPlayer.MODID, "soundfile_uploader");
	public static final Block CASSETTE_DECK = new CassetteDeckBlock(Block.Properties.create(Material.IRON)
			.sound(SoundType.METAL).hardnessAndResistance(1f, 0.5f).func_226896_b_()).setRegistryName(
					IamMusicPlayer.MODID, "cassette_deck");
	public static final Block CASSETTE_STORING = new CassetteStoringBlock(Block.Properties.create(Material.IRON)
			.sound(SoundType.METAL).hardnessAndResistance(1f, 0.5f).func_226896_b_()).setRegistryName(
					IamMusicPlayer.MODID, "cassette_storing");

	public static void registerBlock(IForgeRegistry<Block> r) {
		registryBlock(r, BOOMBOX);
		registryBlock(r, SOUNDFILE_UPLOADER);
		registryBlock(r, CASSETTE_DECK);
		registryBlock(r, CASSETTE_STORING);
	}

	public static void registerItem(IForgeRegistry<Item> r) {

		registryBlockItem(r, SOUNDFILE_UPLOADER);
		registryBlockItem(r, CASSETTE_DECK);
		registryWallblaeBlockItem(r, CASSETTE_STORING);
		r.register(new BoomBoxItem(BOOMBOX, new Item.Properties().group(IMPItemGroup.MOD_TAB))
				.setRegistryName(BOOMBOX.getRegistryName()));
	}

	private static void registryBlock(IForgeRegistry<Block> r, Block b) {
		r.register(b);
	}

	private static void registryWallblaeBlockItem(IForgeRegistry<Item> r, Block b) {
		r.register(new WallOrFloorStateBlockItem(b, new Item.Properties().group(IMPItemGroup.MOD_TAB))
				.setRegistryName(b.getRegistryName()));
	}

	private static void registryBlockItem(IForgeRegistry<Item> r, Block b) {
		r.register(new BlockItem(b, new Item.Properties().group(IMPItemGroup.MOD_TAB))
				.setRegistryName(b.getRegistryName()));
	}
}
