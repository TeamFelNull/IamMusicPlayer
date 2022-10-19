package dev.felnull.imp.client.integration;

import com.sonicether.soundphysics.SoundPhysics;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class SoundPhysicsRemasteredAudio {
    private final String hashCode;
    private long lastUpdate;
    private Vec3 lastPos;

    public SoundPhysicsRemasteredAudio(String hashCode) {
        this.hashCode = hashCode;
        System.out.println(hashCode);
    }

    public void onSound(int source, Vec3 soundPos) {
        if (soundPos == null) {
            SoundPhysics.setDefaultEnvironment(source);
            return;
        }

        long time = System.currentTimeMillis();

        if (time - lastUpdate < 100 && (lastPos != null && lastPos.distanceTo(soundPos) < 1D))
            return;

        SoundPhysics.setLastSoundCategoryAndName(SoundSource.MASTER, "iammusicplayer_" + hashCode);
        SoundPhysics.onPlaySound(soundPos.x(), soundPos.y(), soundPos.z(), source);

        lastUpdate = time;
        lastPos = soundPos;
    }
}
