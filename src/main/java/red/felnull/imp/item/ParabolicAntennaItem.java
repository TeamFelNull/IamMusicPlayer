package red.felnull.imp.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;

public class ParabolicAntennaItem extends Item {
    private static final ResourceLocation PARABOLIC_ANTENNA_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/parabolic_antenna");

    public ParabolicAntennaItem(Properties properties) {
        super(properties);
    }

    public ResourceLocation getAntennaTextuer() {
        return PARABOLIC_ANTENNA_MODEL;
    }
}
