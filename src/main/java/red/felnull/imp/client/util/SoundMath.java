package red.felnull.imp.client.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.openal.AL11;

import static org.lwjgl.openal.AL10.AL_POSITION;

public class SoundMath {
    public static float calculateVolume(float playerVolume, float config) {
        return MathHelper.clamp(playerVolume * config, 0.0F, 1.0F);
    }

    public static float calculatePseudoAttenuation(Vector3d position, float range, float volume) {
        float[] listenerX = new float[1];
        float[] listenerY = new float[1];
        float[] listenerZ = new float[1];
        AL11.alGetListener3f(AL_POSITION, listenerX, listenerY, listenerZ);
        Vector3d listener = new Vector3d(listenerX[0], listenerY[0], listenerZ[0]);
        return calculatePseudoAttenuation((float) listener.distanceTo(position), range, volume);
    }

    private static float calculatePseudoAttenuation(float distance, float range, float volume) {
        float curve = (float) -Math.log10(9f / 10f * distance / range + 1f / 10f);
        //https://cdn.discordapp.com/attachments/465465434641006593/840981921068875857/unknown.png
        //distanceがrange越えると0になる対数関数
        float val = MathHelper.clamp(volume * curve, 0, 1);
        return Float.isNaN(val) ? 0 : val;
    }
}
