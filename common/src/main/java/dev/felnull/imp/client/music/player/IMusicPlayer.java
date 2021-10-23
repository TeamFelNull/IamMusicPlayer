package dev.felnull.imp.client.music.player;

import dev.felnull.imp.music.MusicPlaybackInfo;
import dev.felnull.imp.music.resource.MusicSource;
import net.minecraft.world.phys.Vec3;

public interface IMusicPlayer {
    void ready(long position) throws Exception;

    void play(long delay);

    void stop();

    void destroy();

    void pause();

    void unpause();

    boolean isPlaying();

    boolean isPaused();

    boolean isFinished();

    void setCoordinatePosition(Vec3 vec3);

    Vec3 getCoordinatePosition();

    void setVolume(float v);

    void setRange(float r);

    void update(MusicPlaybackInfo playbackInf);

    long getPosition();

    MusicSource getMusicSource();
}