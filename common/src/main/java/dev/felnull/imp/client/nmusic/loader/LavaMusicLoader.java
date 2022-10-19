package dev.felnull.imp.client.nmusic.loader;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.client.lava.LavaPlayerManager;
import dev.felnull.imp.client.nmusic.player.LavaMusicPlayer;
import dev.felnull.imp.client.nmusic.player.MusicPlayer;
import dev.felnull.imp.music.resource.MusicSource;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LavaMusicLoader implements MusicLoader {
    private MusicSource musicSource;
    private AudioTrack audioTrack;

    @Override
    public @NotNull MusicPlayer<?, ?> createMusicPlayer(UUID musicPlayerId) {
        return new LavaMusicPlayer(musicPlayerId, audioTrack, musicSource);
    }

    @Override
    public void tryLoad(@NotNull MusicSource source) throws Exception {
        var lm = LavaPlayerManager.getInstance();

        if (!lm.getMedias().containsKey(source.getLoaderType()))
            throw new RuntimeException("Unsupported media");

        var track = lm.loadTrack(source.getIdentifier());
        if (track.isEmpty())
            throw new RuntimeException("Failed to load track");

        if (source.isLive() != track.get().getInfo().isStream)
            throw new RuntimeException("Discrepancies in live information");

        this.audioTrack = track.get();
        this.musicSource = source;
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public void cansel() {
        this.audioTrack.stop();
    }
}
