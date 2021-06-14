package red.felnull.imp.music.resource.simple;

import net.minecraft.nbt.CompoundTag;
import red.felnull.imp.data.resource.ImageInfo;
import red.felnull.otyacraftengine.data.ITAGSerializable;

import java.util.UUID;

public class SimpleMusicPlayList implements ITAGSerializable {
    private UUID uuid;
    private String name;
    private ImageInfo image;
    private UUID owner;
    private int playerCont;

    public SimpleMusicPlayList(CompoundTag tag) {
        load(tag);
    }

    public SimpleMusicPlayList(UUID uuid, String name, ImageInfo image, UUID owner, int playerCont) {
        this.uuid = uuid;
        this.name = name;
        this.image = image;
        this.owner = owner;
        this.playerCont = playerCont;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putUUID("UUID", uuid);
        tag.putString("Name", name);
        tag.put("ImageInfo", image.save(new CompoundTag()));
        tag.putUUID("Owner", owner);
        tag.putInt("PlayerCont", playerCont);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.uuid = tag.getUUID("UUID");
        this.name = tag.getString("Name");
        this.image = new ImageInfo(tag.getCompound("ImageInfo"));
        this.owner = tag.getUUID("Owner");
        this.playerCont = tag.getInt("PlayerCont");
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

    public UUID getUuid() {
        return uuid;
    }

}
