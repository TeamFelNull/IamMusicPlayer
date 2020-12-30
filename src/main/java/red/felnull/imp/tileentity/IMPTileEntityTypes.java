package red.felnull.imp.tileentity;


import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.block.IMPBlocks;
import red.felnull.otyacraftengine.util.IKSGRegistryUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class IMPTileEntityTypes {

    public static List<TileEntityType<?>> MOD_TILEENTITYTYPES = new ArrayList<TileEntityType<?>>();

    @ObjectHolder(IamMusicPlayer.MODID + ":" + "music_sharing_device")
    public static TileEntityType<MusicSharingDeviceTileEntity> MUSIC_SHARING_DEVICE = register(MusicSharingDeviceTileEntity::new, new ResourceLocation(IamMusicPlayer.MODID, "music_sharing_device"), IMPBlocks.MUSIC_SHARING_DEVICE);

    @ObjectHolder(IamMusicPlayer.MODID + ":" + "cassette_deck")
    public static TileEntityType<CassetteDeckTileEntity> CASSETTE_DECK = register(CassetteDeckTileEntity::new, new ResourceLocation(IamMusicPlayer.MODID, "cassette_deck"), IMPBlocks.CASSETTE_DECK);

    @ObjectHolder(IamMusicPlayer.MODID + ":" + "boombox")
    public static TileEntityType<BoomboxTileEntity> BOOMBOX = register(BoomboxTileEntity::new, new ResourceLocation(IamMusicPlayer.MODID, "boombox"), IMPBlocks.BOOMBOX);


    private static <T extends TileEntityType<?>> T register(Supplier<? extends TileEntity> factoryIn, ResourceLocation location, Block... blocks) {
        T tile = IKSGRegistryUtil.craeteTileEntityType(factoryIn, location, blocks);
        MOD_TILEENTITYTYPES.add(tile);
        return tile;
    }
}
