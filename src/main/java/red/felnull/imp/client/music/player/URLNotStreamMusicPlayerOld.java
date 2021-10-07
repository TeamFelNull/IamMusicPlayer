package red.felnull.imp.client.music.player;

/*
public class URLNotStreamMusicPlayerOld implements IMusicPlayer {
    private static final long oneCovCutTime = 60 * 1000;

    private final URL inputURL;
    private final long duration;
    private final InputStreamArrayEnumeration streamEnumeration;

    private AdvancedPlayer player;
    private long startPlayTime;
    private long startPosition;
    private int cont;
    private boolean stop;
    private boolean isReady;
    private boolean nextCovFlag;
    private long readyTime;
    private float frameSecond;

    public URLNotStreamMusicPlayerOld(long rery, URL url) throws IOException, BitstreamException, IMPFFmpegException {
        this.readyTime = rery;
        // MultimediaObject mo = FFmpegUtils.createMultimediaObject(url);
        this.duration = MusicUtils.getMillisecondDuration(url);//FFmpegUtils.getInfo(mo).getDuration();
        this.inputURL = url;
        this.streamEnumeration = new InputStreamArrayEnumeration();
    }

    @Override
    public void ready(long startMiliSecond) {
        try {
            this.startPosition = startMiliSecond;
            if (!this.isReady && player == null) {
                this.cont = 0;
                this.streamEnumeration.clear();
                String fristname = UUID.randomUUID().toString();
                nextCovFlag = duration - startMiliSecond > 60;
                converting(inputURL, PathUtils.getIMPTmpFolder().resolve(fristname), startMiliSecond, nextCovFlag ? oneCovCutTime : 0);
                if (stop)
                    return;
                frameSecond = MusicUtils.getMP3MillisecondPerFrame(PathUtils.getIMPTmpFolder().resolve(fristname).toFile());
                if (stop)
                    return;
                streamEnumeration.add(new FileInputStream(PathUtils.getIMPTmpFolder().resolve(fristname).toFile()));
                cont++;
                this.player = new AdvancedPlayer(new SequenceInputStream(streamEnumeration));
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
    public void playMisalignment(long zure) {
        try {
            this.stop = false;
            if (this.isReady && player != null) {
                this.startPosition += zure;
                if (duration <= startPosition) {
                    this.player = null;
                    this.stop = true;
                    return;
                }
                int startFrame = (int) ((float) zure / frameSecond);
                MusicPlayThread playThread = new MusicPlayThread(startFrame);
                playThread.start();
                if (nextCovFlag) {
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
    public long getMaxMisalignment() {
        return oneCovCutTime;
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
        private final int startMiliFrame;

        public MusicPlayThread(int startMiliFrame) {
            this.startMiliFrame = startMiliFrame;
        }

        @Override
        public void run() {
            try {
                if (!stop) {
                    startPlayTime = System.currentTimeMillis();
                    player.play(startMiliFrame, Integer.MAX_VALUE);
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
                converting(inputURL, PathUtils.getIMPTmpFolder().resolve(name), off, nextFlag ? oneCovCutTime : 0);
                if (!isPlaying() || stop)
                    return;
                streamEnumeration.add(new FileInputStream(PathUtils.getIMPTmpFolder().resolve(name).toFile()));
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
            // FFmpegUtils.encode(FFmpegUtils.createMultimediaObject(url), outPath.toFile(), "libmp3lame", 128, ClientWorldMusicManager.instance().isStereoEnabled() ? 2 : 1, 44100, "mp3", offset, duration);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}*/
