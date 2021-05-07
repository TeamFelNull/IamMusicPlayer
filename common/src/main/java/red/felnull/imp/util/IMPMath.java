package red.felnull.imp.util;

import net.minecraft.util.Mth;
import red.felnull.imp.client.IamMusicPlayerClient;

public class IMPMath {
    public static float calculateVolume(float playerVolume) {
        return Mth.clamp(playerVolume * IamMusicPlayerClient.CLIENT_CONFIG.volume, 0.0F, 1.0F);
    }
}
