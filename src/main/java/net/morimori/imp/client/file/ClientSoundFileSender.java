package net.morimori.imp.client.file;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.morimori.imp.IamMusicPlayer;
import net.morimori.imp.config.CommonConfig;
import net.morimori.imp.packet.ClientSendSoundFileMessage;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.util.FileHelper;
import net.morimori.imp.util.FileLoader;

public class ClientSoundFileSender extends Thread {
	private static Map<String, ClientSoundFileSender> SENDS = new HashMap<String, ClientSoundFileSender>();
	private static Map<String, SendFolderType> RESERVATIONS = new HashMap<String, SendFolderType>();
	public static int max = 5;
	private static Minecraft mc = Minecraft.getInstance();
	private String name;
	private SendFolderType type;

	private byte[] soundbyte;
	public int cont;
	public boolean response = false;
	private boolean stop;

	public ClientSoundFileSender(String name, SendFolderType type) {
		this.name = name;
		this.type = type;
	}

	public void sentFinish() {
		soundbyte = null;
		SENDS.remove(name);
	}

	public void sendStart() {
		if (SENDS.size() >= max) {
			IamMusicPlayer.LOGGER
					.error("Cannot send because the number of simultaneous uploads is limited : " + this.name);
		}
		SENDS.put(name, this);
		this.start();
	}

	public void run() {
		soundbyte = FileLoader.fileBytesReader(FileHelper.getClientPlayFileDataPath().resolve(name));
		if (soundbyte == null) {
			IamMusicPlayer.LOGGER.info(
					"Null Sender File : " + FileHelper.getClientPlayFileDataPath().resolve(name).toFile().toString());
			sentFinish();
		}
		boolean frist = true;
		long time = System.currentTimeMillis();
		int sendbyte = CommonConfig.SEND_BYTE.get();
		int soundbytelengt = soundbyte.length;
		for (int i = 0; i < soundbyte.length; i += sendbyte) {

			if (mc.player == null || stop) {
				sentFinish();
				return;
			}

			byte[] sndingbyte = new byte[soundbytelengt - i >= sendbyte ? sendbyte : soundbytelengt - i];

			for (int c = 0; c < CommonConfig.SEND_BYTE.get(); c++) {
				if ((i + c) < soundbytelengt) {
					sndingbyte[c] = soundbyte[i + c];
					cont++;
				}
			}
			ClientSendSoundFileMessage cssfm = null;
			if (frist) {
				cssfm = new ClientSendSoundFileMessage(sndingbyte, true, soundbyte.length, name, type);
			} else {
				cssfm = new ClientSendSoundFileMessage(sndingbyte, name);
			}
			PacketHandler.INSTANCE.sendToServer(cssfm);
			sndingbyte = null;
			frist = false;
			time = System.currentTimeMillis();
			while (!response) {
				if (mc.player == null || stop || System.currentTimeMillis() - time >= 10000) {
					sentFinish();
					return;
				}
				try {
					sleep(1);
				} catch (InterruptedException e) {
				}
			}
			response = false;
		}
		sentFinish();
	}

	public static void tick() {

	}

	public static boolean isSending(String name) {
		return SENDS.containsKey(name);
	}

	public static boolean isReservation(String name) {
		return RESERVATIONS.containsKey(name);
	}

	public static boolean isReservationOrSending(String name) {
		return isSending(name) || isReservation(name);
	}

	public static int getPrograses(String name) {
		if (!isSending(name))
			return 0;

		return SENDS.get(name).cont;
	}

	public static int getLength(String name) {
		if (!isSending(name) || SENDS.get(name).soundbyte == null)
			return 0;

		return SENDS.get(name).soundbyte.length;
	}

	public static void stopSender() {
		SENDS.entrySet().forEach(n -> stopSender(n.getKey()));
	}

	public static void stopSender(String name) {
		if (isSending(name)) {
			SENDS.get(name).stop = true;
			PacketHandler.INSTANCE.sendToServer(new ClientSendSoundFileMessage(name, true));
		}
	}

	public static void deleteReservation(String name) {
		if (isReservation(name)) {
			RESERVATIONS.remove(name);
		}
	}

	public static void deletReseOrStopSend(String name) {
		deleteReservation(name);
		stopSender(name);
	}

	public static void addReservation(String name, SendFolderType type) {
		RESERVATIONS.put(name, type);
	}

	public static void startSend(String name, SendFolderType type) {
		if (!FileHelper.getClientPlayFileDataPath().resolve(name).toFile().exists()) {
			IamMusicPlayer.LOGGER.error("No File : " + name);
			return;
		}
		ClientSoundFileSender csfs = new ClientSoundFileSender(name, type);
		csfs.sendStart();
	}

	public static void addReseOrStartSend(String name, SendFolderType type) {
		if (SENDS.size() >= max) {
			addReservation(name, type);
		} else {
			startSend(name, type);
		}
	}

	public static Map<String, SendFolderType> getReservations() {
		return RESERVATIONS;
	}

	public static Map<String, ClientSoundFileSender> getSender() {

		return SENDS;

	}

	public static ClientSoundFileSender getSender(String name) {
		if (SENDS.containsKey(name)) {
			return null;
		}
		return SENDS.get(name);

	}

	public static enum SendFolderType {
		MAIN, EVERYONE;
	}
}
