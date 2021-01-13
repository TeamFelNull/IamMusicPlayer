package red.felnull.imp.item;

import net.minecraft.util.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;

public class KatyouParabolicAntennaItem extends ParabolicAntennaItem {
    private static final ResourceLocation KATYOU_ANTENNA_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/katyou_antenna");

    public KatyouParabolicAntennaItem(Properties properties) {
        super(properties);
    }

    public ResourceLocation getAntennaTextuer() {
        return KATYOU_ANTENNA_MODEL;
    }
}
