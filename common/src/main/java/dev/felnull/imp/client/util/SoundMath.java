package dev.felnull.imp.client.util;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.openal.AL11;

public class SoundMath {
    /*public static float calculateVolume(float playerVolume) {
        return Mth.clamp(playerVolume * IamMusicPlayer.CONFIG.volume, 0.0F, 1.0F);
    }*/

    public static float calculatePseudoAttenuation(Vec3 position, float range, float volume) {
        float[] listenerX = new float[1];
        float[] listenerY = new float[1];
        float[] listenerZ = new float[1];
        AL11.alGetListener3f(AL11.AL_POSITION, listenerX, listenerY, listenerZ);
        Vec3 listener = new Vec3(listenerX[0], listenerY[0], listenerZ[0]);
        return calculatePseudoAttenuation((float) listener.distanceTo(position), range, volume);
    }

    private static float calculatePseudoAttenuation(float distance, float range, float volume) {
        float curve = (float) -Math.log10(9f / 10f * distance / range + 1f / 10f);
        //https://cdn.discordapp.com/attachments/465465434641006593/840981921068875857/unknown.png
        //distanceがrange越えると0になる対数関数
        var val = Mth.clamp(volume * curve, 0, 1);
        return Float.isNaN(val) ? 0 : val;
    }
}
