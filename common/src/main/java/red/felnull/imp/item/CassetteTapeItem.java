package red.felnull.imp.item;

import net.minecraft.world.item.DyeableLeatherItem;
import red.felnull.otyacraftengine.item.IkisugiItem;

public class CassetteTapeItem extends IkisugiItem implements DyeableLeatherItem {
    private final BaseType type;

    public CassetteTapeItem(Properties properties, BaseType type) {
        super(properties.stacksTo(1));
        this.type = type;
    }

    public BaseType getType() {
        return type;
    }

    public static enum BaseType {
        NORMAL, GLASS
    }
}
