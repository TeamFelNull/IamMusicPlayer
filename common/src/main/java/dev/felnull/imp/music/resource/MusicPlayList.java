package dev.felnull.imp.music.resource;

import dev.felnull.otyacraftengine.server.level.TagSerializable;
import dev.felnull.otyacraftengine.util.OENbtUtils;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MusicPlayList implements TagSerializable, IIMPComparable {
    private UUID uuid;
    private String name;
    private ImageInfo image;
    private AuthorityInfo authority;
    private List<UUID> musicList = new ArrayList<>();
    private long createDate;

    public MusicPlayList() {

    }

    public MusicPlayList(UUID uuid, String name, ImageInfo image, AuthorityInfo authority, List<UUID> musicList, long createDate) {
        this.uuid = uuid;
        this.name = name;
        this.image = image;
        this.authority = authority;
        this.musicList = musicList;
        this.createDate = createDate;
    }

    @Override
    public void save(CompoundTag tag) {
        tag.putUUID("UUID", uuid);
        tag.putString("Name", name);
        tag.put("Image", image.createSavedTag());
        tag.put("Authority", authority.createSavedTag());
        OENbtUtils.writeUUIDList(tag, "MusicList", musicList);
        tag.putLong("CreateDate", createDate);
    }

    @Override
    public void load(CompoundTag tag) {
        this.uuid = tag.getUUID("UUID");
        this.name = tag.getString("Name");
        this.image = TagSerializable.loadSavedTag(tag.getCompound("Image"), new ImageInfo());
        this.authority = TagSerializable.loadSavedTag(tag.getCompound("Authority"), new AuthorityInfo());
        OENbtUtils.readUUIDList(tag, "MusicList", musicList);
        this.createDate = tag.getLong("CreateDate");
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

    public long getCreateDate() {
        return createDate;
    }

    public int getPlayerCount() {
        return (int) authority.getPlayersAuthority().values().stream().filter(AuthorityInfo.AuthorityType::isMoreReadOnly).count();
    }

    @Override
    public String toString() {
        return "MusicPlayList{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", image=" + image +
                ", authority=" + authority +
                ", musicList=" + musicList +
                ", createDate=" + createDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicPlayList that = (MusicPlayList) o;
        return createDate == that.createDate && Objects.equals(uuid, that.uuid) && Objects.equals(name, that.name) && Objects.equals(image, that.image) && Objects.equals(authority, that.authority) && Objects.equals(musicList, that.musicList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, image, authority, musicList, createDate);
    }

    @Override
    public String getCompareName() {
        return name;
    }

    @Override
    public String getComparePlayerName() {
        return authority.getOwner().toString();
    }

    @Override
    public long getCompareDate() {
        return createDate;
    }
}
