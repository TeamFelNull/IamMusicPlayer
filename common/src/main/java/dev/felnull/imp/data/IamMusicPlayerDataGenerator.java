package dev.felnull.imp.data;

import dev.felnull.imp.data.advancements.IMPAdvancementProviderWrapper;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.DataProviderWrapper;
import dev.felnull.otyacraftengine.data.provider.DirectCopyProviderWrapper;
import net.minecraft.data.PackOutput;

import java.nio.file.Paths;

public class IamMusicPlayerDataGenerator {
    public static void init(CrossDataGeneratorAccess access) {
        access.addResourceInputFolders(Paths.get("../../resources"));

        access.addProviderWrapper(IMPRecipeProviderWrapper::new);
        var btp = access.addProviderWrapper(IMPBlockTagProviderWrapper::new);

        access.addProviderWrapper((DataProviderWrapper.LookupGeneratorAccessedFactory<DataProviderWrapper<?>>) (packOutput, lookup, generatorAccess) -> new IMPItemTagProviderWrapper(packOutput, lookup, generatorAccess, btp));
        access.addProviderWrapper(IMPPoiTypeTagProviderWrapper::new);
        access.addProviderWrapper(packOutput -> new DirectCopyProviderWrapper(packOutput, PackOutput.Target.DATA_PACK, "patchouli_books", access));
        access.addProviderWrapper(IMPBlockLootTableProviderWrapper::new);
        access.addProviderWrapper(IMPAdvancementProviderWrapper::new);
    }
}
