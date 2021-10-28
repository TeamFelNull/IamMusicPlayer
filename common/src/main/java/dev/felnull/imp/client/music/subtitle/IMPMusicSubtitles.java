package dev.felnull.imp.client.music.subtitle;

import dev.felnull.imp.music.resource.MusicSource;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class IMPMusicSubtitles {
    private static final Map<String, Function<MusicSource, IMusicSubtitle>> SUBTITLES = new HashMap<>();

    public static void init() {
        registerSubtitle("youtube", YoutubeMusicSubtitle::new);
    }

    public static void registerSubtitle(String loaderType, Function<MusicSource, IMusicSubtitle> subtitle) {
        SUBTITLES.put(loaderType, subtitle);
    }

    public static IMusicSubtitle createSubtitle(String loaderType, MusicSource source) {
        if (SUBTITLES.containsKey(loaderType))
            return SUBTITLES.get(loaderType).apply(source);
        return null;
    }
}
