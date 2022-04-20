package dev.felnull.imp.client.lava;

import com.sedmelluq.discord.lavaplayer.natives.ConnectorNativeLibLoader;
import com.sedmelluq.lava.common.natives.NativeLibraryLoader;
import com.sedmelluq.lava.common.natives.architecture.DefaultOperatingSystemTypes;
import com.sedmelluq.lava.common.natives.architecture.SystemType;
import dev.felnull.imp.util.IMPPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.function.Predicate;

public class LavaPlayerLoader {
    private static final Logger LOGGER = LogManager.getLogger(LavaPlayerLoader.class);
    private static final String LAVA_VERSION = "lava-walkyst-fork-1.3.97";

    public static void init() {
        NativeLibraryLoader[] loaders = null;
        try {
            var f = ConnectorNativeLibLoader.class.getDeclaredField("loaders");
            f.setAccessible(true);
            loaders = (NativeLibraryLoader[]) f.get(null);
        } catch (Exception ex) {
            LOGGER.error("Failed to access the native library loader", ex);
        }
        if (loaders == null) return;

        loaders[0] = createFiltered(ConnectorNativeLibLoader.class, "libmpg123-0", it -> it.osType == DefaultOperatingSystemTypes.WINDOWS);
        loaders[1] = create(ConnectorNativeLibLoader.class, "connector");
    }

    public static NativeLibraryLoader create(Class<?> classLoaderSample, String libraryName) {
        return createFiltered(classLoaderSample, libraryName, null);
    }

    public static NativeLibraryLoader createFiltered(Class<?> classLoaderSample, String libraryName, Predicate<SystemType> systemFilter) {
        var bp = new IMPResourceNativeLibraryBinaryProvider(classLoaderSample);
        return new NativeLibraryLoader(libraryName, systemFilter, new IMPSystemNativeLibraryProperties(libraryName, systemFilter), bp);
    }

    public static Path getNaiveLibraryFolder() {
        return IMPPaths.getNaiveLibraryFolder(LAVA_VERSION);
    }
}
