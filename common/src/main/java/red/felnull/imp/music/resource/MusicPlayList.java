package red.felnull.imp.music.resource;

import net.minecraft.nbt.CompoundTag;
import red.felnull.imp.data.resource.AdministratorInformation;
import red.felnull.imp.data.resource.ImageInfo;
import red.felnull.imp.music.resource.simple.SimpleMusicPlayList;
import red.felnull.otyacraftengine.data.ITAGSerializable;
import red.felnull.otyacraftengine.util.IKSGNbtUtil;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MusicPlayList implements ITAGSerializable {
    public static final MusicPlayList ALL = new MusicPlayList(UUID.fromString("cf9b3688-5cd7-7b89-c3ce-1f3c585b4c61"), "All", MusicPlayListDetailed.EMPTY, new ImageInfo(ImageInfo.ImageType.STRING, "All"), AdministratorInformation.EMPTY, new ArrayList<>(), new ArrayList<>());
    private UUID uuid;
    private String name;
    private MusicPlayListDetailed detailed;
    private ImageInfo image;
    private AdministratorInformation administrator;
    private List<UUID> musicList = new ArrayList<>();
    private List<UUID> playerList = new ArrayList<>();

    public MusicPlayList(CompoundTag tag) {
        load(tag);
    }

    public MusicPlayList(UUID uuid, String name, MusicPlayListDetailed detailed, ImageInfo image, AdministratorInformation administrator, List<UUID> musicList, List<UUID> playerList) {
        this.uuid = uuid;
        this.name = name;
        this.detailed = detailed;
        this.image = image;
        this.administrator = administrator;
        this.musicList = musicList;
        this.playerList = playerList;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putUUID("UUID", uuid);
        tag.putString("Name", name);
        tag.put("MusicPlayListDetailed", detailed.save(new CompoundTag()));
        tag.put("ImageInfo", image.save(new CompoundTag()));
        tag.put("AdministratorInformation", administrator.save(new CompoundTag()));
        IKSGNbtUtil.writeUUIDList(tag, "MusicList", musicList);
        IKSGNbtUtil.writeUUIDList(tag, "PlayerList", playerList);

        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.uuid = tag.getUUID("UUID");
        this.name = tag.getString("Name");
        this.detailed = new MusicPlayListDetailed(tag.getCompound("MusicPlayListDetailed"));
        this.image = new ImageInfo(tag.getCompound("ImageInfo"));
        this.administrator = new AdministratorInformation(tag.getCompound("AdministratorInformation"));
        IKSGNbtUtil.readUUIDList(tag, "MusicList", musicList);
        IKSGNbtUtil.readUUIDList(tag, "PlayerList", playerList);
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public ImageInfo getImage() {
        return image;
    }

    public AdministratorInformation getAdministrator() {
        return administrator;
    }

    public List<UUID> getMusicList() {
        return musicList;
    }

    public List<UUID> getPlayerList() {
        return playerList;
    }

    public MusicPlayListDetailed getDetailed() {
        return detailed;
    }

    public SimpleMusicPlayList getSimple() {
        return new SimpleMusicPlayList(uuid, name, image, getOwner(), playerList.size(), administrator.getAdminData().entrySet().stream().filter(n -> n.getValue().canSave()).map(Map.Entry::getKey).toList(), playerList);
    }

    public UUID getOwner() {
        return playerList.stream().filter(n -> administrator.getAuthority(n).isOwner()).findFirst().orElse(playerList.isEmpty() ? IKSGPlayerUtil.getFakeUUID() : playerList.get(0));
    }

}
