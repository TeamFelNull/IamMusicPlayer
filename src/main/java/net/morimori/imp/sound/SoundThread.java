package net.morimori.imp.sound;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;
import net.morimori.imp.IkisugiMusicPlayer;

public class SoundThread extends Thread {
	protected AdvancedPlayer player;
	public boolean finish;

	public SoundThread(String streamfilename) {

		try {
			player = new AdvancedPlayer(new BufferedInputStream(new FileInputStream(streamfilename)));
		} catch (FileNotFoundException | JavaLayerException e) {

		}

	}

	public void stopSound() {
		finishSound();
	}

	private void finishSound() {
		if (player != null) {
			player.close();
			finish = true;
		}
	}

	public void setVolume(float vol) {
		if (player != null) {

			float v = -50 + 50 * vol;

			AudioDevice dev = player.audio;
			if (dev instanceof JavaSoundAudioDevice) {
				((JavaSoundAudioDevice) dev).setVolume(v);
			}
		}
	}

	@Override
	public void run() {
		if (player != null) {
			try {
				finish = false;
				player.play();

			} catch (JavaLayerException e) {
				IkisugiMusicPlayer.LOGGER.error("Could not play Sound");
			}
			finishSound();
		}
	}
}
