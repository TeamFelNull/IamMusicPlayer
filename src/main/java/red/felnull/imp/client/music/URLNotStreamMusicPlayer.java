package red.felnull.imp.client.music;

import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import red.felnull.imp.util.FFMPEGUtils;
import red.felnull.imp.util.MusicUtils;
import red.felnull.imp.util.PathUtil;
import red.felnull.otyacraftengine.util.IKSGMath;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.BufferedInputStream;
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
    private final URLStreamFileEnumeration streamEnumeration;

    private AdvancedPlayer player;
    private long startPlayTime;
    private long startPosition;
    private int cont;
    private boolean stopRQ;
    private boolean stop;

    public URLNotStreamMusicPlayer(URL url) throws IOException, BitstreamException, EncoderException {
        MultimediaObject mo = FFMPEGUtils.createMultimediaObject(url);
        this.isDirectly = mo.getInfo().getFormat().equals("mp3");
        this.frameSecond = isDirectly ? MusicUtils.getMP3MillisecondPerFrame(url.openStream()) : 0;
        this.duration = !isDirectly ? mo.getInfo().getDuration() : 0;
        this.inputURL = url;
        this.streamEnumeration = new URLStreamFileEnumeration();
    }


    @Override
    public void play(long startMiliSecond) {
        this.startPosition = startMiliSecond;
        stop = false;
        if (isDirectly) {
            try {
                int frame = (int) (startMiliSecond / frameSecond);
                if (player == null) {
                    this.player = new AdvancedPlayer(inputURL.openStream());
                    MusicPlayThread playThread = new MusicPlayThread(frame);
                    playThread.start();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                this.player = null;
            }
        } else {
            try {
                if (player == null) {
                    stopRQ = false;
                    cont = 0;
                    this.streamEnumeration.clear();
                    String fristname = UUID.randomUUID().toString();
                    boolean nextFlag = duration - startMiliSecond > 60;
                    converting(inputURL, PathUtil.getClientTmpFolder().resolve(fristname), startMiliSecond, nextFlag ? oneCovCutTime : 0);
                    if (stopRQ)
                        return;
                    streamEnumeration.add(new FileInputStream(PathUtil.getClientTmpFolder().resolve(fristname).toFile()));
                    cont++;
                    this.player = new AdvancedPlayer(new SequenceInputStream(streamEnumeration));
                    MusicPlayThread playThread = new MusicPlayThread();
                    playThread.start();
                    if (nextFlag) {
                        MusicConversionThread conversionThread = new MusicConversionThread(startMiliSecond);
                        conversionThread.start();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                this.player = null;
                this.streamEnumeration.clear();
            }
        }
    }

    @Override
    public void stop() {
        stop = true;
        if (player != null) {
            player.close();
            stopRQ = true;
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
                stop = false;
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
                if (!isPlaying())
                    return;
                cont++;
                boolean nextFlag = alltime - oneCovCutTime * cont > 60;
                converting(inputURL, PathUtil.getClientTmpFolder().resolve(name), off, nextFlag ? oneCovCutTime : 0);
                if (!isPlaying())
                    return;
                streamEnumeration.add(new FileInputStream(PathUtil.getClientTmpFolder().resolve(name).toFile()));
                if (nextFlag) {
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
            encoder.encode(FFMPEGUtils.createMultimediaObject(url), outPath.toFile(), attrs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
