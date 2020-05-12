package net.morimori.imp.util;

import java.io.File;
import java.nio.file.Path;

import net.minecraft.client.Minecraft;
import net.morimori.imp.IkisugiMusicPlayer;

public class ClientFileHelper {
	private static Minecraft mc = Minecraft.getInstance();

	public static Path getClientPlayFileDataPath() {
		return new File(IkisugiMusicPlayer.MODID).toPath().resolve("musics");
	}

	public static Path getClientServerSoundsDataPath() {
		return new File(IkisugiMusicPlayer.MODID).toPath().resolve("serversounddata");
	}

	public static Path getClientCurrentServerSoundDataPath() {
		return getClientServerSoundsDataPath().resolve(
				mc.isSingleplayer()
						? String.valueOf(IMPMath.stringDecimalConverter(mc.getIntegratedServer().getFolderName()))
						: String.valueOf(IMPMath.stringDecimalConverter(mc.getCurrentServerData().serverIP)));
	}

	public static Path getClientCurrentServerPlaylistPath() {
		return getClientCurrentServerSoundDataPath().resolve("playlist");
	}

	public static Path getClientCurrentServerPlaylistNBTDataPath() {
		return getClientCurrentServerSoundDataPath().resolve("playlist.dat");
	}

	public static Path getClientOptionTxtPath() {
		return new File(IkisugiMusicPlayer.MODID).toPath().resolve("options.txt");
	}
}
