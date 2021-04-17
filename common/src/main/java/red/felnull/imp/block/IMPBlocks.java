package red.felnull.imp.block;

import me.shedaniel.architectury.registry.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.item.IMPCreativeModeTab;

import java.util.function.Function;

public class IMPBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(IamMusicPlayer.MODID, Registry.BLOCK_REGISTRY);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(IamMusicPlayer.MODID, Registry.ITEM_REGISTRY);

    public static final Block MUSIC_SHARING_DEVICE = register("music_sharing_device", new MusicSharingDeviceBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(3.0F)));

    private static Block register(String name, Material materialIn, float hardness, float resistance) {
        return register(name, new Block(BlockBehaviour.Properties.of(materialIn).strength(hardness, resistance)));
    }

    private static Block register(String name, Material materialIn, SoundType sound, float hardness, float resistance) {
        return register(name, new Block(BlockBehaviour.Properties.of(materialIn).sound(sound).strength(hardness, resistance)));
    }

    private static Block register(String name, Block block) {
        return register(name, block, n -> new BlockItem(n, new Item.Properties().tab(IMPCreativeModeTab.MOD_TAB)));
    }

    private static Block register(String name, Block block, Function<Block, Item> blockitem) {
        BLOCKS.register(name, () -> block);
        BLOCK_ITEMS.register(name, () -> blockitem.apply(block));
        return block;
    }

    public static void init() {
        BLOCKS.register();
        BLOCK_ITEMS.register();
    }
}
