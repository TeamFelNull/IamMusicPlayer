package red.felnull.imp.item;

import net.minecraft.util.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;

public class KamesutaParabolicAntennaItem extends ParabolicAntennaItem {
    private static final ResourceLocation KAMESUTA_ANTENNA_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/kamesuta_antenna");

    public KamesutaParabolicAntennaItem(Properties properties) {
        super(properties);
    }

    public ResourceLocation getAntennaTextuer() {
        return KAMESUTA_ANTENNA_MODEL;
    }
}
