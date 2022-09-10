package dev.felnull.imp.data;

import dev.felnull.imp.item.IMPItemTags;
import dev.felnull.imp.item.IMPItems;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.BlockTagProviderWrapper;
import dev.felnull.otyacraftengine.data.provider.ItemTagProviderWrapper;
import org.jetbrains.annotations.NotNull;

public class IMPItemTagProviderWrapper extends ItemTagProviderWrapper {
    public IMPItemTagProviderWrapper(CrossDataGeneratorAccess crossDataGeneratorAccess, @NotNull BlockTagProviderWrapper blockTagProviderWrapper) {
        super(crossDataGeneratorAccess, blockTagProviderWrapper);
    }

    @Override
    public void generateTag(ItemTagProviderAccess providerAccess) {
        providerAccess.tag(IMPItemTags.CASSETTE_TAPE).add(IMPItems.CASSETTE_TAPE.get(), IMPItems.CASSETTE_TAPE_GLASS.get());
    }
}
