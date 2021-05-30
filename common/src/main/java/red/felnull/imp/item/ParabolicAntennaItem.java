package red.felnull.imp.item;

import net.minecraft.world.item.Item;

public class ParabolicAntennaItem extends Item {
    private final float receptionMagnification;

    public ParabolicAntennaItem(Properties properties, float receptionMagnification) {
        super(properties.stacksTo(1));
        this.receptionMagnification = receptionMagnification;
    }

    public float getReceptionMagnification() {
        return receptionMagnification;
    }

}
