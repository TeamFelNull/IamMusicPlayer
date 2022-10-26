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
import dev.felnull.imp.client.music.media.IMPMusicMedias;
import dev.felnull.imp.client.music.media.LavaPlayerBaseMusicMedia;
import dev.felnull.imp.client.music.media.MusicMedia;
import dev.felnull.imp.client.music.media.MusicMediaResult;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public class LavaPlayerManager {
    private static final LavaPlayerManager INSTANCE = new LavaPlayerManager();
    private final Map<String, LavaPlayerBaseMusicMedia> medias = new ConcurrentHashMap<>();
    private AudioDataFormat audioDataFormat;
    private AudioPlayerManager audioPlayerManager;

    public static LavaPlayerManager getInstance() {
        return INSTANCE;
    }

    public void reload() {
        medias.clear();
        if (audioPlayerManager != null)
            audioPlayerManager.shutdown();

        audioDataFormat = new Pcm16AudioDataFormat(2, 48000, 960, false);

        audioPlayerManager = createAudioPlayerManager();
    }

    public int getChannel() {
        return getAudioDataFormat().channelCount;
    }

    public int getSampleRate() {
        return getAudioDataFormat().sampleRate;
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
        for (Map.Entry<String, MusicMedia> entry : IMPMusicMedias.getAllMedia().entrySet()) {
            var media = entry.getValue();
            if (media instanceof LavaPlayerBaseMusicMedia lavaPlayerBaseMusicMedia) {
                lavaPlayerBaseMusicMedia.registerSourceManager(audioPlayerManager);
                medias.put(entry.getKey(), lavaPlayerBaseMusicMedia);
            }
        }
    }

    public Map<String, LavaPlayerBaseMusicMedia> getMedias() {
        return medias;
    }

    public Optional<AudioTrack> loadTrack(String identifier) throws ExecutionException, InterruptedException, TimeoutException {
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
        }).get(10, TimeUnit.SECONDS);
        if (fe.get() != null)
            throw fe.get();
        return Optional.ofNullable(audioTrack.get());
    }

    public AudioDataFormat getAudioDataFormat() {
        return audioDataFormat;
    }

    @Nullable
    public Pair<MusicMedia, MusicMediaResult> autoLoad(String sourceName) throws ExecutionException, InterruptedException, TimeoutException {
        var lpm = LavaPlayerManager.getInstance();
        var track = lpm.loadTrack(sourceName);
        if (track.isEmpty() || track.get().getInfo().isStream)
            return null;

        for (LavaPlayerBaseMusicMedia value : medias.values()) {
            if (value.match(track.get()))
                return Pair.of(value, value.createResult(track.get()));
        }
        return null;
    }
}
