package net.morimori.imp.container;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.morimori.imp.IkisugiMusicPlayer;

public class IMPContainerTypes {
	@ObjectHolder(IkisugiMusicPlayer.MODID + ":" + "soundfile_uploader")
	public static ContainerType<SoundFileUploaderContainer> SOUNDFILE_UPLOADER;

	@ObjectHolder(IkisugiMusicPlayer.MODID + ":" + "cassette_deck")
	public static ContainerType<CassetteDeckContainer> CASSETTE_DECK;

	@SuppressWarnings("unchecked")
	public static void registerContainerType(IForgeRegistry<ContainerType<?>> r) {
		SOUNDFILE_UPLOADER = (ContainerType<SoundFileUploaderContainer>) IForgeContainerType
				.create((windowId, inv, data) -> {
					return new SoundFileUploaderContainer(windowId, inv, new Inventory(1), data.readBlockPos());
				}).setRegistryName(new ResourceLocation(IkisugiMusicPlayer.MODID, "soundfile_uploader"));
		r.register(SOUNDFILE_UPLOADER);

		CASSETTE_DECK = (ContainerType<CassetteDeckContainer>) IForgeContainerType.create((windowId, inv, data) -> {
			return new CassetteDeckContainer(windowId, inv, new Inventory(3), data.readBlockPos());
		}).setRegistryName(new ResourceLocation(IkisugiMusicPlayer.MODID, "cassette_deck"));
		r.register(CASSETTE_DECK);

	}

}
