package dev.felnull.imp.client.music.player;

import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import dev.felnull.imp.client.lava.LavaPlayerManager;
import dev.felnull.imp.client.music.AudioInfo;
import dev.felnull.imp.client.music.MusicEngine;
import dev.felnull.imp.music.resource.MusicSource;

import javax.sound.sampled.AudioInputStream;
import java.util.UUID;

public class LavaMusicPlayer extends BaseMusicPlayer {
    private final AudioTrack audioTrack;
    private AudioPlayer audioPlayer;

    public LavaMusicPlayer(UUID musicPlayerId, AudioTrack audioTrack, MusicSource musicSource) {
        super(musicPlayerId, new AudioInfo(LavaPlayerManager.getInstance().getChannel(), LavaPlayerManager.getInstance().getSampleRate(), LavaPlayerManager.getInstance().getBit()), musicSource, audioTrack.getInfo().isStream ? 5 : 16);
        this.audioTrack = audioTrack;
    }

    @Override
    protected AudioInputStream openAudioStream(long position) throws Exception {
        var lm = LavaPlayerManager.getInstance();

        this.audioPlayer = lm.getAudioPlayerManager().createPlayer();
        this.audioTrack.setPosition(position);
        this.audioPlayer.addListener(new AudioEventAdapter() {
            @Override
            public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                stopReadStream();
            }

            @Override
            public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
                MusicEngine.getInstance().getLogger().error("Audio track error: " + track.getInfo().identifier, exception);
                stopReadStream();
            }

            @Override
            public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
                stopReadStream();
            }
        });

        var stream = AudioPlayerInputStream.createStream(audioPlayer, lm.getAudioDataFormat(), 1000 * 3, true);
        this.audioPlayer.playTrack(this.audioTrack);
        return stream;
    }

    @Override
    protected void closeAudioStream() throws Exception {
        if (this.audioPlayer != null)
            this.audioPlayer.destroy();
    }
}
