package net.morimori.imp.util;

import java.io.File;
import java.nio.file.Path;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

	public static Path getWorldPictuerPath(MinecraftServer ms) {
		return getWorldDataPath(ms).resolve("pictuer");
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

	@OnlyIn(Dist.CLIENT)
	public static Path getClientPlayFileDataPath() {
		return new File(IkisugiMusicPlayer.MODID).toPath().resolve("musics");
	}

	@OnlyIn(Dist.CLIENT)
	public static Path getClientCashPath() {
		return new File(IkisugiMusicPlayer.MODID).toPath().resolve("cash");
	}

	@OnlyIn(Dist.CLIENT)
	public static Path getClientCurrentServerSoundDataPath() {
		Minecraft mc = IkisugiMusicPlayer.proxy.getMinecraft();
		return getClientSoundCashPath().resolve(
				mc.isSingleplayer()
						? String.valueOf(IMPMath.stringDecimalConverter(mc.getIntegratedServer().getFolderName()))
						: String.valueOf(IMPMath.stringDecimalConverter(mc.getCurrentServerData().serverIP)));
	}

	@OnlyIn(Dist.CLIENT)
	public static Path getClientCurrentServerPlaylistPath() {
		return getClientCurrentServerSoundDataPath().resolve("playlist");
	}

	@OnlyIn(Dist.CLIENT)
	public static Path getClientCurrentServerPlaylistNBTDataPath() {
		return getClientCurrentServerSoundDataPath().resolve("playlist.dat");
	}

	@OnlyIn(Dist.CLIENT)
	public static Path getClientOptionTxtPath() {
		return new File(IkisugiMusicPlayer.MODID).toPath().resolve("options.txt");
	}

	@OnlyIn(Dist.CLIENT)
	public static Path getClientSoundCashPath() {
		return getClientCashPath().resolve("sound");
	}

	@OnlyIn(Dist.CLIENT)
	public static Path getClientPictuerCashPath() {
		return getClientCashPath().resolve("pictuer");
	}
}
