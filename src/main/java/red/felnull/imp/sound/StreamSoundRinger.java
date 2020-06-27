package red.felnull.imp.sound;

import java.net.URL;

import javazoom.jl.player.advanced.AdvancedPlayer;

public class StreamSoundRinger extends SoundRinger {
	private String url;

	public StreamSoundRinger(String url) {
		this.url = url;
	}

	@Override
	public void setPotision(long posionmilisec) {

	}

	@Override
	public void run() {

		try {
			URL aurl = new URL(url);
			player = new AdvancedPlayer(aurl.openStream());
			player.play(potisionframe, Integer.MAX_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finishe();
	}

}
