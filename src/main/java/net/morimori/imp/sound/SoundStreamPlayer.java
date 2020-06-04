package net.morimori.imp.sound;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.Player;
import net.morimori.imp.IamMusicPlayer;

public class SoundStreamPlayer extends Thread {
	public Bitstream stream;
	public Player player;
	public AudioDevice device;

	public SoundStreamPlayer(String urns) {

		try {
			URL url = new URL(urns);

			player = new Player(url.openStream());
		} catch (MalformedURLException e) {

		} catch (JavaLayerException e) {

		} catch (IOException e) {

		}

	}

	public void startSound() {
		this.start();
	}

	public void run() {
		if (player != null) {
			try {
				player.play();
			} catch (JavaLayerException e) {
				IamMusicPlayer.LOGGER.error("Could not play Sound");
			}

		}
	}
}
