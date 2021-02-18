package red.felnull.imp.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.item.BoomboxBlockItem;
import red.felnull.imp.item.IMPItemGroup;

import java.util.ArrayList;
import java.util.List;

public class IMPBlocks {
    public static List<Block> MOD_BLOCKS = new ArrayList<Block>();
    public static List<Item> MOD_BLOCKITEMS = new ArrayList<Item>();

    public static final Block MUSIC_SHARING_DEVICE = register("music_sharing_device", new MusicSharingDeviceBlock(AbstractBlock.Properties.create(Material.IRON).sound(SoundType.METAL).setRequiresTool().hardnessAndResistance(3.0F)));
    public static final Block CASSETTE_DECK = register("cassette_deck", new CassetteDeckBlock(AbstractBlock.Properties.create(Material.IRON).sound(SoundType.METAL).setRequiresTool().hardnessAndResistance(3.0F)));
    public static final Block BOOMBOX = register("boombox", new BoomboxBlock(AbstractBlock.Properties.create(Material.IRON).sound(SoundType.METAL).setRequiresTool().hardnessAndResistance(3.0F)), (n, m) -> new BoomboxBlockItem(m, new Item.Properties().group(IMPItemGroup.MOD_TAB).maxStackSize(1)).setRegistryName(IamMusicPlayer.MODID, n));
    public static final Block CASSETTE_STORAGE = register("cassette_storage", new CassetteStorageBlock(AbstractBlock.Properties.create(Material.IRON).sound(SoundType.METAL).setRequiresTool().hardnessAndResistance(3.0F)));

    private static Block register(String name, Block block) {
        return register(name, block, (n, m) -> new BlockItem(m, new Item.Properties().group(IMPItemGroup.MOD_TAB)).setRegistryName(IamMusicPlayer.MODID, n));
    }

    private static Block register(String name, Block block, BlockItemIn blockitem) {
        MOD_BLOCKS.add(block.setRegistryName(IamMusicPlayer.MODID, name));
        MOD_BLOCKITEMS.add(blockitem.blockItem(name, block));
        return block;
    }

    private interface BlockItemIn {
        Item blockItem(String name, Block block);
    }
}
