package red.felnull.imp.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;

public class ParabolicAntennaItem extends Item {
    private final ResourceLocation texLocation;
    private final float writeSpeedMagnification;

    public ParabolicAntennaItem(Properties properties, ResourceLocation textuer, float writeSpeedMagnification) {
        super(properties);
        this.texLocation = textuer;
        this.writeSpeedMagnification = writeSpeedMagnification;
    }

    public ResourceLocation getAntennaTextuer() {
        return texLocation;
    }

    public float getWriteSpeedMagnification() {
        return writeSpeedMagnification;
    }
}
