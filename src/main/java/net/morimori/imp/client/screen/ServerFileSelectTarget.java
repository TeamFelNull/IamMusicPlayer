package net.morimori.imp.client.screen;

import net.minecraft.util.IStringSerializable;

public enum ServerFileSelectTarget implements IStringSerializable {
    MAIN("main"), EVERYONE("everyone");

    private final String name;

    ServerFileSelectTarget(String string) {
        this.name = string;
    }

    @Override
    public String getName() {

        return this.name;
    }

    public String toString() {
        return this.name;
    }

}
