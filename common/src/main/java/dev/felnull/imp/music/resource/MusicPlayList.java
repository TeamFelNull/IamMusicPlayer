package dev.felnull.imp.music.resource;

import dev.felnull.otyacraftengine.data.ITAGSerializable;
import dev.felnull.otyacraftengine.util.OENbtUtil;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MusicPlayList implements ITAGSerializable {
    private UUID uuid;
    private String name;
    private ImageInfo image;
    private AuthorityInfo authority;
    private List<UUID> musicList = new ArrayList<>();

    public MusicPlayList() {

    }

    public MusicPlayList(UUID uuid, String name, ImageInfo image, AuthorityInfo authority, List<UUID> musicList) {
        this.uuid = uuid;
        this.name = name;
        this.image = image;
        this.authority = authority;
        this.musicList = musicList;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putUUID("UUID", uuid);
        tag.putString("Name", name);
        OENbtUtil.writeSerializable(tag, "Image", image);
        OENbtUtil.writeSerializable(tag, "Authority", authority);
        OENbtUtil.writeUUIDList(tag, "MusicList", musicList);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.uuid = tag.getUUID("UUID");
        this.name = tag.getString("Name");
        this.image = OENbtUtil.readSerializable(tag, "Image", new ImageInfo());
        this.authority = OENbtUtil.readSerializable(tag, "Authority", new AuthorityInfo());
        OENbtUtil.readUUIDList(tag, "MusicList", musicList);
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public AuthorityInfo getAuthority() {
        return authority;
    }

    public ImageInfo getImage() {
        return image;
    }

    public List<UUID> getMusicList() {
        return musicList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicPlayList that = (MusicPlayList) o;
        return Objects.equals(uuid, that.uuid) && Objects.equals(name, that.name) && Objects.equals(image, that.image) && Objects.equals(authority, that.authority) && Objects.equals(musicList, that.musicList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, image, authority, musicList);
    }

    @Override
    public String toString() {
        return "MusicPlayList{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", image=" + image +
                ", authority=" + authority +
                ", musicList=" + musicList +
                '}';
    }
}
