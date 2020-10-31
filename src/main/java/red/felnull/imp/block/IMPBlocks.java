package red.felnull.imp.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.item.IMPItemGroup;

import java.util.ArrayList;
import java.util.List;

public class IMPBlocks {
    public static List<Block> MOD_BLOCKS = new ArrayList<Block>();
    public static List<Item> MOD_BLOCKITEMS = new ArrayList<Item>();

    public static final Block MUSIC_SHARING_DEVICE = register("music_sharing_device", new MusicSharingDeviceBlock(AbstractBlock.Properties.create(Material.IRON).sound(SoundType.METAL).setRequiresTool().hardnessAndResistance(3.0F)));

    private static Block register(String name, Block block) {
        return register(name, block, new BlockItem(block, new Item.Properties().group(IMPItemGroup.MOD_TAB)).setRegistryName(IamMusicPlayer.MODID, name));
    }

    private static Block register(String name, Block block, Item blockitem) {
        MOD_BLOCKS.add(block.setRegistryName(IamMusicPlayer.MODID, name));
        MOD_BLOCKITEMS.add(blockitem);
        return block;
    }
}
