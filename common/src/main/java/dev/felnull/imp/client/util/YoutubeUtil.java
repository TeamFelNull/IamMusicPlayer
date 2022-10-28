package dev.felnull.imp.client.util;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestPlaylistInfo;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.model.Extension;
import com.github.kiulian.downloader.model.playlist.PlaylistInfo;
import com.github.kiulian.downloader.model.videos.formats.AudioFormat;
import com.github.kiulian.downloader.model.videos.formats.Format;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class YoutubeUtil {
    private static final YoutubeDownloader youtubeDownloader = new YoutubeDownloader();
    private static final Map<String, URLCacheEntry> CACHE = new HashMap<>();

    @Nullable
    public static String getYoutubeRawURL(String videoID) {
        synchronized (CACHE) {
            var hit = CACHE.get(videoID);
            if (hit != null && ((hit.url != null && System.currentTimeMillis() - hit.time < 1000 * 60 * 10) || (hit.url == null && System.currentTimeMillis() - hit.time < 1000 * 60)))
                return hit.url();

            var video = youtubeDownloader.getVideoInfo(new RequestVideoInfo(videoID)).data();

            String url = null;
            if (!video.details().isLive())
                url = video.audioFormats().stream().filter(n -> n.extension() == Extension.WEBA).min(Comparator.comparingInt(AudioFormat::averageBitrate)).map(Format::url).orElse(null);

            hit = new URLCacheEntry(System.currentTimeMillis(), url);
            CACHE.put(videoID, hit);
            return hit.url();
        }
    }

    public static PlaylistInfo getYoutubePlayList(String playListID) {
        return youtubeDownloader.getPlaylistInfo(new RequestPlaylistInfo(playListID)).data();
    }

    @Nullable
    public static String getPlayListID(@NotNull String value) {
        if (value.matches("[a-zA-Z0-9-_]+")) {
            return value;
        }
        if (!value.contains("list="))
            return null;
        String[] split = value.split("list=");
        String v = split[1];
        if (!v.contains("&"))
            return v;
        return v.split("&")[0];
    }

    private static record URLCacheEntry(long time, String url) {
    }
}
