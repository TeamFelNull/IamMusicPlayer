package dev.felnull.imp.client.music.loadertypes;

import java.util.LinkedHashMap;
import java.util.Map;

public class IMPMusicLoaderTypes {
    private static final Map<String, IMusicLoaderType> LOADER_TYPES = new LinkedHashMap<>();
    public static final String YOUTUBE = "youtube";
    public static final String HTTP = "http";

    public static void init() {
        registerLoaderType(YOUTUBE, new YoutubeMusicLoaderType());
        registerLoaderType(HTTP, new HttpURLMusicLoaderType());
    }

    public static void registerLoaderType(String name, IMusicLoaderType loaderType) {
        LOADER_TYPES.put(name, loaderType);
    }

    public static Map<String, IMusicLoaderType> getMusicLoaderTypes() {
        return LOADER_TYPES;
    }
}
