package dev.felnull.imp.client.lava;

import com.sedmelluq.lava.common.natives.NativeLibraryProperties;
import com.sedmelluq.lava.common.natives.architecture.SystemType;

import java.util.function.Predicate;

public class IMPSystemNativeLibraryProperties implements NativeLibraryProperties {
    private final Predicate<SystemType> systemFilter;
    private final String libraryName;

    public IMPSystemNativeLibraryProperties(String libraryName, Predicate<SystemType> systemFilter) {
        this.systemFilter = systemFilter;
        this.libraryName = libraryName;
    }

    @Override
    public String getLibraryPath() {
        return null;
    }

    @Override
    public String getLibraryDirectory() {
        var sys = detectMatchingSystemType(this, systemFilter);
        if (sys == null)
            throw new IllegalStateException("System type is null");
        var natName = sys.osType.identifier() + "-" + sys.architectureType.identifier();
        boolean ret = LavaNativeManager.getInstance().load(natName, sys.formatLibraryName(libraryName));
        if (!ret)
            throw new UnsatisfiedLinkError("Failed to load the library");
        var p = LavaPlayerLoader.getNaiveLibraryFolder().resolve(natName);
        return p.toAbsolutePath().toString();
    }

    @Override
    public String getExtractionPath() {
        return null;
    }

    @Override
    public String getSystemName() {
        return null;
    }

    @Override
    public String getLibraryFileNamePrefix() {
        return null;
    }

    @Override
    public String getLibraryFileNameSuffix() {
        return null;
    }

    @Override
    public String getArchitectureName() {
        return null;
    }

    private static SystemType detectMatchingSystemType(NativeLibraryProperties properties, Predicate<SystemType> systemFilter) {
        SystemType systemType;
        try {
            systemType = SystemType.detect(properties);
        } catch (IllegalArgumentException e) {
            return null;
        }
        if (systemFilter != null && !systemFilter.test(systemType))
            return null;
        return systemType;
    }
}
