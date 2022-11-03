package dev.felnull.imp.explatform.client;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.sounds.SoundSource;

public class IMPSoundPhysicsRemasteredExpectPlatform {
    @ExpectPlatform
    public static void setDefaultEnvironment(int sourceID) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void setLastSoundCategoryAndName(SoundSource sc, String name) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void onPlaySound(double posX, double posY, double posZ, int sourceID) {
        throw new AssertionError();
    }
}
