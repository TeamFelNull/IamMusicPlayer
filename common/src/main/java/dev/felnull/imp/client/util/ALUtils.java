package dev.felnull.imp.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import org.lwjgl.openal.AL10;

public class ALUtils {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void checkError() {
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

    public static boolean isOnSoundThread() {
        return RenderSystem.isOnRenderThread();
    }

    public static void assertOnSoundThread() {
        if (!isOnSoundThread())
            throw new RuntimeException("Call from wrong thread");
    }

    public static void runOnSoundThread(Runnable runnable) {
        if (isOnSoundThread()) {
            runnable.run();
        } else {
            mc.submit(runnable).join();
        }
    }
}
