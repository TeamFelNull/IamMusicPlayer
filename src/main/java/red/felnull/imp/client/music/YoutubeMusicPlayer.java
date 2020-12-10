package red.felnull.imp.client.music;

import com.github.kiulian.downloader.YoutubeException;
import javazoom.jl.decoder.BitstreamException;
import red.felnull.imp.client.util.YoutubeUtils;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.net.URL;

public class YoutubeMusicPlayer extends URLNotStreamMusicPlayer {

    public YoutubeMusicPlayer(String videoID) throws IOException, BitstreamException, EncoderException, YoutubeException {
        super(new URL(YoutubeUtils.getYoutubeMa4DirectLink(videoID)));
    }
}
