package dev.felnull.imp.client.lava;

import com.sedmelluq.lava.common.natives.NativeLibraryBinaryProvider;
import com.sedmelluq.lava.common.natives.NativeLibraryProperties;
import com.sedmelluq.lava.common.natives.architecture.SystemType;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.function.Predicate;

public class IMPSystemNativeLibraryProperties implements NativeLibraryProperties {
    private final Predicate<SystemType> systemFilter;
    private final String libraryName;
    private final NativeLibraryBinaryProvider binaryProvider;

    public IMPSystemNativeLibraryProperties(NativeLibraryBinaryProvider binaryProvider, String libraryName, Predicate<SystemType> systemFilter) {
        this.systemFilter = systemFilter;
        this.libraryName = libraryName;
        this.binaryProvider = binaryProvider;
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
        InputStream libraryStream = binaryProvider.getLibraryStream(sys, libraryName);
        if (libraryStream == null)
            throw new UnsatisfiedLinkError("Required library was not found");
        libraryStream = new BufferedInputStream(libraryStream);
        var p = LavaPlayerLoader.getNaiveLibraryFolder().resolve(sys.osType.identifier() + "-" + sys.architectureType.identifier());

        if (!p.toFile().exists() && !p.toFile().mkdirs())
            throw new IllegalStateException("Failed to create the folder of the native library");

        var lp = p.resolve(sys.formatLibraryName(libraryName)).toFile();
        try (FileOutputStream fileStream = new FileOutputStream(lp)) {
            IOUtils.copy(libraryStream, fileStream);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
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
