package dev.felnull.imp.client.nmusic.task;

import dev.felnull.imp.client.nmusic.MusicEngine;

import java.util.concurrent.CompletableFuture;

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
        var mex = MusicEngine.getInstance().getMusicTickExecutor();
        if (isDestroy()) {
            task.destroy();
            CompletableFuture.runAsync(task::destroyOnTick, mex).join();
            return true;
        }
        CompletableFuture.runAsync(task::execute, mex).join();
        return false;
    }

    boolean isDestroy();
}
