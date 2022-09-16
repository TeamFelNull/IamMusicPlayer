package dev.felnull.imp.data;

import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.BlockTagProviderWrapper;
import dev.felnull.otyacraftengine.data.provider.PoiTypeTagProviderWrapper;
import net.minecraft.world.level.block.Block;

public class IMPBlockTagProviderWrapper extends BlockTagProviderWrapper {
    public IMPBlockTagProviderWrapper(CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(crossDataGeneratorAccess);
    }

    @Override
    public void generateTag(TagProviderAccess<Block> providerAccess) {

    }
}
