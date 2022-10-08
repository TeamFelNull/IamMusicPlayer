package dev.felnull.imp.client.nmusic.player;

import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.client.lava.LavaPlayerManager;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.AudioInputStream;

public class LavaMusicPlayer extends BaseMusicPlayer {
    @NotNull
    private final AudioTrack audioTrack;
    private AudioPlayer audioPlayer;

    public LavaMusicPlayer(@NotNull AudioTrack audioTrack) {
        super(LavaPlayerManager.getInstance().getChannel(), LavaPlayerManager.getInstance().getSampleRate(), LavaPlayerManager.getInstance().getBit());
        this.audioTrack = audioTrack;
    }

    @Override
    protected AudioInputStream loadAudioStream(long position) throws Exception {
        var lm = LavaPlayerManager.getInstance();

        this.audioPlayer = lm.getAudioPlayerManager().createPlayer();
        this.audioTrack.setPosition(position);
        var stream = AudioPlayerInputStream.createStream(audioPlayer, lm.getAudioDataFormat(), lm.getAudioDataFormat().frameDuration(), false);
        this.audioPlayer.startTrack(this.audioTrack, false);
        return stream;
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
        this.audioPlayer.destroy();
        this.audioTrack.stop();
    }

    @Override
    public void tick() {
        super.tick();
    }
}
