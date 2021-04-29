package red.felnull.imp.client.music.player;

import com.mojang.math.Vector3f;

public interface IMusicPlayer {
    void ready(long position) throws Exception;

    void play();

    void stop();

    void setPosition(long position);

    void clear();

    void setPos(Vector3f pos);
}
