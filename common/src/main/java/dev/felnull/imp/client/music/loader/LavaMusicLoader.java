package dev.felnull.imp.client.music.loader;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.client.lava.LavaPlayerManager;
import dev.felnull.imp.client.music.player.LavaMusicPlayer;
import dev.felnull.imp.client.music.player.MusicPlayer;
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
        if (!isSupportMedia(source))
            throw new RuntimeException("Unsupported media");

        var lm = LavaPlayerManager.getInstance();

        var wr = wrappedIdentifier(source);
        if (wr == null)
            throw new RuntimeException("Failed to get wrapped identifier");

        var track = lm.loadTrack(wr);
        if (track.isEmpty())
            throw new RuntimeException("Failed to load track");

        if (source.isLive() != track.get().getInfo().isStream)
            throw new RuntimeException("Discrepancies in live information");

        this.audioTrack = track.get();
        this.musicSource = source;
    }

    protected boolean isSupportMedia(MusicSource source) {
        if (source.isLive() && source.getLoaderType().isEmpty())
            return true;

        var lm = LavaPlayerManager.getInstance();
        return lm.getMedias().containsKey(source.getLoaderType());
    }

    protected String wrappedIdentifier(MusicSource source) throws Exception {
        return source.getIdentifier();
    }

    @Override
    public int priority() {
        return 0;
    }
}
