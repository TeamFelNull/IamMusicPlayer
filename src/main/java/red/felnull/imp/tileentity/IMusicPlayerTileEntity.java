package red.felnull.imp.tileentity;

import red.felnull.imp.music.resource.PlayMusic;

import java.util.UUID;

public interface IMusicPlayerTileEntity {
    default void play(long pos) {

    }

    default boolean isPlaying() {
        return false;
    }

    boolean canPlay();

    void stoped();

    long getCurrentPlayPosition();

    void setCurrentPlayPosition(long position);

    UUID getMusicPlayerUUID();

    PlayMusic getMusic();
}
