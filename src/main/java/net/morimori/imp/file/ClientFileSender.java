package net.morimori.imp.file;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.morimori.imp.IamMusicPlayer;
import net.morimori.imp.config.CommonConfig;
import net.morimori.imp.packet.ClientSendSoundFileMessage;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.util.FileLoader;
import net.morimori.imp.util.NarratorHelper;
import net.morimori.imp.util.StringHelper;

public class ClientFileSender extends Thread {

	private static Minecraft mc = Minecraft.getInstance();
	public int id;

	public static Map<Integer, ClientFileSender> senderBuffer = new HashMap<Integer, ClientFileSender>();
	public static Map<Integer, Boolean> responseWaits = new HashMap<Integer, Boolean>();
	public static Map<Integer, Boolean> stops = new HashMap<Integer, Boolean>();
	public static Map<Integer, Integer> sendingprograses = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> sendingalls = new HashMap<Integer, Integer>();
	public static Map<Path, Boolean> reservationSenders = new HashMap<Path, Boolean>();

	public static int MaxSendCont = 5;

	public Path path;
	public boolean playerfile;

	public ClientFileSender(int id, Path path, boolean playerfile) {
		this.path = path;
		this.playerfile = playerfile;
		this.id = id;
	}

	public static boolean stopResevaionOrSending(Path path) {

		if (isSending(path)) {
			stopSend(ClientFileSender.getId(path));
			return true;
		}

		if (reservationSenders.containsKey(path)) {
			reservationSenders.remove(path);
		}

		return false;
	}

	public static boolean isResevationOrSending(Path path) {
		boolean resevation = reservationSenders.containsKey(path);
		boolean downloading = isSending(path);
		return resevation || downloading;
	}

	public static void reservationDownloading() {

		int sndid = -1;

		if (mc.player == null)
			return;

		for (int c = 0; c < MaxSendCont; c++) {
			if (!senderBuffer.containsKey(c)) {
				sndid = c;
				break;
			}
		}

		if (sndid == -1 || reservationSenders.isEmpty())
			return;

		for (Entry<Path, Boolean> pathes : reservationSenders.entrySet()) {
			startSender(pathes.getKey(), pathes.getValue());
			reservationSenders.remove(pathes.getKey());
			break;
		}
	}

	public static void addSenderReservation(Path path, boolean playerfile) {
		reservationSenders.put(path, playerfile);
	}

	public static void startSender(Path path, boolean playerfile) {
		int sndid = -1;

		if (mc.player == null)
			return;

		for (int c = 0; c < MaxSendCont; c++) {
			if (!senderBuffer.containsKey(c)) {
				sndid = c;
				break;
			}
		}
		if (sndid != -1) {
			stops.put(sndid, false);
			sendingprograses.put(sndid, 0);
			ClientFileSender SFS = new ClientFileSender(sndid, path, playerfile);
			SFS.start();
		}
	}

	public static void stopSend() {

		for (int c = 0; c < MaxSendCont; c++) {
			if (senderBuffer.containsKey(c)) {
				stopSend(c);
			}
		}
	}

	public static void stopSend(int id) {

		if (id == -1)
			return;

		stops.put(id, true);
	}

	public static String getPrograsePar(int id) {
		int sendingprograse = sendingprograses.get(id);
		int sendingall = sendingalls.get(id);

		return Math.round(((float) sendingprograse / (float) sendingall) * 100) + " %";
	}

	public static String getPrograsePar(Path path) {

		if (sendingprograses.isEmpty() || sendingalls.isEmpty()) {
			return "None";
		}
		int num = -1;

		for (Entry<Integer, ClientFileSender> sets : senderBuffer.entrySet()) {
			if (sets.getValue().path.equals(path)) {
				num = sets.getKey();
				break;
			}
		}

		if (num == -1)
			return "None";

		int sendingprograse = sendingprograses.get(num);
		int sendingall = sendingalls.get(num);

		return Math.round(((float) sendingprograse / (float) sendingall) * 100) + " %";
	}

	public static boolean isSending(Path path) {
		for (Entry<Integer, ClientFileSender> sets : senderBuffer.entrySet()) {
			if (sets.getValue().path.equals(path)) {
				return true;
			}
		}
		return false;
	}

	public static int getId(Path path) {
		int num = -1;
		for (Entry<Integer, ClientFileSender> sets : senderBuffer.entrySet()) {
			if (sets.getValue().path.equals(path)) {
				num = sets.getKey();
				break;
			}
		}
		return num;
	}

