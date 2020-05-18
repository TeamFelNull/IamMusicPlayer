package net.morimori.imp.item;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import net.morimori.imp.IkisugiMusicPlayer;

public class IMPItems {

	public static final Item SOUNDTEST = new MusicItem(new Item.Properties().group(IMPItemGroup.MOD_TAB))
			.setRegistryName(IkisugiMusicPlayer.MODID, "soundtest");
	public static final Item TEST_CASSETTE_TAPE = new CassetteTapeItem(
			new Item.Properties().group(IMPItemGroup.MOD_TAB).maxStackSize(1)).setRegistryName(IkisugiMusicPlayer.MODID,
					"test_cassette_tape");
	public static final Item NO_RECORD_CASSETTE_TAPE = new NoRecordCassetteTapeItem(
			new Item.Properties().group(IMPItemGroup.MOD_TAB).maxStackSize(1)).setRegistryName(IkisugiMusicPlayer.MODID,
					"no_record_cassette_tape");
	public static final Item RECORD_CASSETTE_TAPE = new RecordCassetteTapeItem(
			new Item.Properties().maxStackSize(1)).setRegistryName(IkisugiMusicPlayer.MODID,
					"record_cassette_tape");
	public static final Item OVERWRITABLE_CASSETTE_TAPE = new OverwritableCassetteTapeItem(
			new Item.Properties().group(IMPItemGroup.MOD_TAB).maxStackSize(1)).setRegistryName(IkisugiMusicPlayer.MODID,
					"overwritable_cassette_tape");

	public static final Item PARABOLIC_ANTENNA = newItem("parabolic_antenna", 1);
	public static final Item IRON_BOWL = newItem("iron_bowl");
	public static final Item IRON_STICK = newItem("iron_stick");
	public static final Item RECEIVER = newItem("receiver");
	public static final Item SPEAKER = newItem("speaker", 4);
	public static final Item ACOUSTIC_AMPLIFIER = newItem("acoustic_amplifier");
	public static final Item MONITOR = newItem("monitor");
	public static final Item KEYBOARD = newItem("keyboard");
	public static final Item COMPUTER = newItem("computer");
	public static final Item CASSETTE_PLAYER = newItem("cassette_player");
	public static final Item MAGNETIC_TAPE = newItem("magnetic_tape");

	public static void registerItem(IForgeRegistry<Item> r) {
		registryItem(r, PARABOLIC_ANTENNA);
		registryItem(r, NO_RECORD_CASSETTE_TAPE);
		registryItem(r, RECORD_CASSETTE_TAPE);
		registryItem(r, OVERWRITABLE_CASSETTE_TAPE);
		registryItem(r, IRON_BOWL);
		registryItem(r, IRON_STICK);
		registryItem(r, RECEIVER);
		registryItem(r, SPEAKER);
		registryItem(r, ACOUSTIC_AMPLIFIER);
		registryItem(r, MONITOR);
		registryItem(r, KEYBOARD);
		registryItem(r, COMPUTER);
		registryItem(r, CASSETTE_PLAYER);
		registryItem(r, MAGNETIC_TAPE);

			registryItem(r, SOUNDTEST);

	}

	private static void registryItem(IForgeRegistry<Item> r, Item i) {
		IkisugiMusicPlayer.LOGGER.info("Registering Item : " + i.getRegistryName());
		r.register(i);
	}

	private static Item newItem(String name) {
		return newItem(name, 64);
	}

	private static Item newItem(String name, int maxsize) {
		return new Item(new Item.Properties().group(IMPItemGroup.MOD_TAB).maxStackSize(maxsize))
				.setRegistryName(IkisugiMusicPlayer.MODID, name);
	}
}
