package net.morimori.imp.sound;

import java.nio.file.Paths;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class WorldPlayListSoundData {

	private String uuid;
	private String name;
	private String folder;

	private SoundData data;

	public WorldPlayListSoundData(String name, String folder, String uuid, SoundData sounddata) {
		this.name = name;
		this.folder = folder;
		this.uuid = uuid;
		this.data = sounddata;
	}

	public WorldPlayListSoundData(String file, String uuid, SoundData sounddata) {
		this(Paths.get(file).toFile().getName(), Paths.get(file).getParent().toFile().getName(), uuid, sounddata);
	}

	public WorldPlayListSoundData(CompoundNBT tag) {
		readNBT(tag);
	}

	public void readNBT(CompoundNBT tag) {
		this.name = tag.getString("Name");
		this.folder = tag.getString("Folder");
		this.uuid = tag.getString("UUID");
		this.data = new SoundData((CompoundNBT) tag.get("SoundData"));
	}

	public CompoundNBT writeNBT(CompoundNBT tag) {

		tag.putString("Name", this.name);
		tag.putString("Folder", this.folder);
		tag.putString("UUID", this.uuid);

		tag.put("SoundData", data.writeNBT(new CompoundNBT()));

		return tag;
	}

	public String getName() {
		return name;
	}

	public boolean equals(WorldPlayListSoundData data) {

		return this.getName().equals(data.getName()) && this.getFolderName().equals(data.getFolderName())
				&& this.getUUID().equals(data.getUUID());
	}

	public String getFolderName() {
		return folder;
	}

	public String getUUID() {
		return uuid;
	}

	public SoundData getSoundData() {
		return data;
	}

	public static WorldPlayListSoundData getWorldPlayListData(ItemStack stack) {

		return new WorldPlayListSoundData(stack.getOrCreateTag().getCompound("WorldPlayListSoundData"));
	}

	public static void setWorldPlayList(ItemStack stack, WorldPlayListSoundData WPLSD) {
		CompoundNBT tag = stack.getOrCreateTag();
		tag.put("WorldPlayListSoundData", WPLSD.writeNBT(new CompoundNBT()));
	}
}
