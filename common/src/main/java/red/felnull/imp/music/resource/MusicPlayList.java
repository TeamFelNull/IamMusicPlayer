package red.felnull.imp.music.resource;

import net.minecraft.nbt.CompoundTag;
import red.felnull.imp.data.resource.AdministratorInformation;
import red.felnull.imp.data.resource.ImageLocation;
import red.felnull.otyacraftengine.data.ITAGSerializable;
import red.felnull.otyacraftengine.util.IKSGNbtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MusicPlayList implements ITAGSerializable {
    private UUID uuid;
    private String name;
    private MusicPlayListDetailed detailed;
    private ImageLocation image;
    private AdministratorInformation administrator;
    private List<UUID> musicList = new ArrayList<>();

    public MusicPlayList(CompoundTag tag) {
        load(tag);
    }

    public MusicPlayList(UUID uuid, String name, MusicPlayListDetailed detailed, ImageLocation image, AdministratorInformation administrator, List<UUID> musicList) {
        this.uuid = uuid;
        this.name = name;
        this.detailed = detailed;
        this.image = image;
        this.administrator = administrator;
        this.musicList = musicList;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putUUID("UUID", uuid);
        tag.putString("Name", name);
        tag.put("MusicPlayListDetailed", detailed.save(new CompoundTag()));
        tag.put("ImageLocation", image.save(new CompoundTag()));
        tag.put("AdministratorInformation", administrator.save(new CompoundTag()));
        IKSGNbtUtil.writeUUIDList(tag, "MusicList", musicList);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.uuid = tag.getUUID("UUID");
        this.name = tag.getString("Name");
        this.detailed = new MusicPlayListDetailed(tag.getCompound("MusicPlayListDetailed"));
        this.image = new ImageLocation(tag.getCompound("ImageLocation"));
        this.administrator = new AdministratorInformation(tag.getCompound("AdministratorInformation"));
        IKSGNbtUtil.readUUIDList(tag, "MusicList", musicList);
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public ImageLocation getImage() {
        return image;
    }

    public AdministratorInformation getAdministrator() {
        return administrator;
    }

    public List<UUID> getMusicList() {
        return musicList;
    }

    public MusicPlayListDetailed getDetailed() {
        return detailed;
    }
}
