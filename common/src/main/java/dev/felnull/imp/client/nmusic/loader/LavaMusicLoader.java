package dev.felnull.imp.client.nmusic.loader;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.client.lava.LavaPlayerManager;
import dev.felnull.imp.client.nmusic.player.LavaMusicPlayer;
import dev.felnull.imp.client.nmusic.player.MusicPlayer;
import dev.felnull.imp.music.resource.MusicSource;
import org.jetbrains.annotations.NotNull;

public class LavaMusicLoader implements MusicLoader {
    private MusicSource musicSource;
    private AudioTrack audioTrack;

    @Override
    public @NotNull MusicPlayer<?, ?> createMusicPlayer() {
        return new LavaMusicPlayer(audioTrack, musicSource);
    }

    @Override
    public void tryLoad(@NotNull MusicSource source) throws Exception {
        var track = LavaPlayerManager.getInstance().loadTrack(source.getIdentifier());
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
