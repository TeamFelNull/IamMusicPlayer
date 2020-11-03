package net.morimori.imp.container;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.morimori.imp.IamMusicPlayer;

public class IMPContainerTypes {
	@ObjectHolder(IamMusicPlayer.MODID + ":" + "soundfile_uploader")
	public static ContainerType<SoundFileUploaderContainer> SOUNDFILE_UPLOADER;

	@ObjectHolder(IamMusicPlayer.MODID + ":" + "cassette_deck")
	public static ContainerType<CassetteDeckContainer> CASSETTE_DECK;

	@ObjectHolder(IamMusicPlayer.MODID + ":" + "cassette_storing")
	public static ContainerType<CassetteStoringContainer> CASSETTE_STORING;


	@SuppressWarnings("unchecked")
	public static void registerContainerType(IForgeRegistry<ContainerType<?>> r) {
		SOUNDFILE_UPLOADER = (ContainerType<SoundFileUploaderContainer>) IForgeContainerType
				.create((windowId, inv, data) -> {
					return new SoundFileUploaderContainer(windowId, inv, new Inventory(1), data.readBlockPos());
				}).setRegistryName(new ResourceLocation(IamMusicPlayer.MODID, "soundfile_uploader"));
		r.register(SOUNDFILE_UPLOADER);

		CASSETTE_DECK = (ContainerType<CassetteDeckContainer>) IForgeContainerType.create((windowId, inv, data) -> {
			return new CassetteDeckContainer(windowId, inv, new Inventory(3), data.readBlockPos());
		}).setRegistryName(new ResourceLocation(IamMusicPlayer.MODID, "cassette_deck"));
		r.register(CASSETTE_DECK);

		CASSETTE_STORING = (ContainerType<CassetteStoringContainer>) IForgeContainerType.create((windowId, inv, data) -> {
			return new CassetteStoringContainer(windowId, inv, new Inventory(16), data.readBlockPos());
		}).setRegistryName(new ResourceLocation(IamMusicPlayer.MODID, "cassette_storing"));
		r.register(CASSETTE_STORING);

	}

}
