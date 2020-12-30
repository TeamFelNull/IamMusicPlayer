package red.felnull.imp.block.propertie;

import net.minecraft.util.IStringSerializable;

public enum BoomboxMode implements IStringSerializable {
    NONE("none"),
    PAUSE("pause"),
    STOP("stop"),
    PLAY("play");

    private final String name;

    private BoomboxMode(String name) {
        this.name = name;
    }

    public static BoomboxMode getScreenByName(String name) {
        for (BoomboxMode sc : values()) {
            if (sc.getName().equals(name))
                return sc;
        }
        return NONE;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getString() {
        return name;
    }
}
