package red.felnull.imp.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ParabolicAntennaItem extends Item {
    private final ResourceLocation texLocation;
    private final int writeSpeed;

    public ParabolicAntennaItem(Properties properties, ResourceLocation textuer, int writeSpeed) {
        super(properties);
        this.texLocation = textuer;
        this.writeSpeed = writeSpeed;
    }

    public ResourceLocation getAntennaTextuer() {
        return texLocation;
    }

    public int getWriteSpeed() {
        return writeSpeed;
    }
}
