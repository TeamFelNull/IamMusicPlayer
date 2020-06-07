package net.morimori.imp.sound;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.file.Path;

import com.mpatric.mp3agic.Mp3File;

import javazoom.jl.player.advanced.AdvancedPlayer;

public class SoundRinger extends Thread {
	protected AdvancedPlayer player;
	protected boolean ringd;
	protected boolean finished;
	protected int potisionframe;
	private String filepath;

	public SoundRinger() {

	}

	public SoundRinger(Path soundfilepath) {
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

	public boolean isRing() {
		return ringd;
	}

	public boolean isFinish() {
		return finished;
	}

	public void stopRing() {
		if (ringd && player != null)
			finishe();
	}

	public void startRing() {
		if (!ringd) {
			ringd = true;
			start();
		}
	}

	@Override
	public void run() {

		try {
			player = new AdvancedPlayer(new BufferedInputStream(new FileInputStream(filepath)));
			player.play(potisionframe, Integer.MAX_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finishe();
	}

	protected void finishe() {
		player.close();
		finished = true;
		ringd = false;
	}
}
