package dev.felnull.imp.client.nmusic.task;

import dev.felnull.imp.client.util.MusicUtils;

public interface MusicTaskRunner {
    default void addTaskThrow(Runnable runnable, Runnable destroy, Runnable destroyOnTick) {
        if (addTask(runnable, destroy, destroyOnTick))
            throw new RuntimeException("Stopped!");
    }

    default boolean addTask(Runnable runnable, Runnable destroy, Runnable destroyOnTick) {
        return addTask(new MusicTask() {
            @Override
            public void execute() {
                if (runnable != null) runnable.run();
            }

            @Override
            public void destroy() {
                if (destroy != null) destroy.run();
            }

            @Override
            public void destroyOnTick() {
                if (destroyOnTick != null) destroyOnTick.run();
            }
        });
    }

    default boolean addTask(MusicTask task) {
        if (isDestroy()) {
            task.destroy();
            MusicUtils.runOnMusicTick(task::destroyOnTick);
            return true;
        }
        MusicUtils.runOnMusicTick(task::execute);
        return false;
    }

    boolean isDestroy();
}
