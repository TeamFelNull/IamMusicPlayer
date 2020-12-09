package red.felnull.imp.musicplayer;

import net.minecraft.nbt.CompoundNBT;
import red.felnull.otyacraftengine.data.INBTReadWriter;

public class PlayLocation implements INBTReadWriter {
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
        this.locationType = LocationType.valueOf(tag.getString("LocationType"));
        this.idOrURL = tag.getString("IdOrURL");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putString("LocationType", this.locationType.name());
        tag.putString("IdOrURL", this.idOrURL);
        return tag;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public String getIdOrURL() {
        return idOrURL;
    }

    public enum LocationType {
        WORLD_FILE,
        URL,
        YOUTUBE
    }
}
