package dev.felnull.imp.client.util;

import dev.felnull.imp.IamMusicPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.openal.AL11;

public class MusicMath {
    public static double calculateVolume(double playerVolume) {
        return Mth.clamp(playerVolume * IamMusicPlayer.CONFIG.volume, 0.0, 1.0);
    }

    public static double calculatePseudoAttenuation(Vec3 position, float range, double volume) {
        float[] listenerX = new float[1];
        float[] listenerY = new float[1];
        float[] listenerZ = new float[1];
        AL11.alGetListener3f(AL11.AL_POSITION, listenerX, listenerY, listenerZ);
        Vec3 listener = new Vec3(listenerX[0], listenerY[0], listenerZ[0]);
        return calculatePseudoAttenuation((float) listener.distanceTo(position), range, volume);
    }

    private static double calculatePseudoAttenuation(float distance, float range, double volume) {
        float curve = (float) -Math.log10(9f / 10f * distance / range + 1f / 10f);
        //https://cdn.discordapp.com/attachments/465465434641006593/840981921068875857/unknown.png
        //distanceがrange越えると0になる対数関数
        var val = Mth.clamp(volume * curve, 0, 1);
        return Double.isNaN(val) ? 0 : val;
    }
}
