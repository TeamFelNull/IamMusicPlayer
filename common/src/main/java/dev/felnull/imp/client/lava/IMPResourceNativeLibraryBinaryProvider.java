package dev.felnull.imp.client.lava;

import com.sedmelluq.lava.common.natives.ResourceNativeLibraryBinaryProvider;
import com.sedmelluq.lava.common.natives.architecture.DefaultArchitectureTypes;
import com.sedmelluq.lava.common.natives.architecture.DefaultOperatingSystemTypes;
import com.sedmelluq.lava.common.natives.architecture.SystemType;

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
        if (systemType.osType == DefaultOperatingSystemTypes.DARWIN && systemType.architectureType == DefaultArchitectureTypes.ARMv8_64) {
            var resourcePath = DEFAULT_RESOURCE_ROOT + systemType.formatSystemName() + "-aarch64" + "/" + systemType.formatLibraryName(libraryName);
            return classLoaderSample.getResourceAsStream(resourcePath);
        }
        return super.getLibraryStream(systemType, libraryName);
    }
}
