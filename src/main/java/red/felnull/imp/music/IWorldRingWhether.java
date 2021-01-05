package red.felnull.imp.music;

import net.minecraft.util.math.vector.Vector3d;

public interface IWorldRingWhether {
    void musicPlayed();

    void musicStoped();

    boolean canMusicPlay();

    long getCurrentMusicPlayPosition();

    void setCurrentMusicPlayPosition(long position);

    Vector3d getMusicPos();

    float getMusicVolume();
}
