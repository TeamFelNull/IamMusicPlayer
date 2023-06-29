package dev.felnull.imp.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.item.BoomboxItem;
import dev.felnull.imp.item.IMPCreativeModeTabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

public class IMPBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(IamMusicPlayer.MODID, Registries.BLOCK);
    private static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(IamMusicPlayer.MODID, Registries.ITEM);
    public static final RegistrySupplier<Block> MUSIC_MANAGER = register("music_manager", () -> new MusicManagerBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL).requiresCorrectToolForDrops().strength(3.0F).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> CASSETTE_DECK = register("cassette_deck", () -> new CassetteDeckBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL).requiresCorrectToolForDrops().strength(3.0F).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> BOOMBOX = register("boombox", () -> new BoomboxBlock(BlockBehaviour.Properties.of().sound(SoundType.LANTERN).strength(3.0F)), n -> new BoomboxItem(n, new Item.Properties().arch$tab(IMPCreativeModeTabs.MOD_TAB).stacksTo(1)));

    private static RegistrySupplier<Block> register(String name, Supplier<Block> block) {
        return register(name, block, n -> new BlockItem(n, new Item.Properties().arch$tab(IMPCreativeModeTabs.MOD_TAB)));
    }

    private static RegistrySupplier<Block> register(String name, Supplier<Block> block, Function<Block, Item> blockItem) {
        var blockr = BLOCKS.register(name, block);
        BLOCK_ITEMS.register(name, () -> blockItem.apply(blockr.get()));
        return blockr;
    }

    public static void init() {
        BLOCKS.register();
        BLOCK_ITEMS.register();
    }
}
