package red.felnull.imp.client.music;

import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import red.felnull.imp.util.MusicUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
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
        try {
            int frame = (int) (startMiliSecond / frameSecond);
            if (player == null) {
                this.player = new AdvancedPlayer(new BufferedInputStream(inputURL.openStream()));
                MusicPlayThread playThread = new MusicPlayThread(this, frame);
                playThread.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.player = null;
        }
    }

    @Override
    public void stop() {
        if (player != null) {
            player.close();
        }
    }


    @Override
    public boolean isPlaying() {
        return player != null;
    }

    @Override
    public long cureentElapsedTime() {
        if (player == null)
            return 0;

        return System.currentTimeMillis() - startPlayTime;
    }

    private class MusicPlayThread extends Thread {
        private final URLNotStreamMusicPlayer Fplayer;
        private final int startMiliSecond;

        public MusicPlayThread(URLNotStreamMusicPlayer Fplayer, int startMiliSecond) {
            this.Fplayer = Fplayer;
            this.startMiliSecond = startMiliSecond;
        }

        @Override
        public void run() {
            try {
                Fplayer.startPlayTime = System.currentTimeMillis();
                Fplayer.player.play(startMiliSecond, Integer.MAX_VALUE);
                Fplayer.player = null;
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        }
    }
}
