package dev.felnull.imp.data;

import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.InputCopyProviderWrapper;

import java.nio.file.Path;

public class IMPInputCopyProviderWrapper extends InputCopyProviderWrapper {
    public IMPInputCopyProviderWrapper(CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(crossDataGeneratorAccess);
    }

    @Override
    public boolean isCopy(Path path) {
        System.out.println(path);
        return false;
    }
}
