package net.morimori.imp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import net.morimori.imp.IkisugiMusicPlayer;
import net.morimori.imp.item.IMPItemGroup;

public class IMPBlocks {
	public static final Block BOOMBOX = new BoomboxBlock(Block.Properties.create(Material.IRON)
			.sound(SoundType.LANTERN).hardnessAndResistance(1f, 0.5f).func_226896_b_()).setRegistryName(
					IkisugiMusicPlayer.MODID, "boombox");
	public static final BlockItem BOOMBOXITEM = (BlockItem) new BoomboxBlockItem(BOOMBOX, BOOMBOX, new Item.Properties()
			.group(IMPItemGroup.MOD_TAB)).setRegistryName(
					IkisugiMusicPlayer.MODID, "boombox");
	public static final Block SOUNDFILE_UPLOADER = new SoundfileUploaderBlock(Block.Properties.create(Material.IRON)
			.sound(SoundType.LANTERN).hardnessAndResistance(1f, 0.5f).func_226896_b_()).setRegistryName(
					IkisugiMusicPlayer.MODID, "soundfile_uploader");
	public static final Block CASSETTE_DECK = new CassetteDeckBlock(Block.Properties.create(Material.IRON)
			.sound(SoundType.LANTERN).hardnessAndResistance(1f, 0.5f).func_226896_b_()).setRegistryName(
					IkisugiMusicPlayer.MODID, "cassette_deck");
	public static final Block CASSETTE_STORING = new CassetteStoringBlock(Block.Properties.create(Material.IRON)
			.sound(SoundType.LANTERN).hardnessAndResistance(1f, 0.5f).func_226896_b_()).setRegistryName(
					IkisugiMusicPlayer.MODID, "cassette_storing");

	public static void registerBlock(IForgeRegistry<Block> r) {
		registryBlock(r, BOOMBOX);
		registryBlock(r, SOUNDFILE_UPLOADER);
		registryBlock(r, CASSETTE_DECK);
		registryBlock(r, CASSETTE_STORING);
	}

	public static void registerItem(IForgeRegistry<Item> r) {
		r.register(BOOMBOXITEM);
		registryBlockItem(r, SOUNDFILE_UPLOADER);
		registryBlockItem(r, CASSETTE_DECK);
		registryBlockItem(r, CASSETTE_STORING);
	}

	private static void registryBlock(IForgeRegistry<Block> r, Block b) {
		IkisugiMusicPlayer.LOGGER.info("Registering Block : " + b.getRegistryName());
		r.register(b);
	}

	private static void registryBlockItem(IForgeRegistry<Item> r, Block b) {
		IkisugiMusicPlayer.LOGGER.info("Registering BlockItem : " + b.getRegistryName());
		r.register(new BlockItem(b, new Item.Properties().group(IMPItemGroup.MOD_TAB))
				.setRegistryName(b.getRegistryName()));
	}
}
