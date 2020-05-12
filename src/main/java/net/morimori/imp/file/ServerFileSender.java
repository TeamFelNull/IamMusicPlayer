package net.morimori.imp.file;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.resources.I18n;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.PacketDistributor;
import net.morimori.imp.IkisugiMusicPlayer;
import net.morimori.imp.packet.ClientStopRequestMessage;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.packet.ServerSendSoundFileMessage;
import net.morimori.imp.util.FileLoader;
import net.morimori.imp.util.NarratorHelper;
import net.morimori.imp.util.StringHelper;

public class ServerFileSender extends Thread {

	public static Map<String, Map<Integer, ServerFileSender>> senderBuffer = new HashMap<String, Map<Integer, ServerFileSender>>();
	public static Map<String, Map<Integer, Boolean>> responseWaits = new HashMap<String, Map<Integer, Boolean>>();
	public static Map<String, Map<Integer, Boolean>> stop = new HashMap<String, Map<Integer, Boolean>>();

	public static int bytespeed = 5000;

	private Path path;
	public String pluuid;
	public boolean downloaddolder;
	private MinecraftServer ms;
	public int id;

	public static int MaxSendCont = 5;

	public ServerFileSender(String uuid, int id, Path path, boolean downloaddolder, MinecraftServer mis) {
		this.path = path;
		this.downloaddolder = downloaddolder;
		this.pluuid = uuid;
		this.ms = mis;
		this.id = id;

	}

	public static boolean canSending(String uuid) {
		int sndid = -1;

		if (!senderBuffer.containsKey(uuid)) {
			senderBuffer.put(uuid, new HashMap<Integer, ServerFileSender>());
		}

		for (int c = 0; c < MaxSendCont; c++) {
			if (!senderBuffer.get(uuid).containsKey(c)) {
				sndid = c;
				break;
			}
		}

		return sndid != -1;
	}

	public static void stopSend(String uuid) {

		if (!senderBuffer.containsKey(uuid)) {
			senderBuffer.put(uuid, new HashMap<Integer, ServerFileSender>());
		}
		for (int c = 0; c < MaxSendCont; c++) {
			if (senderBuffer.get(uuid).containsKey(c)) {
				stopSend(uuid, c);
			}
		}
	}

	public static void stopSend(String uuid, int id) {
		if (!stop.containsKey(uuid)) {
			stop.put(uuid, new HashMap<Integer, Boolean>());
		}
		stop.get(uuid).put(id, true);

	}

	public static void startSender(String uuid, Path path, boolean downloaddolder, MinecraftServer ms) {
		int sndid = -1;
		if (!senderBuffer.containsKey(uuid)) {
			senderBuffer.put(uuid, new HashMap<Integer, ServerFileSender>());
		}

		if (!responseWaits.containsKey(uuid)) {
			responseWaits.put(uuid, new HashMap<Integer, Boolean>());
		}

		if (!stop.containsKey(uuid)) {
			stop.put(uuid, new HashMap<Integer, Boolean>());
		}
		for (int c = 0; c < MaxSendCont; c++) {
			if (!senderBuffer.get(uuid).containsKey(c)) {
				sndid = c;
				break;
			}
		}
		if (sndid != -1) {
			stop.get(uuid).put(sndid, false);
			ServerFileSender SFS = new ServerFileSender(uuid, sndid, path, downloaddolder, ms);
			SFS.start();
		}

	}

	public void run() {

		senderBuffer.get(pluuid).put(id, this);
		long fristtime = System.currentTimeMillis();
		long logtime = System.currentTimeMillis();
		byte[] bytes = FileLoader.fileBytesReader(this.path);
		boolean frist = true;
		long time = System.currentTimeMillis();
		if (bytes == null) {
			IkisugiMusicPlayer.LOGGER.info(
					"Null Sender File : " + this.path.toFile().toString());
			finishSend();
			return;
		}

		int cont = 0;

		IkisugiMusicPlayer.LOGGER.info("Server File Sender Start : "
				+ ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid)).getDisplayName()
						.getString()
				+ " Name "
				+ this.path.toFile().getName() + " Size "
				+ StringHelper.fileCapacityNotation(this.path.toFile().length()));

		for (int i = 0; i < bytes.length; i += bytespeed) {
			byte[] bi = new byte[bytes.length - i >= bytespeed ? bytespeed : bytes.length - i];
			for (int c = 0; c < bytespeed; c++) {
				if ((i + c) < bytes.length) {
					bi[c] = bytes[i + c];
					cont++;
				}
			}
			responseWaits.get(pluuid).put(id, true);
			PacketHandler.INSTANCE.send(
					PacketDistributor.PLAYER.with(() -> ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid))),
					new ServerSendSoundFileMessage(bi, this.id, frist, bytes.length, this.path.toString(),
							downloaddolder, PlayList.getWorldPlaylistNBTDataString(ms, path, "UUID"),
							PlayList.getWorldPlaylistNBTDataSoundData(ms, path, "SoundData")));
			frist = false;

			try {

				while (responseWaits.get(pluuid).get(id)) {
					Thread.sleep(1);

					if (stop.get(pluuid).get(id)) {

						IkisugiMusicPlayer.LOGGER.error("Server File Sender Stop : Player "
								+ ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid)).getDisplayName()
										.getString()
								+ " Name "
								+ this.path.toFile().getName() + " Sent " + StringHelper.fileCapacityNotation(cont)
								+ " Elapsed "
								+ (System.currentTimeMillis() - fristtime)
								+ "ms");

						PacketHandler.INSTANCE.send(
								PacketDistributor.PLAYER
										.with(() -> ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid))),
								new ClientStopRequestMessage(id));
						finishSend();
						return;
					}

					if (System.currentTimeMillis() - time >= 10000) {
						IkisugiMusicPlayer.LOGGER.error("Client File Sender Time Out : Player "
								+ " Name "
								+ this.path.toFile().getName() + " Sent " + StringHelper.fileCapacityNotation(cont)
								+ " Elapsed "
								+ (System.currentTimeMillis() - fristtime)
								+ "ms");
						NarratorHelper.say(I18n.format("narrator.fileuploadtimeout", this.path.toFile().getName()));
						finishSend();
						return;
					}

					if (System.currentTimeMillis() - logtime >= 5000) {
						logtime = System.currentTimeMillis();
						IkisugiMusicPlayer.LOGGER.info("Server File Sending :"
								+ ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid)).getDisplayName()
										.getString()
								+ " Name "
								+ this.path.toFile().getName() + " Sent " + StringHelper.fileCapacityNotation(cont)
								+ " Elapsed " + (System.currentTimeMillis() - fristtime)
								+ "ms");

					}
				}

			} catch (InterruptedException e) {

			}
			time = System.currentTimeMillis();
		}

		IkisugiMusicPlayer.LOGGER.info("Server File Sender was Success Full :"
				+ ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid)).getDisplayName()
						.getString()
				+ " Name "
				+ this.path.toFile().getName() + " Size " + StringHelper.fileCapacityNotation(cont) + " Elapsed "
				+ (System.currentTimeMillis() - fristtime) + "ms");

		finishSend();
	}

	public void finishSend() {
		senderBuffer.get(pluuid).remove(id);

	}
}
