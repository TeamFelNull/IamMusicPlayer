package red.felnull.imp.client.data;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import red.felnull.imp.IamMusicPlayer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;

public class YoutubeData {

    private static String youtubeAPIKey;
    private static YouTube youtube;

    public static void init() {
        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {
        }).setApplicationName("youtube-cmdline-search-" + IamMusicPlayer.MODID).build();
        try {
            FileReader re = new FileReader(Paths.get("V:\\dev\\minecraft\\IamMusicPlayer\\youtubekey.txt").toFile());
            BufferedReader bre = new BufferedReader(re);
            String st;
            while ((st = bre.readLine()) != null) {
                try {
                    youtubeAPIKey = st;
                } catch (Exception exa) {
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static YouTube getYoutube() {
        return youtube;
    }

    public static String getYoutubeAPIKey() {
        return youtubeAPIKey;
    }
}
