package red.felnull.imp.client.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import red.felnull.imp.client.data.YoutubeData;
import red.felnull.otyacraftengine.util.IKSGURLUtil;

import java.io.IOException;
import java.util.*;

public class YoutubeUtils {

    public static final Map<String, String> YOUTUBE_VIDEOINFO = new HashMap<>();
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
        String info = getYoutubeInfo(videoID);
        if (info != null && !info.isEmpty()) {
            JsonObject jsonobject = new Gson().fromJson(info, JsonObject.class);
            String url = jsonobject.get("thumbnail_url").getAsString();
            YOUTUBE_THUMBNAILURL.put(videoID, url);
            return url;
        }
        return null;
    }

    public static String getYoutubeInfo(String videoID) {

        if (YOUTUBE_VIDEOINFO.containsKey(videoID))
            return YOUTUBE_VIDEOINFO.get(videoID);

        YoutubeInfoThread yit = new YoutubeInfoThread(videoID);
        yit.start();

        YOUTUBE_VIDEOINFO.put(videoID, "");
        return "";
    }

    public static class YoutubeInfoThread extends Thread {
        private final String videoID;

        private YoutubeInfoThread(String videoID) {
            this.videoID = videoID;
        }

        @Override
        public void run() {
            try {
                String url = "https://noembed.com/embed?url=https://www.youtube.com/watch?v=" + videoID;
                String rp = IKSGURLUtil.getURLResponse(url);
                YOUTUBE_VIDEOINFO.put(videoID, rp);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                YOUTUBE_VIDEOINFO.put(videoID, null);
                return;
            }
        }
    }
}
