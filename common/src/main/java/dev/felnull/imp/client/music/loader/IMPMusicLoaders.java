package dev.felnull.imp.client.music.loader;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class IMPMusicLoaders {
    private static final List<Supplier<MusicLoader>> LOADER_FACTORIES = new ArrayList<>();

    public static void init() {
        register(LavaMusicLoader::new);
    }

    public static void register(Supplier<MusicLoader> loaderSupplier) {
        LOADER_FACTORIES.add(loaderSupplier);
    }

    public static List<Supplier<MusicLoader>> getAllLoader() {
        synchronized (LOADER_FACTORIES) {
            return ImmutableList.copyOf(LOADER_FACTORIES);
        }
    }
}
