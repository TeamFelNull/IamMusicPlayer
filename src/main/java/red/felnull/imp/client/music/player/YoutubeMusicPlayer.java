package red.felnull.imp.client.music.player;

import com.github.kiulian.downloader.YoutubeException;
import javazoom.jl.decoder.BitstreamException;
import red.felnull.imp.client.util.YoutubeUtils;
import red.felnull.imp.exception.IMPFFmpegException;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.net.URL;

public class YoutubeMusicPlayer extends URLNotStreamMusicPlayer {

    private final String videoID;

    public YoutubeMusicPlayer(long rery, String videoID) throws IOException, BitstreamException, EncoderException, YoutubeException, IMPFFmpegException {
        super(rery, new URL(YoutubeUtils.getYoutubeMa4DirectLink(videoID)));
        this.videoID = videoID;
    }

    @Override
    public Object getMusicSource() {
        return videoID;
    }

}
