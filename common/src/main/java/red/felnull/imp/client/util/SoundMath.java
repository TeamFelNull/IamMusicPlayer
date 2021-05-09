package red.felnull.imp.client.util;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.openal.AL11;
import red.felnull.imp.client.IamMusicPlayerClient;

import static org.lwjgl.openal.AL10.AL_POSITION;

public class SoundMath {
    public static float calculateVolume(float playerVolume) {
        return Mth.clamp(playerVolume * IamMusicPlayerClient.CLIENT_CONFIG.volume, 0.0F, 1.0F);
    }

    public static float calculatePseudoAttenuation(Vec3 position, float attenuation, float volume) {
        float[] listenerX = new float[1];
        float[] listenerY = new float[1];
        float[] listenerZ = new float[1];
        AL11.alGetListener3f(AL_POSITION, listenerX, listenerY, listenerZ);
        Vec3 listener = new Vec3(listenerX[0], listenerY[0], listenerZ[0]);
        return calculatePseudoAttenuation((float) listener.distanceTo(position), attenuation, volume);
    }

    private static float calculatePseudoAttenuation(float distance, float attenuation, float volume) {
        float zure = attenuation / 5f;
        float atzure = attenuation / 10f;
        float nn = Math.min((volume / (distance - zure)) * 3f, 1f);
        float at = Math.min((volume / (attenuation - zure)) * 3f, 1f);
        return Mth.clamp(distance <= zure ? 1f : distance <= (attenuation - atzure) ? nn : at * ((distance - attenuation) * -1 / atzure), 0f, 1f);
    }
}
