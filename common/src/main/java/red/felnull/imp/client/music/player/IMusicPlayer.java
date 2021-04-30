package red.felnull.imp.client.music.player;

import net.minecraft.world.phys.Vec3;

public interface IMusicPlayer {
    void ready(long position) throws Exception;

    void play();

    void stop();

    void setPosition(long position);

    void destroy();

    void pause();

    void unpause();

    boolean playing();

    boolean stopped();

    void setSelfPosition(Vec3 vec3);

    void setLooping(boolean bl);

    void setVolume(float f);

    void linearAttenuation(float f);

    void disableAttenuation();

    void update();
}
