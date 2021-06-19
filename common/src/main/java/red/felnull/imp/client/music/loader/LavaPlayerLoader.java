package red.felnull.imp.client.music.loader;

import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.network.chat.TextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.gui.components.IMSDSmartRender;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.client.music.player.LavaALMusicPlayer;
import red.felnull.imp.client.music.player.LavaLineMusicPlayer;
import red.felnull.imp.client.music.subtitle.IMusicSubtitle;
import red.felnull.imp.client.music.subtitle.YoutubeSubtitle;
import red.felnull.imp.music.resource.MusicSource;
import red.felnull.imp.throwable.InvalidIdentifierException;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class LavaPlayerLoader implements IMusicLoader, IMSDSmartRender {
    private static final Logger LOGGER = LogManager.getLogger(LavaPlayerLoader.class);
    private static final AudioDataFormat COMMON_PCM_S16_LE_C2 = new Pcm16AudioDataFormat(2, 48000, 960, false);
    private static final AudioDataFormat COMMON_PCM_S16_BE_C2 = new Pcm16AudioDataFormat(2, 48000, 960, true);
    private final AudioSourceManager[] sourceManagers;
    private final String name;
    private final String testIdentifier;
    private AudioPlayerManager audioPlayerManager;

    public LavaPlayerLoader(String name, AudioSourceManager... sourceManagers) {
        this(name, null, sourceManagers);
    }

    public LavaPlayerLoader(String name, String testIdentifier, AudioSourceManager... sourceManagers) {
        this.sourceManagers = sourceManagers;
        this.name = name;
        this.testIdentifier = testIdentifier;
    }

    @Override
    public void init() {
        audioPlayerManager = new DefaultAudioPlayerManager();
        audioPlayerManager.setFrameBufferDuration(1000);
        audioPlayerManager.setPlayerCleanupThreshold(Long.MAX_VALUE);
        audioPlayerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        audioPlayerManager.getConfiguration().setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
        Arrays.stream(sourceManagers).forEach(n -> audioPlayerManager.registerSourceManager(n));
        if (testIdentifier != null) {
            LoadTestThread ltt = new LoadTestThread();
            ltt.start();
        }
    }

    public AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }

    @Override
    public IMusicSubtitle createMusicSubtitle(IMusicPlayer musicPlayer, MusicSource location) {
        if ("youtube".equals(name)) {
            return new YoutubeSubtitle(musicPlayer, location.getIdentifier());
        }
        return IMusicLoader.super.createMusicSubtitle(musicPlayer, location);
    }

    @Override
    public IMusicPlayer createMusicPlayer(MusicSource location) {
        switch (IamMusicPlayer.CONFIG.playSystem) {
            case OPEN_AL_SPATIAL:
                audioPlayerManager.getConfiguration().setOutputFormat(COMMON_PCM_S16_LE_C2);
                return new LavaALMusicPlayer(location, audioPlayerManager, COMMON_PCM_S16_LE_C2, true);
            case OPEN_AL_NONSPATIAL:
                audioPlayerManager.getConfiguration().setOutputFormat(COMMON_PCM_S16_LE_C2);
                return new LavaALMusicPlayer(location, audioPlayerManager, COMMON_PCM_S16_LE_C2, false);
            case JAVA_SOUND_API:
                audioPlayerManager.getConfiguration().setOutputFormat(COMMON_PCM_S16_BE_C2);
                return new LavaLineMusicPlayer(location, audioPlayerManager, COMMON_PCM_S16_BE_C2);
        }

        throw new IllegalArgumentException("No sound play system.");
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

    @Override
    public void renderIcon(PoseStack poseStack, int x, int y, int w, int h) {
        drawPrettyCenteredString(poseStack, new TextComponent(name), x + (float) w / 2f, y + (float) (h - getFont().lineHeight) / 2f, 0);
    }

    @Override
    public SearchData getPlayMusicData(String identifier) {
        AtomicBoolean ff = new AtomicBoolean(false);
        AtomicReference<SearchData> data = new AtomicReference<>(null);
        getAudioPlayerManager().loadItemOrdered(UUID.randomUUID(), identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                if (!track.getInfo().isStream)
                    data.set(toMusicSearchData(track));
                ff.set(true);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                ff.set(true);
            }

            @Override
            public void noMatches() {
                ff.set(true);
            }

            @Override
            public void loadFailed(FriendlyException ex) {
                ff.set(true);
            }
        });

        long ft = System.currentTimeMillis();

        while (!ff.get()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - ft > 30000)
                break;
        }

        return data.get();
    }

    protected SearchData toMusicSearchData(AudioTrack track) {
        return new SearchData(track.getInfo().title, null, track.getIdentifier(), null, track.getDuration(), track.getInfo().author);
    }
}
