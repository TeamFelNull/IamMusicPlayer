package red.felnull.imp.item;

import net.minecraft.world.item.Item;

public class CassetteTapeItem extends Item {
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
