package net.morimori.imp.file;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import net.morimori.imp.sound.ClientSoundPlayer;
import net.morimori.imp.sound.WorldPlayListSoundData;
import net.morimori.imp.sound.WorldSoundKey;
import net.morimori.imp.util.FileHelper;
import net.morimori.imp.util.FileLoader;

public class ClientFileReceiver extends Thread {

	public static Map<Integer, FileReceiverBuffer> receiverBufer = new HashMap<Integer, FileReceiverBuffer>();
	public static Map<Integer, Boolean> stop = new HashMap<Integer, Boolean>();
	public static boolean canReceiving;
	public String filename;
	public String path;
	public String sounduuid;

	public boolean downloaddolder;
	public int id;

	public ClientFileReceiver(int id, int bytecont, String filepath, boolean downloaddolder, String sounduuid) {
		stop.put(id, false);
		receiverBufer.put(id, new FileReceiverBuffer(bytecont, filepath));
		this.path = filepath;
		this.filename = Paths.get(filepath).toFile().getName();
		this.downloaddolder = downloaddolder;
		this.id = id;
		this.sounduuid = sounduuid;
	}

	public static boolean isReceving(File file) {

		return getReceivingId(file) != -1;
	}

	public static int getReceivingId(File file) {

		for (Entry<Integer, FileReceiverBuffer> t : receiverBufer.entrySet()) {
			if (t.getValue().filepath.equals(file.toString())) {
				return t.getKey();
			}
		}

		return -1;
	}

	public static void stopReceiver() {

		for (Entry<Integer, FileReceiverBuffer> rb : receiverBufer.entrySet()) {

			stopReceiver(rb.getKey());
		}

	}

	public static void stopReceiver(int id) {
		stop.put(id, true);
	}

	public static void addBufferBytes(int id, byte[] bytes) {
		receiverBufer.get(id).addBytes(bytes);
	}

	public void run() {
		int cont = receiverBufer.get(id).getCont();
		long time = System.currentTimeMillis();
		long logtime = System.currentTimeMillis();

		while (!receiverBufer.get(id).isPerfectByte()) {
			if (stop.get(id)) {

				finishReceiver();
				return;
			}

			if (cont == receiverBufer.get(id).getCont() && System.currentTimeMillis() - time >= 10000) {

				finishReceiver();
				return;
			}

			if (System.currentTimeMillis() - logtime >= 5000) {
				logtime = System.currentTimeMillis();

			}

			if (cont != receiverBufer.get(id).getCont()) {
				cont = receiverBufer.get(id).getCont();
				time = System.currentTimeMillis();
			}
		}
		if (downloaddolder) {
			String writefilename = filename;
			int namecont = 1;
			while (PlayList.isExistsClientPlaylistFile(writefilename)) {
				String[] filenames = filename.split(Pattern.quote("."));
				String writefilenamea = "";
				if (filenames.length != 1) {
					for (int c = 0; c < filenames.length - 1; c++) {

						if (c == 0) {
							writefilenamea += filenames[c];
						} else {
							writefilenamea += "." + filenames[c];
						}
					}
					writefilenamea += "(" + namecont + ")";
					writefilenamea += "." + filenames[filenames.length - 1];
				} else {
					writefilenamea += filename;
					writefilenamea += "(" + namecont + ")";
				}
				writefilename = writefilenamea;
				namecont++;
			}

			FileLoader.fileBytesWriter(receiverBufer.get(id).getBytes(),
					FileHelper.getClientPlayFileDataPath().resolve(writefilename));
		} else {
			FileLoader.fileBytesWriter(receiverBufer.get(id).getBytes(),
					FileHelper.getClientCurrentServerPlaylistPath()
							.resolve(Paths.get(path).getParent().toFile().getName()).resolve(filename));
			PlayList.addClientPlayeyListFileNBT(Paths.get(path).getParent().toFile().getName(), filename,
					sounduuid);
			ClientSoundPlayer.INSTANS
					.finishedDownload(new WorldSoundKey(new WorldPlayListSoundData(path, sounduuid, null)));
		}

		finishReceiver();
	}

	private void finishReceiver() {
		receiverBufer.remove(id);
	}
}
