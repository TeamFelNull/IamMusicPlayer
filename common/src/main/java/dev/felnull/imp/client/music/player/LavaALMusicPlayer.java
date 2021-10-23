package dev.felnull.imp.client.music.player;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import dev.felnull.imp.music.MusicPlaybackInfo;
import dev.felnull.imp.music.resource.MusicSource;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.openal.AL11;

public class LavaALMusicPlayer implements IMusicPlayer {
    private final MusicSource musicSource;
    private final int source;
    private final AudioPlayerManager audioPlayerManager;
    private final AudioDataFormat audioFormat;
    private final AudioPlayer audioPlayer;
    private final boolean stereo;

    public LavaALMusicPlayer(MusicSource musicSource, AudioPlayerManager audioPlayerManager, AudioDataFormat audioFormat, boolean stereo) {
        this.musicSource = musicSource;
        this.source = AL11.alGenSources();
        this.audioPlayerManager = audioPlayerManager;
        this.audioFormat = audioFormat;
        this.audioPlayer = audioPlayerManager.createPlayer();
        this.stereo = stereo;
    }

    @Override
    public void ready(long position) throws Exception {

    }

    @Override
    public void play(long delay) {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void unpause() {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void setCoordinatePosition(Vec3 vec3) {

    }

    @Override
    public Vec3 getCoordinatePosition() {
        return null;
    }

    @Override
    public void setVolume(float v) {

    }

    @Override
    public void setRange(float r) {

    }

    @Override
    public void update(MusicPlaybackInfo playbackInf) {

    }

    @Override
    public long getPosition() {
        return 0;
    }

    @Override
    public MusicSource getMusicSource() {
        return musicSource;
    }
}
