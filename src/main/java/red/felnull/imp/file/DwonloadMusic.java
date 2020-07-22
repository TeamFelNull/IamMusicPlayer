package red.felnull.imp.file;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.api.registries.IMPRegistries;
import red.felnull.imp.sound.SoundData;
import red.felnull.imp.util.FileHelper;
import red.felnull.imp.util.FileLoader;

public class DwonloadMusic {
	public static void dwonloadSoundFromWorldPlayLists(MinecraftServer ms) {
		DwonloadSoundFile dsf = new DwonloadSoundFile(ms);
		dsf.start();
	}

	static class DwonloadSoundFile extends Thread {

		private MinecraftServer ms;

		public DwonloadSoundFile(MinecraftServer ms) {
			this.ms = ms;
		}

		public void run() {
			dwonloadSoundFromWorldPlayLists(ms);
		}

		public void dwonloadSoundFromWorldPlayLists(MinecraftServer ms) {
			for (Entry<ResourceLocation, String> ruen : IMPRegistries.DwonloadMusics.entrySet()) {
				IamMusicPlayer.LOGGER.info("Dwonload Stating : " + ruen.getKey().toString());
				dwonloadSound(ms, ruen.getValue(), new TranslationTextComponent(
						"music." + ruen.getKey().getNamespace() + "." + ruen.getKey().getPath()).getString(),
						ruen.getKey());

			}

		}

		public void dwonloadSound(MinecraftServer ms, String url, String name, ResourceLocation rl) {

			try {
				BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
				FileLoader.fileBytesWriter(IOUtils.toByteArray(in),
						FileHelper.getWorldEveryonePlayListDataPath(ms).resolve(name + ".mp3"));

				String uuid = UUID.randomUUID().toString();
				ImagePictuers.addPictuer(uuid, FileHelper.getWorldEveryonePlayListDataPath(ms).resolve(name + ".mp3"),
						ms);
				PlayList.addPlayeyListFileNBT(ms, "everyone", name + ".mp3", rl.getNamespace(),
						new SoundData(FileHelper.getWorldEveryonePlayListDataPath(ms).resolve(name + ".mp3"), uuid));
				IamMusicPlayer.LOGGER.info("Dwonload  was Success Full  : " + rl.toString());
			} catch (IOException e) {
				IamMusicPlayer.LOGGER.info("Dwonload was Failure : " + url);
			}

		}
	}
}