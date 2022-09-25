package dev.felnull.imp.data;

import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.InputCopyProviderWrapper;

import java.nio.file.Path;
import java.nio.file.Paths;

public class IMPInputCopyProviderWrapper extends InputCopyProviderWrapper {
    public IMPInputCopyProviderWrapper(CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(crossDataGeneratorAccess);
    }

    @Override
    public boolean isCopy(Path inputFolder, Path path) {
        return isChildDir(inputFolder, path, Paths.get("data\\iammusicplayer\\patchouli_books"));
    }

    protected boolean isChildDir(Path inputFolder, Path path, Path targetPath) {
        var rp = inputFolder.relativize(path);

        var rps = rp.toString().replace("\\", "/");
        var tps = targetPath.toString().replace("\\", "/");

        return rps.startsWith(tps);
    }
}
