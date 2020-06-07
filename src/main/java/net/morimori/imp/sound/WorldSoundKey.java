package net.morimori.imp.sound;

import java.nio.file.Path;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.morimori.imp.file.PlayList;
import net.morimori.imp.util.FileHelper;

public class WorldSoundKey {

	private String folder;
	private String name;
	private String uuid;

	public WorldSoundKey(String folder, String name, String uuid) {
		this.folder = folder;
		this.name = name;
		this.uuid = uuid;
	}

	public WorldSoundKey(WorldPlayListSoundData wplsd) {
		this(wplsd.getFolderName(), wplsd.getName(), wplsd.getUUID());
	}

	public String getFolder() {
		return folder;
	}

	public String getName() {
		return name;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean isClientExistence() {

		boolean flag1 = getClientPath().toFile().exists();
		boolean flag2 = PlayList.getClientPlaylistNBTdata(getClientPath(), "UUID").equals(getUUID());

		return flag1 && flag2;
	}

	@OnlyIn(Dist.CLIENT)
	public Path getClientPath() {
		return FileHelper.getClientCurrentServerPlaylistPath().resolve(getFolder()).resolve(getName());
	}

	public boolean isServerExistence(MinecraftServer ms) {

		boolean flag1 = getServerPath(ms).toFile().exists();
		boolean flag2 = PlayList.getWorldPlaylistNBTDataString(ms, getServerPath(ms), "UUID").equals(getUUID());

		return flag1 && flag2;
	}

	public Path getServerPath(MinecraftServer ms) {

		return FileHelper.getWorldPlayListDataPath(ms).resolve(getFolder()).resolve(getName());
	}

	public String getUUID() {
		return uuid;
	}
}
