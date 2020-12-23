package red.felnull.imp.musicplayer;

import net.minecraft.nbt.CompoundNBT;
import red.felnull.otyacraftengine.data.INBTReadWriter;

import java.util.Objects;

public class PlayLocation implements INBTReadWriter {
    public static final PlayLocation EMPTY = new PlayLocation(LocationType.EMPTY, "");
    private LocationType locationType;
    private String idOrURL;

    public PlayLocation(LocationType type, String idOrURL) {
        this.locationType = type;
        this.idOrURL = idOrURL;
    }

    public PlayLocation(CompoundNBT tag) {
        read(tag);
    }

    @Override
    public void read(CompoundNBT tag) {
        this.locationType = LocationType.getLocationTypeByName(tag.getString("LocationType"));
        this.idOrURL = tag.getString("IdOrURL");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putString("LocationType", this.locationType.getNmae());
        tag.putString("IdOrURL", this.idOrURL);
        return tag;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public String getIdOrURL() {
        return idOrURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayLocation that = (PlayLocation) o;
        return locationType == that.locationType && Objects.equals(idOrURL, that.idOrURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationType, idOrURL);
    }

    public enum LocationType {
        EMPTY("empty"),
        WORLD_FILE("world_file"),
        URL("url"),
        YOUTUBE("youtube");
        private final String nmae;

        private LocationType(String name) {
            this.nmae = name;
        }

        public String getNmae() {
            return nmae;
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
