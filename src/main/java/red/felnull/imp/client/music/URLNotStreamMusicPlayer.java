package red.felnull.imp.client.music;

import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import red.felnull.imp.util.MusicUtils;

import java.io.IOException;
import java.net.URL;

public class URLNotStreamMusicPlayer implements IMusicPlayer {
    private AdvancedPlayer player;
    private long startPlayTime;

    private final URL inputURL;
    private final float frameSecond;

    public URLNotStreamMusicPlayer(URL url) throws IOException, BitstreamException {
        this.frameSecond = MusicUtils.getMP3MillisecondPerFrame(url.openStream());
        this.inputURL = url;
    }


    @Override
    public void play(long startMiliSecond) {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public long cureentElapsedTime() {
        return 0;
    }

    private class MusicPlayThread extends Thread {


    }
}
