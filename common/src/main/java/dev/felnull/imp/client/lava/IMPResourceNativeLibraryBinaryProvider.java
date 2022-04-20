package dev.felnull.imp.client.lava;

import com.sedmelluq.lava.common.natives.ResourceNativeLibraryBinaryProvider;
import com.sedmelluq.lava.common.natives.architecture.SystemType;

import java.io.InputStream;

public class IMPResourceNativeLibraryBinaryProvider extends ResourceNativeLibraryBinaryProvider {
    private static final String DEFAULT_RESOURCE_ROOT = "/natives/";

    public IMPResourceNativeLibraryBinaryProvider(Class<?> classLoaderSample) {
        super(classLoaderSample, DEFAULT_RESOURCE_ROOT);
    }

    @Override
    public InputStream getLibraryStream(SystemType systemType, String libraryName) {
        return null;
    }
}
