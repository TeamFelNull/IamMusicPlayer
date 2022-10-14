package dev.felnull.imp.client.nmusic.task;

import dev.felnull.imp.client.util.MusicUtils;

public interface MusicDestroyRunner {
    default void run(Runnable ifDestroy) {
        if (isDestroy()) {
            MusicUtils.runOnMusicTick(ifDestroy);
            throw new RuntimeException("Stopped!");
        }
    }

    boolean isDestroy();
}
