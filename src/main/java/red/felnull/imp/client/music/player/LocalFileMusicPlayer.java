package red.felnull.imp.client.music.player;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import red.felnull.imp.exception.IMPFFmpegException;
import red.felnull.imp.util.MusicUtils;
import ws.schild.jave.EncoderException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LocalFileMusicPlayer implements IMusicPlayer {
    private final File inputFile;
    private final float frameSecond;
    private final long duration;

    private AdvancedPlayer player;
    private long startPlayTime;
    private long startPosition;
    private int startFrame;
    private boolean isReady;
    private long readyTime;

    public LocalFileMusicPlayer(long rery, File file) throws IOException, InvalidDataException, UnsupportedTagException, EncoderException, IMPFFmpegException, BitstreamException {
        this.readyTime = rery;
        if (!file.exists())
            throw new FileNotFoundException();

        this.frameSecond = MusicUtils.getMP3MillisecondPerFrame(file);
        this.inputFile = file;
        this.duration = MusicUtils.getMillisecondDuration(file);
    }

    @Override
    public void ready(long startMiliSecond) {
        try {
            if (!this.isReady && player == null) {
                this.startFrame = (int) (startMiliSecond / frameSecond);
                this.startPosition = startMiliSecond;
                this.player = new AdvancedPlayer(new FileInputStream(inputFile));
                this.isReady = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.player = null;
            this.isReady = false;
        }
    }

    @Override
    public void playMisalignment(long zure) {
        try {
            if (this.isReady && player != null) {
                this.startFrame += (int) (zure / frameSecond);
                if (duration * frameSecond <= startFrame) {
                    this.player = null;
                    this.isReady = false;
                    return;
                }
                MusicPlayThread playThread = new MusicPlayThread(startFrame);
                playThread.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.player = null;
            this.isReady = false;
        }
    }

    @Override
    public void play() {
        playMisalignment(0);
    }

    @Override
    public void playAndReady(long startMiliSecond) {
        ready(startMiliSecond);
        play();
    }

    @Override
    public void playAutoMisalignment() {
        long zure = System.currentTimeMillis() - readyTime;
        if (getMaxMisalignment() > zure) {
            playMisalignment(zure);
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
    public long getMaxMisalignment() {
        return Integer.MAX_VALUE;
    }

    @Override
    public long getCureentElapsed() {
        if (player == null)
            return 0;

        return System.currentTimeMillis() - startPlayTime + startPosition;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public Object getMusicSource() {
        return inputFile;
    }

    @Override
    public void setVolume(float vol) {
        if (player != null)
            player.setVolume(vol);
    }

    @Override
    public float getVolume() {
        if (player != null)
            return player.getVolume();
        return 0;
    }

    private class MusicPlayThread extends Thread {
        private final int startMiliSecond;

        public MusicPlayThread(int startMiliSecond) {
            this.startMiliSecond = startMiliSecond;
        }

        @Override
        public void run() {
            try {
                startPlayTime = System.currentTimeMillis();
                player.play(startMiliSecond, Integer.MAX_VALUE);
                player = null;
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        }
    }
}
