package dev.felnull.imp.data;

import dev.felnull.imp.entity.village.IMPPoiType;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.PoiTypeTagProviderWrapper;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class IMPPoiTypeTagProviderWrapper extends PoiTypeTagProviderWrapper {
    public IMPPoiTypeTagProviderWrapper(CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(crossDataGeneratorAccess);
    }

    @Override
    public void generateTag(TagProviderAccess<PoiType> providerAccess) {
        providerAccess.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(IMPPoiType.DJ.get());
    }
}
