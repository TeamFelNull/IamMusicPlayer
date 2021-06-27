package red.felnull.imp.packet;

import net.minecraft.util.StringRepresentable;

public enum SyncType implements StringRepresentable {
    NONE("none"),
    MY_PLAYLISTS("my_playlists"),
    PUBLIC_PLAYLISTS("public_playlists"),
    MUSIC("music"),
    ALL_MUSIC("all_music");
    private final String name;

    SyncType(String name) {
        this.name = name;
    }

    public static SyncType getTypeByName(String name) {
        for (SyncType sc : values()) {
            if (sc.getSerializedName().equals(name))
                return sc;
        }
        return NONE;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
