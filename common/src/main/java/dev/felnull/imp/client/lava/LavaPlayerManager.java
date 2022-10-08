package dev.felnull.imp.client.lava;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.Pcm16AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.client.nmusic.media.IMPMusicMedias;
import dev.felnull.imp.client.nmusic.media.LavaPlayerBaseMusicMedia;
import dev.felnull.imp.client.nmusic.media.MusicMedia;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class LavaPlayerManager {
    private static final LavaPlayerManager INSTANCE = new LavaPlayerManager();
    private static final AudioDataFormat COMMON_PCM_S16_LE_C2 = new Pcm16AudioDataFormat(1, 48000, 960, false);
    private AudioPlayerManager audioPlayerManager;

    public static LavaPlayerManager getInstance() {
        return INSTANCE;
    }

    public void reload() {
        if (audioPlayerManager != null)
            audioPlayerManager.shutdown();
        audioPlayerManager = createAudioPlayerManager();
    }

    public int getChannel() {
        return COMMON_PCM_S16_LE_C2.channelCount;
    }

    public int getSampleRate() {
        return COMMON_PCM_S16_LE_C2.sampleRate;
    }

    public int getBit() {
        return 16;
    }

    public AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }

    private AudioPlayerManager createAudioPlayerManager() {
        AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

        audioPlayerManager.setFrameBufferDuration(1000);
        audioPlayerManager.setPlayerCleanupThreshold(Long.MAX_VALUE);

        audioPlayerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        audioPlayerManager.getConfiguration().setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
        audioPlayerManager.getConfiguration().setOutputFormat(getAudioDataFormat());

        registerSourceManager(audioPlayerManager);

        return audioPlayerManager;
    }


    private void registerSourceManager(AudioPlayerManager audioPlayerManager) {
        for (MusicMedia value : IMPMusicMedias.getAllMedia().values()) {
            if (value instanceof LavaPlayerBaseMusicMedia lavaPlayerBaseMusicMedia)
                lavaPlayerBaseMusicMedia.registerSourceManager(audioPlayerManager);
        }
    }

    public Optional<AudioTrack> loadTrack(String identifier) throws ExecutionException, InterruptedException {
        AtomicReference<AudioTrack> audioTrack = new AtomicReference<>();
        AtomicReference<FriendlyException> fe = new AtomicReference<>();
        audioPlayerManager.loadItem(identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                audioTrack.set(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
                    track.stop();
                }
            }

            @Override
            public void noMatches() {
            }

            @Override
            public void loadFailed(FriendlyException ex) {
                fe.set(ex);
            }
        }).get();
        if (fe.get() != null)
            throw fe.get();
        return Optional.ofNullable(audioTrack.get());
    }

    public AudioDataFormat getAudioDataFormat() {
        return COMMON_PCM_S16_LE_C2;
    }
}
