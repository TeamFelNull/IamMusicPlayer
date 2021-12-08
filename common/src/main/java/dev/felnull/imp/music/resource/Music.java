package dev.felnull.imp.music.resource;

import dev.felnull.otyacraftengine.data.ITAGSerializable;
import dev.felnull.otyacraftengine.util.OENbtUtil;
import net.minecraft.nbt.CompoundTag;

import java.util.Objects;
import java.util.UUID;

public class Music implements ITAGSerializable {
    private UUID uuid;
    private String name;
    private MusicSource source;
    private ImageInfo image;
    private UUID owner;

    public Music() {

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
        tag.putUUID("UUID", this.uuid);
        tag.putString("Name", this.name);
        OENbtUtil.writeSerializable(tag, "Source", this.source);
        OENbtUtil.writeSerializable(tag, "Image", this.image);
        tag.putUUID("Owner", owner);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.uuid = tag.getUUID("UUID");
        this.name = tag.getString("Name");
        this.source = OENbtUtil.readSerializable(tag, "Source", new MusicSource());
        this.image = OENbtUtil.readSerializable(tag, "Image", new ImageInfo());
        this.owner = tag.getUUID("Owner");
    }

    public String getName() {
        return name;
    }

    public ImageInfo getImage() {
        return image;
    }

    public MusicSource getSource() {
        return source;
    }

    public UUID getOwner() {
        return owner;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Music music = (Music) o;
        return Objects.equals(uuid, music.uuid) && Objects.equals(name, music.name) && Objects.equals(source, music.source) && Objects.equals(image, music.image) && Objects.equals(owner, music.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, source, image, owner);
    }

    @Override
    public String toString() {
        return "Music{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", source=" + source +
                ", image=" + image +
                ", owner=" + owner +
                '}';
    }
}
