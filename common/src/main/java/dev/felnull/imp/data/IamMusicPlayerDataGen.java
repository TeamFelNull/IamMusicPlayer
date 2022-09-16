package dev.felnull.imp.data;

import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;

public class IamMusicPlayerDataGen {
    public static void init(CrossDataGeneratorAccess access) {
        access.addProvider(new IMPRecipeProviderWrapper(access));
        var btp = new IMPBlockTagProviderWrapper(access);
        access.addProvider(btp);
        access.addProvider(new IMPItemTagProviderWrapper(access, btp));
        access.addProvider(new IMPPoiTypeTagProviderWrapper(access));
    }
}
