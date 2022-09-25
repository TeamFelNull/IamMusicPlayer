package dev.felnull.imp.data;

import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.BlockTagProviderWrapper;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

public class IMPBlockTagProviderWrapper extends BlockTagProviderWrapper {
    public IMPBlockTagProviderWrapper(CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(crossDataGeneratorAccess);
    }

    @Override
    public void generateTag(TagProviderAccess<Block> providerAccess) {
        providerAccess.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(IMPBlocks.BOOMBOX.get(), IMPBlocks.CASSETTE_DECK.get(), IMPBlocks.MUSIC_MANAGER.get());
    }
}
