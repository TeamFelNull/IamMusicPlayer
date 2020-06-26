package net.morimori.imp.tileentity;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.morimori.imp.IamMusicPlayer;
import net.morimori.imp.block.IMPBlocks;

public class IMPTileEntityTypes {

    @ObjectHolder(IamMusicPlayer.MODID + ":" + "boombox")
    public static TileEntityType<BoomboxTileEntity> BOOMBOX;

    @ObjectHolder(IamMusicPlayer.MODID + ":" + "soundfile_uploader")
    public static TileEntityType<SoundFileUploaderTileEntity> SOUNDFILE_UPLOADER;

    @ObjectHolder(IamMusicPlayer.MODID + ":" + "cassette_deck")
    public static TileEntityType<CassetteDeckTileEntity> CASSETTE_DECK;

    @ObjectHolder(IamMusicPlayer.MODID + ":" + "cassette_storing")
    public static TileEntityType<CassetteStoringTileEntity> CASSETTE_STORING;

    public static void registerTileEntityType(IForgeRegistry<TileEntityType<?>> r) {
        registryTileEntityType(r, BoomboxTileEntity::new, BOOMBOX, "boombox",
                IMPBlocks.BOOMBOX);

        registryTileEntityType(r, SoundFileUploaderTileEntity::new, SOUNDFILE_UPLOADER, "soundfile_uploader",
                IMPBlocks.SOUNDFILE_UPLOADER);

        registryTileEntityType(r, CassetteDeckTileEntity::new, CASSETTE_DECK, "cassette_deck",
                IMPBlocks.CASSETTE_DECK);

        registryTileEntityType(r, CassetteStoringTileEntity::new, CASSETTE_STORING, "cassette_storing",
                IMPBlocks.CASSETTE_STORING);

    }

    private static void registryTileEntityType(IForgeRegistry<TileEntityType<?>> r,
                                               Supplier<? extends TileEntity> factoryIn, TileEntityType<?> te, String name,
                                               Block... blocks) {
        te = TileEntityType.Builder.create(factoryIn, blocks).build(null)
                .setRegistryName(new ResourceLocation(IamMusicPlayer.MODID, name));
        r.register(te);

    }

}
