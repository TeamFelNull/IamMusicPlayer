package net.morimori.imp.sound;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javazoom.jl.player.advanced.AdvancedPlayer;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.packet.ServerSoundStreamMessage;

public class WorldSoundRinger extends SoundRinger {

	public static Map<String, List<byte[]>> bytebuf = new HashMap<String, List<byte[]>>();
	public static Map<String, Integer> leths = new HashMap<String, Integer>();
	public static Map<String, Boolean> stops = new HashMap<String, Boolean>();
	public static Map<String, Long> milisecs = new HashMap<String, Long>();
	public static Map<String, Float> bairitus = new HashMap<String, Float>();
	public static Map<String, Float> sbairitus = new HashMap<String, Float>();

	private WorldSoundKey wsk;
	private String key;
	private boolean stop;

	public WorldSoundRinger(WorldSoundKey ws) {
		this.wsk = ws;
		this.key = UUID.randomUUID().toString();
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
	public void run() {

		stops.put(key, false);

		if (!bytebuf.containsKey(key)) {
			bytebuf.put(key, new ArrayList<byte[]>());
		}
		leths.put(key, -1);
		milisecs.put(key, -1l);
		PacketHandler.INSTANCE.sendToServer(new ServerSoundStreamMessage(key, wsk, 0, false));

		int co = 0;
		while (!stop) {

			if (leths.get(key) != -1 && leths.get(key) == co) {
				break;
			}

			while (WorldSoundRinger.bytebuf.get(key).isEmpty()) {

				if (stops.get(key)) {
					finishe();
					return;
				}

				try {
					sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			SoundRingedThread srt = new SoundRingedThread(WorldSoundRinger.bytebuf.get(key).get(0), 0);
			srt.start();

			long sl = (long) (milisecs.get(key) * bairitus.get(key));

			if (leths.get(key) != co) {
				try {
					sleep(sl - 17);
				} catch (InterruptedException e) {
				}
			}

			if (WorldSoundRinger.bytebuf.containsKey(key))
				WorldSoundRinger.bytebuf.get(key).remove(0);

			co++;
		}

		finishe();
	}

	@Override
	protected void finishe() {
		WorldSoundRinger.bytebuf.remove(key);
		WorldSoundRinger.leths.remove(key);
		WorldSoundRinger.milisecs.remove(key);
		WorldSoundRinger.bairitus.remove(key);
		WorldSoundRinger.sbairitus.remove(key);

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

	public SoundRingedThread(byte[] bytes, int frame) {
		this.bytes = bytes;
		this.frame = frame;
	}

	public void stopr() {
		if (player != null) {
			player.close();
		}
	}

	public void run() {

		ByteArrayInputStream st = new ByteArrayInputStream(bytes);
		try {
			player = new AdvancedPlayer(new BufferedInputStream(st));
			player.play(frame, Integer.MAX_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		player.close();
	}
}
