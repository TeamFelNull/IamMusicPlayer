package red.felnull.imp.client.music.player;

import net.minecraft.world.phys.Vec3;
import red.felnull.imp.music.resource.MusicSource;

public interface IMusicPlayer {
    void ready(long position) throws Exception;

    void play(long delay);

    void stop();

    void setPosition(long position);

    void destroy();

    void pause();

    void unpause();

    boolean playing();

    boolean stopped();

    boolean paused();

    void setSelfPosition(Vec3 vec3);

    Vec3 getSelfPosition();

    void setVolume(float f);

    void linearAttenuation(float f);

    void disableAttenuation();

    void update();

    MusicSource getMusicLocation();

    long getPosition();
}
