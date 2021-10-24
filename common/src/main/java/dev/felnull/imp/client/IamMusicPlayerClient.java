package dev.felnull.imp.client;

import dev.felnull.imp.client.music.loader.IMPMusicLoaders;
import dev.felnull.imp.client.music.tracker.IMPMusicTrackers;

public class IamMusicPlayerClient {
    public static void init() {
        IMPMusicTrackers.init();
        IMPMusicLoaders.init();
    }
}
