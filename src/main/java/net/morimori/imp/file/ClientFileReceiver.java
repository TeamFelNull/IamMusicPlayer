package net.morimori.imp.file;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import net.minecraft.client.resources.I18n;
import net.morimori.imp.IkisugiMusicPlayer;
import net.morimori.imp.sound.SoundData;
import net.morimori.imp.sound.SoundWaitThread;
import net.morimori.imp.sound.WorldPlayListSoundData;
import net.morimori.imp.util.ClientFileHelper;
import net.morimori.imp.util.FileLoader;
import net.morimori.imp.util.NarratorHelper;
import net.morimori.imp.util.StringHelper;

public class ClientFileReceiver extends Thread {

	public static Map<Integer, FileReceiverBuffer> receiverBufer = new HashMap<Integer, FileReceiverBuffer>();
	public static Map<Integer, Boolean> stop = new HashMap<Integer, Boolean>();
	public static boolean canReceiving;
	public String filename;
	public String path;
	public String sounduuid;
	public SoundData sd;

	public boolean downloaddolder;
	public int id;

	public ClientFileReceiver(int id, int bytecont, String filepath, boolean downloaddolder, String sounduuid,
			SoundData sd) {
		stop.put(id, false);
		receiverBufer.put(id, new FileReceiverBuffer(bytecont, filepath));
		this.path = filepath;
		this.filename = Paths.get(filepath).toFile().getName();
		this.downloaddolder = downloaddolder;
		this.id = id;
		this.sounduuid = sounduuid;
		this.sd = sd;
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
		long fristtime = System.currentTimeMillis();
		long logtime = System.currentTimeMillis();
		IkisugiMusicPlayer.LOGGER.info("Client File Receiver Start : " + " Name "
				+ filename + " Size " + StringHelper.fileCapacityNotation(receiverBufer.get(id).getBytes().length));

		while (!receiverBufer.get(id).isPerfectByte()) {
			if (stop.get(id)) {
				IkisugiMusicPlayer.LOGGER.error("Client File Receiver Stop :"
						+ " Name "
						+ filename + " Sent " + StringHelper.fileCapacityNotation(cont)
						+ " Elapsed "
						+ (System.currentTimeMillis() - fristtime)
						+ "ms " + receiverBufer.get(id).getPrograsePar());
				NarratorHelper.say(I18n.format("narrator.filedownloadstop", filename));
				finishReceiver();
				return;
			}

			if (cont == receiverBufer.get(id).getCont() && System.currentTimeMillis() - time >= 10000) {
				IkisugiMusicPlayer.LOGGER.error("Client File Receiver Time Out :"
						+ " Name "
						+ filename + " Sent " + StringHelper.fileCapacityNotation(cont) + " Elapsed "
						+ (System.currentTimeMillis() - fristtime)
						+ "ms");
				NarratorHelper.say(I18n.format("narratorfiledownloadtimeout", filename));
				finishReceiver();
				return;
			}

			if (System.currentTimeMillis() - logtime >= 5000) {
				logtime = System.currentTimeMillis();
				IkisugiMusicPlayer.LOGGER.info("Client File Receing :"
						+ " Name "
						+ filename + " Sent " + StringHelper.fileCapacityNotation(cont) + " Elapsed "
						+ (System.currentTimeMillis() - fristtime)
						+ "ms " + receiverBufer.get(id).getPrograsePar());
				NarratorHelper
						.say(I18n.format("narrator.filedownload", filename, receiverBufer.get(id).getPrograsePar()));
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
					ClientFileHelper.getClientPlayFileDataPath().resolve(writefilename));
		} else {
			FileLoader.fileBytesWriter(receiverBufer.get(id).getBytes(),
					ClientFileHelper.getClientCurrentServerPlaylistPath()
							.resolve(Paths.get(path).getParent().toFile().getName()).resolve(filename));
			PlayList.addClientPlayeyListFileNBT(Paths.get(path).getParent().toFile().getName(), filename,
					sounduuid);
			SoundWaitThread.finishedDownload(new WorldPlayListSoundData(path, sounduuid, sd));
		}

		IkisugiMusicPlayer.LOGGER.info("Client File Receiver was Success Full :" + " Name "
				+ filename + " Size " + StringHelper.fileCapacityNotation(cont) + " Elapsed "
				+ (System.currentTimeMillis() - fristtime) + "ms");
		NarratorHelper.say(I18n.format("narrator.filedownloadsuccess", filename));

		finishReceiver();
	}

	private void finishReceiver() {
		receiverBufer.remove(id);
	}
}
