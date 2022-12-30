package dev.felnull.imp.data;

import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.BlockLootTableProviderWrapper;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.level.block.Block;

public class IMPBlockLootTableProviderWrapper extends BlockLootTableProviderWrapper {
    public IMPBlockLootTableProviderWrapper(PackOutput packOutput, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, crossDataGeneratorAccess);
    }

    @Override
    public void generateBlockLootTables(BlockLootSubProvider blockLoot, BlockLootTableProviderAccess providerAccess) {
        providerAccess.dropSelf(IMPBlocks.BOOMBOX.get());
        providerAccess.dropSelf(IMPBlocks.MUSIC_MANAGER.get());
        providerAccess.dropSelf(IMPBlocks.CASSETTE_DECK.get());
    }

    @Override
    public Iterable<Block> getKnownBlocks() {
        return extract(IMPBlocks.BLOCKS);
    }
}
