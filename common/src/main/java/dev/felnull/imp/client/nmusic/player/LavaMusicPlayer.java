package dev.felnull.imp.client.nmusic.player;

import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.client.lava.LavaPlayerManager;
import dev.felnull.imp.client.nmusic.AudioInfo;
import dev.felnull.imp.music.resource.MusicSource;

import javax.sound.sampled.AudioInputStream;

public class LavaMusicPlayer extends BaseMusicPlayer {
    private final AudioTrack audioTrack;
    private AudioPlayer audioPlayer;

    public LavaMusicPlayer(AudioTrack audioTrack, MusicSource musicSource) {
        super(new AudioInfo(LavaPlayerManager.getInstance().getChannel(), LavaPlayerManager.getInstance().getSampleRate(), LavaPlayerManager.getInstance().getBit()), musicSource, audioTrack.getInfo().isStream ? 5 : 15);
        this.audioTrack = audioTrack;
    }

    @Override
    protected AudioInputStream openAudioStream(long position) throws Exception {
        var lm = LavaPlayerManager.getInstance();

        this.audioPlayer = lm.getAudioPlayerManager().createPlayer();
        this.audioTrack.setPosition(position);
        var stream = AudioPlayerInputStream.createStream(audioPlayer, lm.getAudioDataFormat(), 1000 * 15, false);
        this.audioPlayer.startTrack(this.audioTrack, false);
        return stream;
    }

    @Override
    protected void closeAudioStream() throws Exception {
        if (audioPlayer != null) {
            this.audioPlayer.stopTrack();
            this.audioPlayer.destroy();
        }

        this.audioTrack.stop();
    }
}
