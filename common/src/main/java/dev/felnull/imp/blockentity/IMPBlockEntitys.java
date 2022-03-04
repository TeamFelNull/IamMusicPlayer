package dev.felnull.imp.blockentity;

import dev.architectury.registry.registries.DeferredRegister;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.otyacraftengine.OtyacraftEngine;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class IMPBlockEntitys {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES_REGISTER = DeferredRegister.create(OtyacraftEngine.MODID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    public static final BlockEntityType<MusicManagerBlockEntity> MUSIC_MANAGER = register("music_manager", MusicManagerBlockEntity::new, IMPBlocks.MUSIC_MANAGER);
    public static final BlockEntityType<CassetteDeckBlockEntity> CASSETTE_DECK = register("cassette_deck", CassetteDeckBlockEntity::new, IMPBlocks.CASSETTE_DECK);
    public static final BlockEntityType<BoomboxBlockEntity> BOOMBOX = register("boombox", BoomboxBlockEntity::new, IMPBlocks.BOOMBOX);

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.BlockEntitySupplier<T> constructor, Block... blocks) {
        var be = BlockEntityType.Builder.of(constructor, blocks).build(null);
        BLOCK_ENTITY_TYPES_REGISTER.register(name, () -> be);
        return be;
    }

    public static void init() {
        BLOCK_ENTITY_TYPES_REGISTER.register();
    }
}
