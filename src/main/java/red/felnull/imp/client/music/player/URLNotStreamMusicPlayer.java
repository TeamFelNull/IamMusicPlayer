package red.felnull.imp.client.music.player;

import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import red.felnull.imp.client.music.InputStreamArrayEnumeration;
import red.felnull.imp.util.FFmpegUtils;
import red.felnull.imp.util.MusicUtils;
import red.felnull.imp.util.PathUtils;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.UUID;

public class URLNotStreamMusicPlayer implements IMusicPlayer {
    private static final long oneCovCutTime = 60 * 1000;

    private final URL inputURL;
    private final float frameSecond;
    private final boolean isDirectly;
    private final long duration;
    private final InputStreamArrayEnumeration streamEnumeration;

    private AdvancedPlayer player;
    private long startPlayTime;
    private long startPosition;
    private int cont;
    private boolean stop;
    private int startFrame;
    private boolean isReady;
    private boolean nextCovFlag;

    public URLNotStreamMusicPlayer(URL url) throws IOException, BitstreamException, EncoderException {
        MultimediaObject mo = FFmpegUtils.createMultimediaObject(url);
        this.isDirectly = mo.getInfo().getFormat().equals("mp3");
        this.frameSecond = isDirectly ? MusicUtils.getMP3MillisecondPerFrame(url.openStream()) : 0;
        this.duration = !isDirectly ? mo.getInfo().getDuration() : 0;
        this.inputURL = url;
        this.streamEnumeration = new InputStreamArrayEnumeration();
    }

    @Override
    public void ready(long startMiliSecond) {
        try {
            this.startPosition = startMiliSecond;
            if (!this.isReady && player == null) {
                if (isDirectly) {
                    this.startFrame = (int) (startMiliSecond / frameSecond);
                    this.player = new AdvancedPlayer(inputURL.openStream());
                } else {
                    this.cont = 0;
                    this.streamEnumeration.clear();
                    String fristname = UUID.randomUUID().toString();
                    nextCovFlag = duration - startMiliSecond > 60;
                    converting(inputURL, PathUtils.getClientTmpFolder().resolve(fristname), startMiliSecond, nextCovFlag ? oneCovCutTime : 0);
                    if (stop)
                        return;
                    streamEnumeration.add(new FileInputStream(PathUtils.getClientTmpFolder().resolve(fristname).toFile()));
                    cont++;
                    this.player = new AdvancedPlayer(new SequenceInputStream(streamEnumeration));
                }
                this.isReady = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.player = null;
            this.isReady = false;
            this.streamEnumeration.clear();
        }
    }

    @Override
    public void play() {
        try {
            this.stop = false;
            if (this.isReady && player != null) {
                MusicPlayThread playThread = new MusicPlayThread(startFrame);
                playThread.start();
                if (!isDirectly && nextCovFlag) {
                    MusicConversionThread conversionThread = new MusicConversionThread(startPosition);
                    conversionThread.start();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.player = null;
            this.stop = true;
        }
    }

    @Override
    public void playAndReady(long startMiliSecond) {
        ready(startMiliSecond);
        play();
    }

    @Override
    public void stop() {
        stop = true;
        if (player != null) {
            player.close();
        }
    }


    @Override
    public boolean isPlaying() {
        return player != null;
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
        return inputURL;
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

        public MusicPlayThread() {
            this(0);
        }

        @Override
        public void run() {
            try {
                if (!stop) {
                    startPlayTime = System.currentTimeMillis();
                    player.play(startMiliSecond, Integer.MAX_VALUE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                player = null;
                streamEnumeration.clear();
            }
        }
    }

    private class MusicConversionThread extends Thread {
        private final long startMiliSecond;

        public MusicConversionThread(long startMiliSecond) {
            this.startMiliSecond = startMiliSecond;
        }

        @Override
        public void run() {
            try {
                String name = UUID.randomUUID().toString();
                long off = oneCovCutTime * cont;
                long alltime = duration - startMiliSecond - oneCovCutTime * cont;
                long wait = off - oneCovCutTime / 2 - getCureentElapsed();
                if (wait >= 0)
                    sleep(wait);
                if (!isPlaying() || stop)
                    return;
                cont++;
                boolean nextFlag = alltime - oneCovCutTime * cont > 60;
                converting(inputURL, PathUtils.getClientTmpFolder().resolve(name), off, nextFlag ? oneCovCutTime : 0);
                if (!isPlaying() || stop)
                    return;
                streamEnumeration.add(new FileInputStream(PathUtils.getClientTmpFolder().resolve(name).toFile()));
                if (nextFlag && !stop) {
                    MusicConversionThread conversionThread = new MusicConversionThread(startMiliSecond);
                    conversionThread.start();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void converting(URL url, Path outPath, long offset, long duration) {
        try {
            float offsetF = (float) offset / 1000f;
            float durationF = (float) duration / 1000f;

            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setBitRate(128000);
            audio.setChannels(1);
            audio.setSamplingRate(44100);

            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("mp3");
            attrs.setAudioAttributes(audio);
            if (offset != 0)
                attrs.setOffset(offsetF);
            if (duration != 0)
                attrs.setDuration(durationF);

            Encoder encoder = new Encoder();
            encoder.encode(FFmpegUtils.createMultimediaObject(url), outPath.toFile(), attrs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
