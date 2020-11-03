package net.morimori.imp.sound;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mpatric.mp3agic.Mp3File;

import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;
import net.minecraft.client.Minecraft;

public class SoundRinger extends Thread {
	private static Minecraft mc = Minecraft.getInstance();
	public static Map<String, AdvancedPlayer> players = new HashMap<String, AdvancedPlayer>();
	protected AdvancedPlayer player;
	protected boolean ringd;
	protected boolean finished;
	protected int potisionframe;
	private String filepath;
	private float voli;
	private boolean stop;

	public SoundRinger() {

	}

	public SoundRinger(Path soundfilepath) {
		voli = 0;
		this.filepath = soundfilepath.toString();
	}

	public void setPotision(long posionmilisec) {
		try {
			Mp3File mfile = new Mp3File(filepath);
			long framesc = mfile.getLengthInMilliseconds() / mfile.getFrameCount();
			int frame = (int) (posionmilisec / framesc);
			potisionframe = frame;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setVolume(float vol) {
		this.voli = vol;
	}

	public boolean isRing() {
		return ringd;
	}

	public boolean isFinish() {
		return finished;
	}

	public void stopRing() {
		stop = true;
	}

	public void startRing() {
		if (!ringd) {
			ringd = true;
			start();
		}
	}

	@Override
	public void run() {

		PlayThread pt = new PlayThread(filepath, potisionframe);
		pt.start();

		while (!pt.isFinish()) {

			pt.setVolume(voli);
			if (stop || mc.player == null) {
				pt.stopS();
			}
			try {
				sleep(1);
			} catch (InterruptedException e) {
			}

		}

		finishe();
	}

	protected void finishe() {
		finished = true;
		ringd = false;
	}
}

class PlayThread extends Thread {

	private String uuid;
	private boolean finish;
	private int posfalrme;
	private boolean stop;
	private String path;

	public PlayThread(String filepath, int flame) {
		this.posfalrme = flame;
		this.path = filepath;
		this.uuid = UUID.randomUUID().toString();
	}

	public void stopS() {

		if (SoundRinger.players.containsKey(uuid)) {
			SoundRinger.players.get(uuid).close();
			SoundRinger.players.remove(uuid);
		}

		finish = true;
		stop = true;

	}

	public void setVolume(float vol) {
		if (SoundRinger.players.containsKey(uuid)) {
			AudioDevice dev = SoundRinger.players.get(uuid).audio;
			if (dev instanceof JavaSoundAudioDevice) {
				float v = -60 + 60 * vol;
				((JavaSoundAudioDevice) dev).setVolume(v);
			}
		}
	}

	public boolean isFinish() {
		return finish;
	}

	public void run() {
		try {
			sleep(100);

			SoundRinger.players.put(uuid, new AdvancedPlayer(new BufferedInputStream(new FileInputStream(path))));

			SoundRinger.players.get(uuid).play(posfalrme, Integer.MAX_VALUE);
		} catch (Exception e) {
		}
		if (!stop) {
			if (SoundRinger.players.containsKey(uuid)) {
				SoundRinger.players.get(uuid).close();
				SoundRinger.players.remove(uuid);
			}
			finish = true;

		}
	}
}
