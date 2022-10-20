package dev.felnull.imp.client.integration;

import dev.felnull.imp.explatform.client.IMPSoundPhysicsRemasteredExpectPlatform;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class SoundPhysicsRemasteredAudio {
    private long lastUpdate;
    private Vec3 lastPos;

    public void onSound(int source, Vec3 soundPos) {
        if (soundPos == null) {
            IMPSoundPhysicsRemasteredExpectPlatform.setDefaultEnvironment(source);
            return;
        }

        long time = System.currentTimeMillis();

        if (time - lastUpdate < 100 && (lastPos != null && lastPos.distanceTo(soundPos) < 1D))
            return;

        IMPSoundPhysicsRemasteredExpectPlatform.setLastSoundCategoryAndName(SoundSource.MASTER, "iammusicplayer");
        IMPSoundPhysicsRemasteredExpectPlatform.onPlaySound(soundPos.x(), soundPos.y(), soundPos.z(), source);

        lastUpdate = time;
        lastPos = soundPos;
    }
}
