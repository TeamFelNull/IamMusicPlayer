package net.morimori.imp.file;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.morimori.imp.IkisugiMusicPlayer;
import net.morimori.imp.registries.IMPRegistries;
import net.morimori.imp.sound.SoundData;
import net.morimori.imp.util.FileHelper;
import net.morimori.imp.util.FileLoader;

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
				dwonloadSound(ms, ruen.getValue(), new TranslationTextComponent(
						"music." + ruen.getKey().getNamespace() + "." + ruen.getKey().getPath()).getString(),
						ruen.getKey());
				IkisugiMusicPlayer.LOGGER.info("Dwonload Stating : " + ruen.getKey().toString());
			}

		}

		public void dwonloadSound(MinecraftServer ms, String url, String name, ResourceLocation rl) {

			try {
				BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
				FileLoader.fileBytesWriter(IOUtils.toByteArray(in),
						FileHelper.getWorldEveryonePlayListDataPath(ms).resolve(name + ".mp3"));

				sleep(10);

				PlayList.addPlayeyListFileNBT(ms, "everyone", name + ".mp3", rl.getNamespace(),
						new SoundData(FileHelper.getWorldEveryonePlayListDataPath(ms).resolve(name + ".mp3")));

			} catch (IOException | InterruptedException e) {
				IkisugiMusicPlayer.LOGGER.info("Dwonload Failure : " + url);
			}

		}
	}
}