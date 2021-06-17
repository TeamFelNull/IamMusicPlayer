package red.felnull.imp.music.resource;

import net.minecraft.nbt.CompoundTag;
import red.felnull.imp.data.resource.ImageInfo;
import red.felnull.otyacraftengine.data.ITAGSerializable;

import java.util.Objects;
import java.util.UUID;

public class Music implements ITAGSerializable {
    private UUID uuid;
    private String name;
    private MusicSource source;
    private ImageInfo image;
    private UUID owner;

    public Music(CompoundTag tag) {
        this.load(tag);
    }

    public Music(UUID uuid, String name, MusicSource source, ImageInfo image, UUID owner) {
        this.uuid = uuid;
        this.name = name;
        this.source = source;
        this.image = image;
        this.owner = owner;
    }


    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putUUID("UUID", uuid);
        tag.putString("Name", name);
        tag.put("MusicSource", source.save(new CompoundTag()));
        tag.put("ImageInfo", image.save(new CompoundTag()));
        tag.putUUID("Owner", owner);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.uuid = tag.getUUID("UUID");
        this.name = tag.getString("Name");
        this.source = new MusicSource(tag.getCompound("MusicSource"));
        this.image = new ImageInfo(tag.getCompound("ImageInfo"));
        this.owner = tag.getUUID("Owner");
    }

    public UUID getOwner() {
        return owner;
    }

    public ImageInfo getImage() {
        return image;
    }

    public MusicSource getSource() {
        return source;
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Music music = (Music) o;
        return o.equals(this.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, source, image, owner);
    }
}
