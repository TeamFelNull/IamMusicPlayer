package dev.felnull.imp.client.lava;

import com.sedmelluq.lava.common.natives.ResourceNativeLibraryBinaryProvider;
import com.sedmelluq.lava.common.natives.architecture.DefaultArchitectureTypes;
import com.sedmelluq.lava.common.natives.architecture.DefaultOperatingSystemTypes;
import com.sedmelluq.lava.common.natives.architecture.SystemType;
import dev.felnull.fnjl.util.FNDataUtil;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class IMPResourceNativeLibraryBinaryProvider extends ResourceNativeLibraryBinaryProvider {
    private static final String DEFAULT_RESOURCE_ROOT = "/natives/";
    private final Class<?> classLoaderSample;

    public IMPResourceNativeLibraryBinaryProvider(Class<?> classLoaderSample) {
        super(classLoaderSample, DEFAULT_RESOURCE_ROOT);
        this.classLoaderSample = classLoaderSample != null ? classLoaderSample : IMPResourceNativeLibraryBinaryProvider.class;
    }

    @Override
    public InputStream getLibraryStream(SystemType systemType, String libraryName) {
        InputStream stream;
        var resourcePath = DEFAULT_RESOURCE_ROOT + systemType.formatSystemName() + "/" + systemType.formatLibraryName(libraryName);
        if (systemType.osType == DefaultOperatingSystemTypes.DARWIN && systemType.architectureType == DefaultArchitectureTypes.ARMv8_64) {
            resourcePath = DEFAULT_RESOURCE_ROOT + systemType.formatSystemName() + "-aarch64" + "/" + systemType.formatLibraryName(libraryName);
            stream = classLoaderSample.getResourceAsStream(resourcePath);
        } else {
            stream = super.getLibraryStream(systemType, libraryName);
        }
        if (stream == null)
            return FNDataUtil.resourceExtractor(IMPResourceNativeLibraryBinaryProvider.class, resourcePath.substring(1));
        return new BufferedInputStream(stream);
    }
}
