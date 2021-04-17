package red.felnull.imp.music.resource;

import net.minecraft.nbt.CompoundTag;
import red.felnull.otyacraftengine.data.ITAGSerializable;

public class MusicLocation implements ITAGSerializable {
    private LocationType locationType;
    private String identifier;

    public MusicLocation(CompoundTag tag) {
        this.load(tag);
    }

    public MusicLocation(LocationType type, String identifier) {
        this.locationType = type;
        this.identifier = identifier;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("LocationType", this.locationType.getNmae());
        tag.putString("Identifier", this.identifier);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.locationType = LocationType.getLocationTypeByName(tag.getString("LocationType"));
        this.identifier = tag.getString("Identifier");
    }

    public static enum LocationType {
        EMPTY("empty"),
        URL("url"),
        YOUTUBE("youtube");
        private final String name;

        private LocationType(String name) {
            this.name = name;
        }

        public String getNmae() {
            return name;
        }

        public static LocationType getLocationTypeByName(String name) {
            for (LocationType it : values()) {
                if (it.getNmae().equals(name))
                    return it;
            }
            return EMPTY;
        }
    }
}
