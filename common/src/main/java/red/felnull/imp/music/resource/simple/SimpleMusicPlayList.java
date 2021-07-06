package red.felnull.imp.music.resource.simple;

import net.minecraft.nbt.CompoundTag;
import red.felnull.imp.data.resource.ImageInfo;
import red.felnull.otyacraftengine.data.ITAGSerializable;
import red.felnull.otyacraftengine.util.IKSGNbtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SimpleMusicPlayList implements ITAGSerializable {
    private UUID uuid;
    private String name;
    private ImageInfo image;
    private UUID owner;
    private List<UUID> administrators = new ArrayList<>();
    private List<UUID> players = new ArrayList<>();
    private List<UUID> baned = new ArrayList<>();
    private int playerCont;

    public SimpleMusicPlayList(CompoundTag tag) {
        load(tag);
    }

    public SimpleMusicPlayList(UUID uuid, String name, ImageInfo image, UUID owner, int playerCont, List<UUID> administrators, List<UUID> players, List<UUID> baned) {
        this.uuid = uuid;
        this.name = name;
        this.image = image;
        this.owner = owner;
        this.playerCont = playerCont;
        this.administrators = administrators;
        this.players = players;
        this.baned = baned;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putUUID("UUID", uuid);
        tag.putString("Name", name);
        tag.put("ImageInfo", image.save(new CompoundTag()));
        tag.putUUID("Owner", owner);
        tag.putInt("PlayerCont", playerCont);
        IKSGNbtUtil.writeUUIDList(tag, "Administrators", administrators);
        IKSGNbtUtil.writeUUIDList(tag, "Players", players);
        IKSGNbtUtil.writeUUIDList(tag, "Baned", baned);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.uuid = tag.getUUID("UUID");
        this.name = tag.getString("Name");
        this.image = new ImageInfo(tag.getCompound("ImageInfo"));
        this.owner = tag.getUUID("Owner");
        this.playerCont = tag.getInt("PlayerCont");
        IKSGNbtUtil.readUUIDList(tag, "Administrators", administrators);
        IKSGNbtUtil.readUUIDList(tag, "Players", players);
        IKSGNbtUtil.readUUIDList(tag, "Baned", baned);
    }

    public UUID getOwner() {
        return owner;
    }

    public ImageInfo getImage() {
        return image;
    }

    public int getPlayerCont() {
        return playerCont;
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public List<UUID> getAdministrators() {
        return administrators;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public List<UUID> getBaned() {
        return baned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleMusicPlayList that = (SimpleMusicPlayList) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, image, owner, playerCont);
    }
}
