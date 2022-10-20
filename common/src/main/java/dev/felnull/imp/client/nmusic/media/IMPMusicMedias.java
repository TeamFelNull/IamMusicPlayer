package dev.felnull.imp.client.nmusic.media;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class IMPMusicMedias {
    protected static final Map<String, MusicMedia> MEDIAS = new HashMap<>();
    public static final MusicMedia YOUTUBE = new YoutubeMusicMedia("youtube");
    public static final MusicMedia SOUNDCLOUD = new SoundCloudMusicMedia("soundcloud");
    public static final MusicMedia HTTP = new HttpMusicMedia("http");

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
