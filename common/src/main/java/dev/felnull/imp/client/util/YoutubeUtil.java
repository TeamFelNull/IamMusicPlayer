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

public class YoutubeUtil {
    private static final YoutubeDownloader youtubeDownloader = new YoutubeDownloader();

    public static String getYoutubeRawURL(String videoID) {
        var video = youtubeDownloader.getVideoInfo(new RequestVideoInfo(videoID)).data();
        if (video.details().isLive())
            return null;
        return video.audioFormats().stream().filter(n -> n.extension() == Extension.WEBA).min(Comparator.comparingInt(AudioFormat::averageBitrate)).map(Format::url).orElse(null);
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
}
