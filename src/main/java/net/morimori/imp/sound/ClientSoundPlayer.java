package net.morimori.imp.sound;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.morimori.imp.client.handler.RenderHandler;

public class ClientSoundPlayer {
	public static ClientSoundPlayer INSTANS;

	public Map<String, SoundRinger> ringSounds = new HashMap<String, SoundRinger>();

	public void tick() {
		try {
			ringtick();
		} catch (Exception e) {
			RenderHandler.expations.put("Exception : " + e, 100);
		}
	}

	public void addRingSound(String key, SoundRinger sr) {
		if (!ringSounds.containsKey(key)) {
			ringSounds.put(key, sr);
		}
	}

	public void removeRingSound(String key) {
		if (ringSounds.containsKey(key)) {
			ringSounds.get(key).stopRing();
			ringSounds.remove(key);
		}
	}

	private void ringtick() {

		for (Entry<String, SoundRinger> rs : ringSounds.entrySet()) {
			SoundRinger sr = rs.getValue();

			if (sr.isFinish())
				ringSounds.remove(rs.getKey());

			if (!sr.isRing() && !sr.isFinish())
				sr.startRing();

		}
	}

}
