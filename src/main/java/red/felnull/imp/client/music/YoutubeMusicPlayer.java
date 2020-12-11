package red.felnull.imp.client.music;

import com.github.kiulian.downloader.YoutubeException;
import javazoom.jl.decoder.BitstreamException;
import red.felnull.imp.client.util.YoutubeUtils;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.net.URL;

public class YoutubeMusicPlayer extends URLNotStreamMusicPlayer {

    private final String videoID;

    public YoutubeMusicPlayer(String videoID) throws IOException, BitstreamException, EncoderException, YoutubeException {
        super(new URL(YoutubeUtils.getYoutubeMa4DirectLink(videoID)));
        this.videoID = videoID;
    }

    @Override
    public Object getMusicSource() {
        return videoID;
    }

}
