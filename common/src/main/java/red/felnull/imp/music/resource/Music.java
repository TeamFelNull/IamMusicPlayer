package red.felnull.imp.music.resource;

import net.minecraft.nbt.CompoundTag;
import red.felnull.imp.data.resource.AdministratorInformation;
import red.felnull.imp.data.resource.ImageInfo;
import red.felnull.otyacraftengine.data.ITAGSerializable;

import java.util.UUID;

public class Music implements ITAGSerializable {
    private UUID uuid;
    private String name;
    private long duration;
    private MusicLocation location;
    private MusicDetailed detailed;
    private ImageInfo image;
    private AdministratorInformation administrator;

    public Music(CompoundTag tag) {
        this.load(tag);
    }

    public Music(UUID uuid, String name, long duration, MusicLocation location, MusicDetailed detailed, ImageInfo image, AdministratorInformation administrator) {
        this.uuid = uuid;
        this.name = name;
        this.duration = duration;
        this.location = location;
        this.detailed = detailed;
        this.image = image;
        this.administrator = administrator;
    }


    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putUUID("UUID", uuid);
        tag.putString("Name", name);
        tag.putLong("Duration", duration);
        tag.put("MusicLocation", location.save(new CompoundTag()));
        tag.put("MusicDetailed", detailed.save(new CompoundTag()));
        tag.put("ImageLocation", image.save(new CompoundTag()));
        tag.put("AdministratorInformation", administrator.save(new CompoundTag()));
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.uuid = tag.getUUID("UUID");
        this.name = tag.getString("Name");
        this.duration = tag.getLong("Duration");
        this.location = new MusicLocation(tag.getCompound("MusicLocation"));
        this.detailed = new MusicDetailed(tag.getCompound("MusicDetailed"));
        this.image = new ImageInfo(tag.getCompound("ImageLocation"));
        this.administrator = new AdministratorInformation(tag.getCompound("AdministratorInformation"));
    }

    public AdministratorInformation getAdministrator() {
        return administrator;
    }

    public ImageInfo getImage() {
        return image;
    }

    public long getDuration() {
        return duration;
    }

    public MusicDetailed getDetailed() {
        return detailed;
    }

    public MusicLocation getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }
}
