package net.morimori.imp.sound;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.packet.ServerSoundStreamMessage;

public class WorldSoundRinger extends SoundRinger {

	public static Map<String, List<byte[]>> bytebuf = new HashMap<String, List<byte[]>>();
	public static Map<String, Integer> leths = new HashMap<String, Integer>();
	public static Map<String, Boolean> stops = new HashMap<String, Boolean>();

	private WorldSoundKey wsk;
	private String key;
	private boolean stop;
	private long posmi;

	private float vol;

	public WorldSoundRinger(WorldSoundKey ws) {
		this.wsk = ws;
		this.key = UUID.randomUUID().toString();
		this.vol = 0;
	}

	@Override
	public void setPotision(long posionmilisec) {
		this.posmi = posionmilisec;
	}

	@Override
	public void stopRing() {
		stop = true;
		if (ringd && player != null) {
			player.close();
			finishe();
		}
	}

	@Override
	public void setVolume(float vol) {
		this.vol = vol;
	}

	@Override
	public void run() {

		stops.put(key, false);

		if (!bytebuf.containsKey(key)) {
			bytebuf.put(key, new ArrayList<byte[]>());
		}
		leths.put(key, -1);
		PacketHandler.INSTANCE.sendToServer(new ServerSoundStreamMessage(key, wsk, posmi, false));

		int co = 0;
		while (!stop) {

			if (leths.get(key) != -1 && leths.get(key) == co) {
				break;
			}

			while (true) {

				if (stops.get(key) || stop) {
					finishe();
					return;
				}

				if (!WorldSoundRinger.bytebuf.get(key).isEmpty()) {
					break;
				}

				try {
					sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

			SoundRingedThread srt = new SoundRingedThread(WorldSoundRinger.bytebuf.get(key).get(0), 0);
			srt.start();
			srt.setVolume(0);

			while (!srt.finish) {

				srt.setVolume(vol);

				if (stops.get(key) || stop) {
					srt.stopr();
					finishe();
					return;
				}

				try {
					sleep(1);
				} catch (InterruptedException e) {
				}

			}

			co += WorldSoundRinger.bytebuf.get(key).get(0).length;

			if (WorldSoundRinger.bytebuf.containsKey(key))
				WorldSoundRinger.bytebuf.get(key).remove(0);

		}

		finishe();
	}

	@Override
	protected void finishe() {
		WorldSoundRinger.bytebuf.remove(key);
		WorldSoundRinger.leths.remove(key);
		PacketHandler.INSTANCE.sendToServer(new ServerSoundStreamMessage(key, wsk, 0, true));
		finished = true;
		ringd = false;
		stops.remove(key);
	}
}

class SoundRingedThread extends Thread {
	private byte[] bytes;
	private int frame;
	private AdvancedPlayer player;
	public boolean finish;

	public SoundRingedThread(byte[] bytes, int frame) {
		this.bytes = bytes;
		this.frame = frame;

	}

	public void stopr() {
		if (player != null) {
			player.close();
			finish = true;
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

	public void run() {

		ByteArrayInputStream st = new ByteArrayInputStream(bytes);
		try {
			player = new AdvancedPlayer(new BufferedInputStream(st));
			player.play(frame, Integer.MAX_VALUE);
		} catch (Exception e) {

		}
		player.close();
		finish = true;
	}
}
