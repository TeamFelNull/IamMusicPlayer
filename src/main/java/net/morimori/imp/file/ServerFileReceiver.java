package net.morimori.imp.file;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.server.MinecraftServer;
import net.morimori.imp.IkisugiMusicPlayer;
import net.morimori.imp.sound.SoundData;
import net.morimori.imp.util.FileHelper;
import net.morimori.imp.util.FileLoader;
import net.morimori.imp.util.StringHelper;

public class ServerFileReceiver extends Thread {

	public static Map<String, FileReceiverBuffer> receiverBufer = new HashMap<String, FileReceiverBuffer>();
	public static Map<String, Boolean> stop = new HashMap<String, Boolean>();

	private String pluuid;
	private MinecraftServer ms;
	private String filename;
	private boolean playerfile;
	private SoundData sd;

	public ServerFileReceiver(String uuid, int bytecont, String filename, MinecraftServer msi, boolean isPlayerFile,
			SoundData sd) {
		receiverBufer.put(uuid, new FileReceiverBuffer(bytecont, filename));
		stop.put(uuid, false);
		pluuid = uuid;
		this.filename = filename;
		this.ms = msi;
		this.playerfile = isPlayerFile;
		this.sd = sd;
	}

	public static void stopReceiver(String uuid) {
		stop.put(uuid, true);
	}

	public static void addBufferBytes(String uuid, byte[] bytes) {
		receiverBufer.get(uuid).addBytes(bytes);
	}

	public void run() {
		long time = System.currentTimeMillis();
		int cont = receiverBufer.get(pluuid).getCont();
		long logtime = System.currentTimeMillis();
		long fristtime = System.currentTimeMillis();

		IkisugiMusicPlayer.LOGGER.info("Server File Receiver Start : "
				+ ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid)).getDisplayName().getString() + " Name "
				+ filename + " Size " + StringHelper.fileCapacityNotation(receiverBufer.get(pluuid).getBytes().length)
				+ " Target "
				+ (playerfile ? "Main" : "Everyone"));

		while (!receiverBufer.get(pluuid).isPerfectByte()) {

			if (stop.get(pluuid)) {
				IkisugiMusicPlayer.LOGGER.error("Server File Receiver Stop : Player "
						+ ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid)).getDisplayName().getString()
						+ " Name "
						+ filename + " Sent " + StringHelper.fileCapacityNotation(cont) + " Target "
						+ (playerfile ? "Main" : "Everyone") + " Elapsed " + (System.currentTimeMillis() - fristtime)
						+ "ms");
				receiverBufer.remove(pluuid);
			}

			if (cont == receiverBufer.get(pluuid).getCont() && System.currentTimeMillis() - time >= 10000) {
				IkisugiMusicPlayer.LOGGER.error("Server File Receiver Time Out : Player "
						+ ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid)).getDisplayName().getString()
						+ " Name "
						+ filename + " Sent " + StringHelper.fileCapacityNotation(cont) + " Target "
						+ (playerfile ? "Main" : "Everyone") + " Elapsed " + (System.currentTimeMillis() - fristtime)
						+ "ms");
				receiverBufer.remove(pluuid);
				return;
			}
			if (System.currentTimeMillis() - logtime >= 5000) {
				logtime = System.currentTimeMillis();
				IkisugiMusicPlayer.LOGGER.info("Server File Receing :"
						+ ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid)).getDisplayName().getString()
						+ " Name "
						+ filename + " Sent " + StringHelper.fileCapacityNotation(cont) + " Target "
						+ (playerfile ? "Main" : "Everyone") + " Elapsed " + (System.currentTimeMillis() - fristtime)
						+ "ms");

			}
			if (cont != receiverBufer.get(pluuid).getCont()) {
				cont = receiverBufer.get(pluuid).getCont();
				time = System.currentTimeMillis();
			}
		}

		if (playerfile) {
			FileLoader.fileBytesWriter(receiverBufer.get(pluuid).getBytes(),
					FileHelper.getWorldPlayerPlayListDataPath(ms, pluuid).resolve(filename));
			PlayList.addPlayeyListFileNBT(ms, pluuid, filename, pluuid, sd);
		} else {
			FileLoader.fileBytesWriter(receiverBufer.get(pluuid).getBytes(),
					FileHelper.getWorldEveryonePlayListDataPath(ms).resolve(filename));
			PlayList.addPlayeyListFileNBT(ms, "everyone", filename, pluuid, sd);
		}
		IkisugiMusicPlayer.LOGGER.info("Server File Receiver was Success Full :  "
				+ ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid)).getDisplayName().getString() + " Name "
				+ filename + " Size " + StringHelper.fileCapacityNotation(cont) + " Target "
				+ (playerfile ? "Main" : "Everyone") + " Elapsed " + (System.currentTimeMillis() - fristtime) + "ms");
		receiverBufer.remove(pluuid);
	}
}
