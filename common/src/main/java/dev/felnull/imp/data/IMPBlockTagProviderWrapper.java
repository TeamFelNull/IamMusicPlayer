package dev.felnull.imp.data;

import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.BlockTagProviderWrapper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class IMPBlockTagProviderWrapper extends BlockTagProviderWrapper {
    public IMPBlockTagProviderWrapper(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, lookup, crossDataGeneratorAccess);
    }

    @Override
    public void generateTag(IntrinsicTagProviderAccess<Block> providerAccess) {
        providerAccess.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(IMPBlocks.BOOMBOX.get(), IMPBlocks.CASSETTE_DECK.get(), IMPBlocks.MUSIC_MANAGER.get());
    }
}
