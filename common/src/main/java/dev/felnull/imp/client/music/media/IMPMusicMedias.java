package dev.felnull.imp.client.music.media;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class IMPMusicMedias {
    protected static final Map<String, MusicMedia> MEDIAS = new HashMap<>();
    public static final YoutubeMusicMedia YOUTUBE = new YoutubeMusicMedia("youtube");
    public static final SoundCloudMusicMedia SOUNDCLOUD = new SoundCloudMusicMedia("soundcloud");
    public static final HttpMusicMedia HTTP = new HttpMusicMedia("http");

    public static void init() {
        register("youtube", YOUTUBE);
        register("soundcloud", SOUNDCLOUD);
        register("http", HTTP);
    }

    public static void register(String name, MusicMedia media) {
        MEDIAS.put(name, media);
    }

    public static MusicMedia getMedia(String name) {
        synchronized (MEDIAS) {
            return MEDIAS.get(name);
        }
    }

    public static Map<String, MusicMedia> getAllMedia() {
        synchronized (MEDIAS) {
            return ImmutableMap.copyOf(MEDIAS);
        }
    }
}
