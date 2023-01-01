package dev.felnull.imp.data;

import dev.felnull.imp.entity.village.IMPPoiType;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.PoiTypeTagProviderWrapper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import java.util.concurrent.CompletableFuture;

public class IMPPoiTypeTagProviderWrapper extends PoiTypeTagProviderWrapper {
    public IMPPoiTypeTagProviderWrapper(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, lookup, crossDataGeneratorAccess);
    }

    @Override
    public void generateTag(TagProviderAccess<PoiType, TagAppenderWrapper<PoiType>> providerAccess) {
        providerAccess.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(IMPPoiType.getResourceKey(IMPPoiType.DJ.get()));
    }
}
