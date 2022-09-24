package dev.felnull.imp.data;

import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;

import java.nio.file.Paths;

public class IamMusicPlayerDataGen {
    public static void init(CrossDataGeneratorAccess access) {
        access.getVanillaGenerator().getInputFolders().add(Paths.get("../../resources"));

        access.addProvider(new IMPRecipeProviderWrapper(access));
        var btp = new IMPBlockTagProviderWrapper(access);
        access.addProvider(btp);
        access.addProvider(new IMPItemTagProviderWrapper(access, btp));
        access.addProvider(new IMPPoiTypeTagProviderWrapper(access));
        access.addProvider(new IMPInputCopyProviderWrapper(access));
    }
}
