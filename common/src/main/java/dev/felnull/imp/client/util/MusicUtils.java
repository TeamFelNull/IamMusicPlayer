package dev.felnull.imp.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.felnull.fnjl.concurrent.InvokeExecutor;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.music.MusicEngine;
import dev.felnull.imp.music.SpatialType;
import org.lwjgl.openal.AL10;

import java.util.concurrent.CompletableFuture;

public class MusicUtils {
    public static void checkALError() {
        int e = AL10.alGetError();
        switch (e) {
            case AL10.AL_INVALID_NAME -> throw new RuntimeException("Invalid name parameter");
            case AL10.AL_INVALID_ENUM -> throw new RuntimeException("Invalid parameter");
            case AL10.AL_INVALID_VALUE -> throw new RuntimeException("Invalid enum parameter value");
            case AL10.AL_INVALID_OPERATION -> throw new RuntimeException("Illegal call");
            case AL10.AL_OUT_OF_MEMORY -> throw new RuntimeException("Unable to allocate memory");
        }
        if (e != AL10.AL_NO_ERROR)
            throw new RuntimeException("Unknown error");
    }

    public static boolean isOnMusicTick() {
        return RenderSystem.isOnRenderThread();
    }

    public static void assertOnMusicTick() {
        if (!isOnMusicTick())
            throw new RuntimeException("Call from wrong thread");
    }

    public static void runOnMusicTick(Runnable runnable) {
        if (isOnMusicTick()) {
            runnable.run();
        } else {
            CompletableFuture.runAsync(runnable, MusicEngine.getInstance().getMusicTickExecutor()).join();
        }
    }

    public static void runInvokeTasks(InvokeExecutor executor, String name) {
        executor.runTasks(100);
        if (executor.getTaskCount() != 0)
            MusicEngine.getInstance().getLogger().warn("A lot of music processing tasks are running,Execute separately to reduce the load - Remaining " + name + " Tasks:" + executor.getTaskCount());
    }

    public static boolean isSpatial(SpatialType spatialType) {
        if (spatialType == SpatialType.ENTRUST)
            return IamMusicPlayer.CONFIG.spatial;
        return spatialType == SpatialType.ENABLE;
    }
}
