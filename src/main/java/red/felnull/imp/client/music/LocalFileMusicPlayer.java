package red.felnull.imp.client.music;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import red.felnull.imp.util.MusicUtils;

import java.io.*;

public class LocalFileMusicPlayer implements IMusicPlayer {
    private AdvancedPlayer player;
    private long startPlayTime;
    private long startPosition;

    private final File inputFile;
    private final float frameSecond;

    public LocalFileMusicPlayer(File file) throws IOException, InvalidDataException, UnsupportedTagException {

        if (!file.exists())
            throw new FileNotFoundException();

        this.frameSecond = MusicUtils.getMP3MillisecondPerFrame(file);
        this.inputFile = file;
    }

    @Override
    public void play(long startMiliSecond) {
        try {
            int frame = (int) (startMiliSecond / frameSecond);
            if (player == null) {
                this.startPosition = startMiliSecond;
                this.player = new AdvancedPlayer(new FileInputStream(inputFile));
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

        return System.currentTimeMillis() - startPlayTime + startPosition;
    }

    private class MusicPlayThread extends Thread {
        private final LocalFileMusicPlayer Fplayer;
        private final int startMiliSecond;

        public MusicPlayThread(LocalFileMusicPlayer Fplayer, int startMiliSecond) {
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
