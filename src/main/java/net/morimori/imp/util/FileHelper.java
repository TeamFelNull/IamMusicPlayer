package net.morimori.imp.util;

import java.nio.file.Path;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.morimori.imp.IkisugiMusicPlayer;

public class FileHelper {

	public static Path getWorldSaveDataPath(MinecraftServer ms) {
		return ms.getActiveAnvilConverter().getFile(ms.getFolderName(), "test").getParentFile().toPath();
	}

	public static Path getWorldDataPath(MinecraftServer ms) {
		return getWorldSaveDataPath(ms).resolve(IkisugiMusicPlayer.MODID);
	}

	public static Path getWorldPlayListNBTDataPath(MinecraftServer ms) {
		return getWorldDataPath(ms).resolve("playlist.dat");
	}

	public static Path getWorldPlayListDataPath(MinecraftServer ms) {
		return getWorldDataPath(ms).resolve("playlist");
	}

	public static Path getWorldEveryonePlayListDataPath(MinecraftServer ms) {
		return getWorldPlayListDataPath(ms).resolve("everyone");
	}

	public static Path getWorldPlayerPlayListDataPath(MinecraftServer ms, String uuid) {
		return getWorldPlayListDataPath(ms).resolve(uuid);
	}

	public static Path getWorldPlayerPlayListDataPath(PlayerEntity pl) {
		return getWorldPlayerPlayListDataPath(pl.getServer(), PlayerHelper.getUUID(pl));
	}

}
