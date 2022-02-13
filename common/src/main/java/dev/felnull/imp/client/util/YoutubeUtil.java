package dev.felnull.imp.client.util;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestPlaylistInfo;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.model.playlist.PlaylistInfo;
import com.github.kiulian.downloader.model.subtitles.SubtitlesInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YoutubeUtil {
    private static final YoutubeDownloader youtubeDownloader = new YoutubeDownloader();
    private static final Map<String, String> URL_CASH = new HashMap<>();

    public static String getCashedYoutubeRawURL(String videoID, boolean remove) {
        var ch = URL_CASH.get(videoID);
        if (ch != null) {
            if (remove)
                URL_CASH.remove(videoID);
            return ch;
        }
        var url = getYoutubeRawURL(videoID);
        if (url != null)
            URL_CASH.put(videoID, url);
        return url;
    }

    public static String getYoutubeRawURL(String videoID) {
        var video = youtubeDownloader.getVideoInfo(new RequestVideoInfo(videoID)).data();
        return video.bestAudioFormat().url();
    }

    public static List<SubtitlesInfo> getSubtitle(String videoID) {
        var video = youtubeDownloader.getVideoInfo(new RequestVideoInfo(videoID)).data();
        return video.subtitlesInfo();
    }

    public static PlaylistInfo getYoutubePlayList(String playListID) {
        return youtubeDownloader.getPlaylistInfo(new RequestPlaylistInfo(playListID)).data();
    }

}
