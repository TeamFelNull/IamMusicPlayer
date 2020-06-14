package net.morimori.imp.sound;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;

import com.mpatric.mp3agic.Mp3File;

import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;
import net.minecraft.client.Minecraft;

public class SoundRinger extends Thread {
	private static Minecraft mc = Minecraft.getInstance();
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

	private boolean finish;
	protected AdvancedPlayer player;
	private int posfalrme;
	private boolean stop;
	private BufferedInputStream bis;
	private FileInputStream fus;

	public PlayThread(String filepath, int flame) {
		this.posfalrme = flame;
		try {
			this.fus = new FileInputStream(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.bis = new BufferedInputStream(fus);

	}

	public void stopS() {
		player.close();
		finish = true;
		stop = true;
		player = null;
		try {
			fus.close();
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setVolume(float vol) {
		if (player != null) {
			AudioDevice dev = player.audio;
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

			player = new AdvancedPlayer(bis);
			player.play(posfalrme, Integer.MAX_VALUE);
		} catch (Exception e) {
		}
		if (!stop) {
			player.close();
			finish = true;
			player = null;
			try {
				fus.close();
				bis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
