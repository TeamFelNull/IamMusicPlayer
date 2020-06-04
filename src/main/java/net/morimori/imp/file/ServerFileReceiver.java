package net.morimori.imp.file;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.server.MinecraftServer;
import net.morimori.imp.IkisugiMusicPlayer;
import net.morimori.imp.sound.SoundData;
import net.morimori.imp.util.FileHelper;
import net.morimori.imp.util.FileLoader;
import net.morimori.imp.util.StringHelper;

public class ServerFileReceiver extends Thread {

	//	public static Map<String, FileReceiverBuffer> receiverBufer = new HashMap<String, FileReceiverBuffer>();
	public static Map<String, Map<Integer, FileReceiverBuffer>> receiverBufer = new HashMap<String, Map<Integer, FileReceiverBuffer>>();
	public static Map<String, Map<Integer, Boolean>> stop = new HashMap<String, Map<Integer, Boolean>>();
	private String pluuid;
	private MinecraftServer ms;
	private String filename;
	private boolean playerfile;
	int id;

	public ServerFileReceiver(String uuid, int bytecont, String filename, MinecraftServer msi, boolean isPlayerFile,
			int id) {

		if (!receiverBufer.containsKey(uuid)) {
			receiverBufer.put(uuid, new HashMap<Integer, FileReceiverBuffer>());
		}

		receiverBufer.get(uuid).put(id, new FileReceiverBuffer(bytecont, filename));

		if (!stop.containsKey(uuid)) {
			stop.put(uuid, new HashMap<Integer, Boolean>());
		}

		stop.get(uuid).put(id, false);
		pluuid = uuid;
		this.filename = filename;
		this.ms = msi;
		this.playerfile = isPlayerFile;
		this.id = id;
	}

	public static void stopReceiver(String uuid) {
		if (!stop.containsKey(uuid)) {
			stop.put(uuid, new HashMap<Integer, Boolean>());
		}
		if (!stop.isEmpty()) {
			for (Entry<Integer, Boolean> stops : stop.get(uuid).entrySet()) {
				stop.get(uuid).put(stops.getKey(), true);
			}
		}
	}

	public static void stopReceiver(String uuid, int id) {

		if (!stop.containsKey(uuid)) {
			stop.put(uuid, new HashMap<Integer, Boolean>());
		}
		stop.get(uuid).put(id, true);
	}

	public static void addBufferBytes(String uuid, byte[] bytes, int id) {
		if (!receiverBufer.containsKey(uuid)) {
			receiverBufer.put(uuid, new HashMap<Integer, FileReceiverBuffer>());
		}

		receiverBufer.get(uuid).get(id).addBytes(bytes);
	}

	public void run() {
		long time = System.currentTimeMillis();
		int cont = receiverBufer.get(pluuid).get(id).getCont();
		long logtime = System.currentTimeMillis();
		long fristtime = System.currentTimeMillis();

		IkisugiMusicPlayer.LOGGER.info("Server File Receiver Start : "
				+ ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid)).getDisplayName().getString() + " Name "
				+ filename + " Size "
				+ StringHelper.fileCapacityNotation(receiverBufer.get(pluuid).get(id).getBytes().length)
				+ " Target "
				+ (playerfile ? "Main" : "Everyone"));

		while (!receiverBufer.get(pluuid).get(id).isPerfectByte()) {

			if (stop.get(pluuid).get(id)) {
				IkisugiMusicPlayer.LOGGER.error("Server File Receiver Stop : Player "
						+ ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid)).getDisplayName().getString()
						+ " Name "
						+ filename + " Sent " + StringHelper.fileCapacityNotation(cont) + " Target "
						+ (playerfile ? "Main" : "Everyone") + " Elapsed " + (System.currentTimeMillis() - fristtime)
						+ "ms");
				receiverBufer.get(pluuid).remove(this.id);
			}

			if (cont == receiverBufer.get(pluuid).get(id).getCont() && System.currentTimeMillis() - time >= 10000) {
				IkisugiMusicPlayer.LOGGER.error("Server File Receiver Time Out : Player "
						+ ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid)).getDisplayName().getString()
						+ " Name "
						+ filename + " Sent " + StringHelper.fileCapacityNotation(cont) + " Target "
						+ (playerfile ? "Main" : "Everyone") + " Elapsed " + (System.currentTimeMillis() - fristtime)
						+ "ms");
				receiverBufer.get(pluuid).remove(this.id);
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
			if (cont != receiverBufer.get(pluuid).get(id).getCont()) {
				cont = receiverBufer.get(pluuid).get(id).getCont();
				time = System.currentTimeMillis();
			}
		}

		if (playerfile) {
			FileLoader.fileBytesWriter(receiverBufer.get(pluuid).get(id).getBytes(),
					FileHelper.getWorldPlayerPlayListDataPath(ms, pluuid).resolve(filename));

			try {
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			String uuid = UUID.randomUUID().toString();
			ImagePictuers.addPictuer(uuid, FileHelper.getWorldPlayerPlayListDataPath(ms, pluuid).resolve(filename), ms);
			PlayList.addPlayeyListFileNBT(ms, pluuid, filename, pluuid,
					new SoundData(FileHelper.getWorldPlayerPlayListDataPath(ms, pluuid).resolve(filename), uuid));

		} else {
			FileLoader.fileBytesWriter(receiverBufer.get(pluuid).get(id).getBytes(),
					FileHelper.getWorldEveryonePlayListDataPath(ms).resolve(filename));
			try {
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String uuid = UUID.randomUUID().toString();
			ImagePictuers.addPictuer(uuid, FileHelper.getWorldPlayerPlayListDataPath(ms, pluuid).resolve(filename), ms);
			PlayList.addPlayeyListFileNBT(ms, "everyone", filename, pluuid,
					new SoundData(FileHelper.getWorldEveryonePlayListDataPath(ms).resolve(filename), uuid));
		}
		IkisugiMusicPlayer.LOGGER.info("Server File Receiver was Success Full :  "
				+ ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid)).getDisplayName().getString() + " Name "
				+ filename + " Size " + StringHelper.fileCapacityNotation(cont) + " Target "
				+ (playerfile ? "Main" : "Everyone") + " Elapsed " + (System.currentTimeMillis() - fristtime) + "ms");
		receiverBufer.get(pluuid).remove(this.id);
	}
}