	public void run() {

		senderBuffer.put(id, this);
		byte[] bytes = FileLoader.fileBytesReader(this.path);
		boolean frist = true;
		long fristtime = System.currentTimeMillis();
		long logtime = System.currentTimeMillis();
		long time = System.currentTimeMillis();
		if (bytes == null) {
			IamMusicPlayer.LOGGER.info("Null Sender File : " + this.path.toFile().toString());
			finishSend();
			return;
		}
		int cont = 0;

		IamMusicPlayer.LOGGER.info("Client File Sender Start : " + " Name "
				+ this.path.toFile().getName() + " Size "
				+ StringHelper.fileCapacityNotation(this.path.toFile().length())
				+ " Target " + (playerfile ? "Main" : "Everyone"));

		for (int i = 0; i < bytes.length; i += CommonConfig.SEND_BYTE.get()) {
			byte[] bi = new byte[bytes.length - i >= CommonConfig.SEND_BYTE.get() ? CommonConfig.SEND_BYTE.get()
					: bytes.length - i];
			for (int c = 0; c < CommonConfig.SEND_BYTE.get(); c++) {
				if ((i + c) < bytes.length) {
					bi[c] = bytes[i + c];
					cont++;
				}
			}
			responseWaits.put(id, true);
			try {

				if (mc.player == null) {
					finishSend();
					return;
				}

				PacketHandler.INSTANCE
						.sendToServer(
								new ClientSendSoundFileMessage(bi, frist, bytes.length, this.path.toFile().getName(),
										playerfile, id));
				frist = false;
				sendingprograses.put(id, cont);
				sendingalls.put(id, bytes.length);

				while (responseWaits.get(id)) {

					if (stops.get(id)) {
						IamMusicPlayer.LOGGER.error("Client File Sending Stop : Player "
								+ " Name "
								+ this.path.toFile().getName() + " Sent " + StringHelper.fileCapacityNotation(cont)
								+ " Target "
								+ (playerfile ? "Main" : "Everyone") + " Elapsed "
								+ (System.currentTimeMillis() - fristtime)
								+ "ms " + getPrograsePar(id));
						NarratorHelper.say(I18n.format("narrator.fileuploadstop", this.path.toFile().getName()));
						finishSend();
						return;
					}

					Thread.sleep(1);

					if (System.currentTimeMillis() - time >= 10000) {
						IamMusicPlayer.LOGGER.error("Client File Sender Time Out : Player "
								+ " Name "
								+ this.path.toFile().getName() + " Sent " + StringHelper.fileCapacityNotation(cont)
								+ " Target "
								+ (playerfile ? "Main" : "Everyone") + " Elapsed "
								+ (System.currentTimeMillis() - fristtime)
								+ "ms " + getPrograsePar(id));
						NarratorHelper.say(I18n.format("narrator.fileuploadtimeout", this.path.toFile().getName()));
						finishSend();
						return;
					}

				}
			} catch (InterruptedException e) {

			}

			if (System.currentTimeMillis() - logtime >= 5000) {

				logtime = System.currentTimeMillis();
				IamMusicPlayer.LOGGER.info("Client File Sending :" + " Name " + this.path.toFile().getName()
						+ " Sent " + StringHelper.fileCapacityNotation(cont) + " Target "
						+ (playerfile ? "Main" : "Everyone") + " Elapsed "
						+ (System.currentTimeMillis() - fristtime) + "ms " + getPrograsePar(id));
				NarratorHelper
						.say(I18n.format("narrator.fileupload", this.path.toFile().getName(), getPrograsePar(id)));
			}
			time = System.currentTimeMillis();
		}

		IamMusicPlayer.LOGGER.info("Client File Sender was Success Full :" + " Name "
				+ this.path.toFile().getName() + " Size " + StringHelper.fileCapacityNotation(cont) + " Target "
				+ (playerfile ? "Main" : "Everyone") + " Elapsed " + (System.currentTimeMillis() - fristtime) + "ms");
		NarratorHelper.say(I18n.format("narrator.fileuploadsuccess", this.path.toFile().getName()));
		finishSend();
	}

	public void finishSend() {
		senderBuffer.remove(id);
		sendingprograses.put(id, 0);
		sendingalls.put(id, 0);
		stops.put(id, false);
	}

}
