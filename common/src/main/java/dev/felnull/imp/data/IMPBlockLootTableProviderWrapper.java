package dev.felnull.imp.data;

import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.BlockLootTableProviderWrapper;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;

public class IMPBlockLootTableProviderWrapper extends BlockLootTableProviderWrapper {
    public IMPBlockLootTableProviderWrapper(CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(crossDataGeneratorAccess);
    }

    @Override
    public void generateBlockLootTables(BlockLoot blockLoot, BlockLootTableProviderAccess providerAccess) {
        blockLoot.dropSelf(IMPBlocks.BOOMBOX.get());
        blockLoot.dropSelf(IMPBlocks.MUSIC_MANAGER.get());
        blockLoot.dropSelf(IMPBlocks.CASSETTE_DECK.get());
    }

    @Override
    public Iterable<Block> getKnownBlocks() {
        return extract(IMPBlocks.BLOCKS);
    }
}