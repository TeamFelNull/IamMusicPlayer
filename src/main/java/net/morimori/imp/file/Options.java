package net.morimori.imp.file;

import java.util.HashMap;

import net.morimori.imp.sound.SoundWaitThread;
import net.morimori.imp.util.ClientFileHelper;
import net.morimori.imp.util.FileLoader;

public class Options {

	public static void loadOption() {
		HashMap<String, String> opmap = new HashMap<String, String>();
		FileLoader.txtReader(opmap, ClientFileHelper.getClientOptionTxtPath());
		if (opmap.isEmpty()) {
			opmap.put("soundCategory_ikisugimusicplayer", "1.0");
			FileLoader.txtWriter(opmap, ClientFileHelper.getClientOptionTxtPath());
		}
		SoundWaitThread.AllSoundVolume = Float.valueOf(opmap.get("soundCategory_ikisugimusicplayer"));
	}

	public static void writeOption(boolean tread) {
		if (!tread) {
			HashMap<String, String> opmap = new HashMap<String, String>();
			opmap.put("soundCategory_ikisugimusicplayer", String.valueOf(SoundWaitThread.AllSoundVolume));
			FileLoader.txtWriter(opmap, ClientFileHelper.getClientOptionTxtPath());
		} else {
			WriteThread wt = new WriteThread();
			wt.start();
		}
	}
}

class WriteThread extends Thread {
	public void run() {
		Options.writeOption(false);
	}
}
