package red.felnull.imp.lava;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class LavaPlayerPort {
    private static AudioPlayerManager localAudioPlayerManager;
    private static AudioPlayerManager urlAudioPlayerManager;
    private static AudioPlayerManager youtubeAudioPlayerManager;

    public static void init() {
        localAudioPlayerManager = new DefaultAudioPlayerManager();
        managerInit(localAudioPlayerManager);
        localAudioPlayerManager.registerSourceManager(new LocalAudioSourceManager());

        urlAudioPlayerManager = new DefaultAudioPlayerManager();
        managerInit(urlAudioPlayerManager);
        urlAudioPlayerManager.registerSourceManager(new HttpAudioSourceManager());

        youtubeAudioPlayerManager = new DefaultAudioPlayerManager();
        managerInit(youtubeAudioPlayerManager);
        youtubeAudioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager());
    }

    private static void managerInit(AudioPlayerManager audioPlayerManager) {
        audioPlayerManager.setFrameBufferDuration(1000);
        audioPlayerManager.setPlayerCleanupThreshold(Long.MAX_VALUE);
        audioPlayerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        audioPlayerManager.getConfiguration().setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
    }

    public static boolean isSupport(String url) {
        AudioTrack track = search(urlAudioPlayerManager, url);
        return track != null && !track.getInfo().isStream;
    }

    public static long getDurationByYoutube(String videoID) {
        AudioTrack track = search(youtubeAudioPlayerManager, videoID);
        if (track == null)
            return 0;
        return track.getInfo().length;
    }

    public static long getDuration(String url) {
        AudioTrack track = search(urlAudioPlayerManager, url);
        if (track == null)
            return 0;
        return track.getInfo().length;
    }

    public static boolean isSupport(File file) {
        AudioTrack track = search(localAudioPlayerManager, file.toString());
        return track != null && !track.getInfo().isStream;
    }

    public static long getDuration(File file) {
        AudioTrack track = search(localAudioPlayerManager, file.toString());
        if (track == null)
            return 0;
        return track.getInfo().length;
    }

    public static String getFormat(String url) {
        return "";
    }

    public static String getFormat(File file) {
        return "";
    }

    private static AudioTrack search(AudioPlayerManager manager, String identifier) {
        AtomicBoolean ff = new AtomicBoolean(false);
        AtomicReference<AudioTrack> data = new AtomicReference<>(null);
        manager.loadItemOrdered(UUID.randomUUID(), identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                if (!track.getInfo().isStream)
                    data.set(track);
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

}
