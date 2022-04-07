package dev.felnull.imp.client.music.loadertypes;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class IMPMusicLoaderTypes {
    private static final Map<String, IMusicLoaderType> LOADER_TYPES = new LinkedHashMap<>();
    public static AudioPlayerManager allAudioPlayerManager;
    public static final String YOUTUBE = "youtube";
    public static final String SOUNDCLOUD = "soundcloud";
    public static final String HTTP = "http";
    //  public static final String NICONICO = "niconico";

    public static void init() {
        registerLoaderType(YOUTUBE, new YoutubeMusicLoaderType());
        registerLoaderType(SOUNDCLOUD, new SoundCloudMusicLoaderType());
        registerLoaderType(HTTP, new HttpURLMusicLoaderType());
        //   registerLoaderType(NICONICO, new NicoNicoMusicLoaderType());
    }

    public static IMusicLoaderType getLoaderType(String name) {
        return LOADER_TYPES.get(name);
    }

    public static void registerLoaderType(String name, IMusicLoaderType loaderType) {
        LOADER_TYPES.put(name, loaderType);
        if (allAudioPlayerManager == null)
            allAudioPlayerManager = LavaPlayerUtil.createAudioPlayerManager();
        if (loaderType instanceof AbstractLavaPlayerMusicLoaderType lLoaderType)
            lLoaderType.registerSourceManager(allAudioPlayerManager);
    }

    public static Map<String, IMusicLoaderType> getMusicLoaderTypes() {
        return LOADER_TYPES;
    }

    public static Pair<String, MusicLoadResult> loadAuto(String sourceName) throws InterruptedException {
        try {
            var otrack = LavaPlayerUtil.loadTrack(allAudioPlayerManager, sourceName);
            if (otrack.isPresent() && !otrack.get().getInfo().isStream) {
                var track = otrack.get();
                for (Map.Entry<String, IMusicLoaderType> entry : IMPMusicLoaderTypes.getMusicLoaderTypes().entrySet()) {
                    if (entry.getValue() instanceof AbstractLavaPlayerMusicLoaderType lLoaderType) {
                        if (lLoaderType.match(track))
                            return Pair.of(entry.getKey(), lLoaderType.createResult(track));
                    }
                }
            }
        } catch (ExecutionException ignored) {
        }
        return null;
    }
}
