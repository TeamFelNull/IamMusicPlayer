package red.felnull.imp.client.music.player;

import com.github.kiulian.downloader.YoutubeException;
import red.felnull.imp.client.util.YoutubeUtils;

import java.io.IOException;
import java.net.URL;

public class YoutubeMusicPlayer extends URLNotStreamMusicPlayer {

    private final String videoID;

    public YoutubeMusicPlayer(long rery, String videoID, LavaMusicLoader loader) throws IOException, YoutubeException {
        super(rery, new URL(YoutubeUtils.getYoutubeMa4DirectLink(videoID)), loader);
        this.videoID = videoID;
    }

    @Override
    public Object getMusicSource() {
        return videoID;
    }

}
