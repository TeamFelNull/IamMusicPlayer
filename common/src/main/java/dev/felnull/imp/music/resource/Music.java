package dev.felnull.imp.music.resource;

import dev.felnull.otyacraftengine.server.data.ITAGSerializable;
import dev.felnull.otyacraftengine.util.OENbtUtil;
import net.minecraft.nbt.CompoundTag;

import java.util.Objects;
import java.util.UUID;

public class Music implements ITAGSerializable, IIMPComparable {
    private UUID uuid;
    private String name;
    private String author;
    private MusicSource source;
    private ImageInfo image;
    private UUID owner;
    private long createDate;

    public Music() {

    }

    public Music(UUID uuid, String name, String author, MusicSource source, ImageInfo image, UUID owner, long createDate) {
        this.uuid = uuid;
        this.name = name;
        this.author = author;
        this.source = source;
        this.image = image;
        this.owner = owner;
        this.createDate = createDate;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putUUID("UUID", this.uuid);
        tag.putString("Name", this.name);
        tag.putString("Author", this.author);
        OENbtUtil.writeSerializable(tag, "Source", this.source);
        OENbtUtil.writeSerializable(tag, "Image", this.image);
        tag.putUUID("Owner", owner);
        tag.putLong("CreateDate", createDate);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.uuid = tag.getUUID("UUID");
        this.name = tag.getString("Name");
        this.author = tag.getString("Author");
        this.source = OENbtUtil.readSerializable(tag, "Source", new MusicSource());
        this.image = OENbtUtil.readSerializable(tag, "Image", new ImageInfo());
        this.owner = tag.getUUID("Owner");
        this.createDate = tag.getLong("CreateDate");
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

    public long getCreateDate() {
        return createDate;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Music music = (Music) o;
        return createDate == music.createDate && Objects.equals(uuid, music.uuid) && Objects.equals(name, music.name) && Objects.equals(author, music.author) && Objects.equals(source, music.source) && Objects.equals(image, music.image) && Objects.equals(owner, music.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, author, source, image, owner, createDate);
    }

    @Override
    public String toString() {
        return "Music{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", source=" + source +
                ", image=" + image +
                ", owner=" + owner +
                ", createDate=" + createDate +
                '}';
    }

    @Override
    public String getCompareName() {
        return name;
    }

    @Override
    public String getComparePlayerName() {
        return owner.toString();
    }

    @Override
    public long getCompareDate() {
        return createDate;
    }
}
