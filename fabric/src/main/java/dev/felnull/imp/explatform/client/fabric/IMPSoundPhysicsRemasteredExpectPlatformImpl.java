package dev.felnull.imp.explatform.client.fabric;

import com.sonicether.soundphysics.SoundPhysics;
import net.minecraft.sounds.SoundSource;

public class IMPSoundPhysicsRemasteredExpectPlatformImpl {
    public static void setDefaultEnvironment(int sourceID) {
        SoundPhysics.setDefaultEnvironment(sourceID);
    }

    public static void setLastSoundCategoryAndName(SoundSource sc, String name) {
        SoundPhysics.setLastSoundCategoryAndName(sc, name);
    }

    public static void onPlaySound(double posX, double posY, double posZ, int sourceID) {
        SoundPhysics.onPlaySound(posX, posY, posZ, sourceID);
    }
}
