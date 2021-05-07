package red.felnull.imp.client.music.loader;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.Pcm16AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.client.music.player.LavaMusicPlayer;
import red.felnull.imp.music.resource.MusicLocation;
import red.felnull.imp.throwable.InvalidIdentifierException;

import java.util.Arrays;

public class LavaPlayerLoader implements IMusicPlayerLoader {
    private static final Logger LOGGER = LogManager.getLogger(LavaPlayerLoader.class);
    public static final AudioDataFormat COMMON_PCM_S16_LE_C1 = new Pcm16AudioDataFormat(1, 48000, 960, false);
    private final AudioSourceManager[] sourceManagers;
    private final String name;
    private final String testIdentifier;
    private AudioPlayerManager audioPlayerManager;
    private AudioDataFormat dataformat;

    public LavaPlayerLoader(String name, String testIdentifier, AudioSourceManager... sourceManagers) {
        this.sourceManagers = sourceManagers;
        this.name = name;
        this.testIdentifier = testIdentifier;
    }

    @Override
    public void init() {
        audioPlayerManager = new DefaultAudioPlayerManager();
        dataformat = COMMON_PCM_S16_LE_C1;
        audioPlayerManager.setFrameBufferDuration(1000);
        audioPlayerManager.setPlayerCleanupThreshold(Long.MAX_VALUE);
        audioPlayerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        audioPlayerManager.getConfiguration().setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
        audioPlayerManager.getConfiguration().setOutputFormat(dataformat);
        Arrays.stream(sourceManagers).forEach(n -> audioPlayerManager.registerSourceManager(n));
        LoadTestThread ltt = new LoadTestThread();
        ltt.start();
    }

    @Override
    public IMusicPlayer createMusicPlayer(MusicLocation location) {
        return new LavaMusicPlayer(location, audioPlayerManager, dataformat);
    }

    public class LoadTestThread extends Thread {
        private boolean trackLoaded;
        private Exception exception;

        public LoadTestThread() {
            setName("Lava Player Test Thread");
        }

        @Override
        public void run() {
            LOGGER.info("Start load test: " + name);
            long stTime = System.currentTimeMillis();
            audioPlayerManager.loadItem(testIdentifier, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    trackLoaded = true;
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    exception = new InvalidIdentifierException("ambiguous");
                    trackLoaded = true;
                }

                @Override
                public void noMatches() {
                    exception = new InvalidIdentifierException("nomatche");
                    trackLoaded = true;
                }

                @Override
                public void loadFailed(FriendlyException ex) {
                    exception = ex;
                    trackLoaded = true;
                }
            });
            while (!trackLoaded) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    exception = e;
                }
            }

            if (exception != null)
                LOGGER.error("Test load failed " + (System.currentTimeMillis() - stTime) + "ms" + ":" + name, exception);
            else
                LOGGER.info("Test load successful " + (System.currentTimeMillis() - stTime) + "ms" + ": " + name);
        }
    }
}
