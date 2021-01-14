package red.felnull.imp.client.util;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import red.felnull.imp.client.data.YoutubeData;

import java.util.*;

public class YoutubeUtils {

    public static final Map<String, String> YOUTUBE_THUMBNAILURL = new HashMap<>();

    public static List<AudioTrack> getVideoSearchResults(String searchText) {
        List<AudioTrack> list = new ArrayList<>();
        YoutubeData.getAudioPlayerManager().loadItemOrdered(UUID.randomUUID(), "ytsearch:" + searchText, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                list.addAll(playlist.getTracks());
            }

            @Override
            public void noMatches() {
                list.add(null);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                list.add(null);
            }
        });
        long ft = System.currentTimeMillis();
        while (list.isEmpty()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - ft > 30000)
                return new ArrayList<>();
        }

        if (list.get(0) == null)
            return new ArrayList<>();

        return list;
    }

    public static String getThumbnailURL(String videoID) {
        return String.format("https://i.ytimg.com/vi/%s/hqdefault.jpg", videoID);
    }

    public static boolean isYoutubeURL(String url) {

        return url.contains("youtube") && url.contains("=");
    }

    public static String getYoutubeIDFromURL(String url) {
        String[] ur = url.split("=");
        return ur[ur.length - 1];
    }

    public static String getYoutubeMa4DirectLink(String videoID) throws YoutubeException {
        YoutubeDownloader yd = new YoutubeDownloader();
        YoutubeVideo yv = yd.getVideo(videoID);
        AudioFormat audioformat = yv.audioFormats().get(0);
        return audioformat.url();
    }
}
