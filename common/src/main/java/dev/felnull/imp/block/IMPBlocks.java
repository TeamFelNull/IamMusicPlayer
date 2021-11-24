package dev.felnull.imp.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.item.IMPCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Function;

public class IMPBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(IamMusicPlayer.MODID, Registry.BLOCK_REGISTRY);
    private static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(IamMusicPlayer.MODID, Registry.ITEM_REGISTRY);
    public static final Block MUSIC_MANAGER = register("music_manager", new MusicManagerBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(3.0F)));

    private static Block register(String name, Block block) {
        return register(name, block, n -> new BlockItem(n, new Item.Properties().tab(IMPCreativeModeTab.MOD_TAB)));
    }

    private static Block register(String name, Block block, Function<Block, Item> blockItem) {
        BLOCKS.register(name, () -> block);
        BLOCK_ITEMS.register(name, () -> blockItem.apply(block));
        return block;
    }

    public static void init() {
        BLOCKS.register();
        BLOCK_ITEMS.register();
    }
}
