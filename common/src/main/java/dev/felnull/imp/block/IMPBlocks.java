package dev.felnull.imp.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.item.BoomboxItem;
import dev.felnull.imp.item.IMPCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class IMPBlocks {
    private static final Map<Block, Item> BLOCK_BY_ITEM = new HashMap<>();
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(IamMusicPlayer.MODID, Registry.BLOCK_REGISTRY);
    private static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(IamMusicPlayer.MODID, Registry.ITEM_REGISTRY);
    public static final Block MUSIC_MANAGER = register("music_manager", new MusicManagerBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(3.0F).requiresCorrectToolForDrops()));
    public static final Block CASSETTE_DECK = register("cassette_deck", new CassetteDeckBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(3.0F).requiresCorrectToolForDrops()));
    public static final Block BOOMBOX = register("boombox", new BoomboxBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.LANTERN).strength(3.0F)), n ->new

            BoomboxItem(n, new Item.Properties().

    tab(IMPCreativeModeTab.MOD_TAB).

    stacksTo(1)));

    private static Block register(String name, Block block) {
        return register(name, block, n -> new BlockItem(n, new Item.Properties().tab(IMPCreativeModeTab.MOD_TAB)));
    }

    private static Block register(String name, Block block, Function<Block, Item> blockItem) {
        BLOCKS.register(name, () -> block);
        var item = blockItem.apply(block);
        BLOCK_ITEMS.register(name, () -> item);
        BLOCK_BY_ITEM.put(block, item);
        return block;
    }

    public static Item getItemByBlock(Block block) {
        return BLOCK_BY_ITEM.getOrDefault(block, Items.AIR);
    }

    public static void init() {
        BLOCKS.register();
        BLOCK_ITEMS.register();
    }
}
