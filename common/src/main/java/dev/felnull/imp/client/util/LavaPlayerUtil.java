package dev.felnull.imp.client.util;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class LavaPlayerUtil {
    private static final Map<CashEntry, AudioTrack> TRACK_CASH = new HashMap<>();

    public static Optional<AudioTrack> loadCashedTrack(String loadType, AudioPlayerManager audioPlayerManager, String identifier, boolean remove) throws ExecutionException, InterruptedException {
        CashEntry ce = new CashEntry(loadType, identifier);
        var track = TRACK_CASH.get(ce);
        if (track != null) {
            if (remove)
                TRACK_CASH.put(ce, TRACK_CASH.get(ce).makeClone());
            return Optional.of(track);
        }
        var lt = loadTrack(audioPlayerManager, identifier);
        lt.ifPresent(n -> TRACK_CASH.put(ce, n));
        return lt;
    }

    public static Optional<AudioTrack> loadTrackNonThrow(AudioPlayerManager audioPlayerManager, String identifier) {
        try {
            return loadTrack(audioPlayerManager, identifier);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<AudioTrack> loadTrack(AudioPlayerManager audioPlayerManager, String identifier) throws ExecutionException, InterruptedException {
        AtomicReference<AudioTrack> audioTrack = new AtomicReference<>();
        AtomicReference<FriendlyException> fe = new AtomicReference<>();
        audioPlayerManager.loadItem(identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                audioTrack.set(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
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

    public static AudioPlayerManager createAudioPlayerManager() {
        AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
        audioPlayerManager.setFrameBufferDuration(1000);
        audioPlayerManager.setPlayerCleanupThreshold(Long.MAX_VALUE);
        audioPlayerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        audioPlayerManager.getConfiguration().setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
        return audioPlayerManager;
    }

    private static record CashEntry(String loadType, String str) {
    }

}
