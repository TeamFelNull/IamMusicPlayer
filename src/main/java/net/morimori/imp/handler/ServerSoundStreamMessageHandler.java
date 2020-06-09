package net.morimori.imp.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.morimori.imp.packet.ClientSoundStreamMessage;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.packet.ServerSoundStreamMessage;
import net.morimori.imp.sound.WorldSoundKey;
import net.morimori.imp.util.FileLoader;

public class ServerSoundStreamMessageHandler {
	public static Map<WorldSoundKey, byte[]> dwonloadbuf = new HashMap<WorldSoundKey, byte[]>();
	public static int sendspeed = 1024 * 32;
	public static Map<String, Boolean> stopsends = new HashMap<String, Boolean>();

	public static void reversiveMessage(ServerSoundStreamMessage message, Supplier<NetworkEvent.Context> ctx) {
		if (!message.stop) {
			stopsends.put(message.key, false);

			SendThread ST = new SendThread(message.key, message.wsk, message.offsetpos, ctx.get().getSender());
			ST.start();
		} else {
			stopsends.put(message.key, true);
		}

	}
}

class SendThread extends Thread {
	public String key;
	public WorldSoundKey wsk;
	public long offsetpos;
	public ServerPlayerEntity SPE;

	public SendThread(String key, WorldSoundKey wsk, long offsetpos2, ServerPlayerEntity spe) {
		this.key = key;
		this.wsk = wsk;
		this.offsetpos = offsetpos2;
		this.SPE = spe;
	}

	public void run() {
		if (!ServerSoundStreamMessageHandler.dwonloadbuf.containsKey(wsk)) {
			if (wsk != null && wsk.isServerExistence(SPE.getServer())) {
				ServerSoundStreamMessageHandler.dwonloadbuf.put(wsk,
						FileLoader.fileBytesReader(wsk.getServerPath(SPE.getServer())));
			} else {
				PacketHandler.INSTANCE.send(
						PacketDistributor.PLAYER.with(() -> SPE),
						new ClientSoundStreamMessage(key, new byte[1], 0, true));
				return;
			}
		}

		if (!ServerSoundStreamMessageHandler.dwonloadbuf.containsKey(wsk)
				|| ServerSoundStreamMessageHandler.dwonloadbuf.get(wsk) == null) {
			PacketHandler.INSTANCE.send(
					PacketDistributor.PLAYER.with(() -> SPE),
					new ClientSoundStreamMessage(key, new byte[1], 0, true));
			return;
		}

		int contl = 0;

		Mp3File mfile;
		try {
			mfile = new Mp3File(wsk.getServerPath(SPE.getServer()));

			long milsecnd = mfile.getLengthInMilliseconds();

			int oft = (int) ((float) ServerSoundStreamMessageHandler.dwonloadbuf.get(wsk).length
					* ((float) offsetpos / (float) milsecnd));

			int le = (ServerSoundStreamMessageHandler.dwonloadbuf.get(wsk).length - oft);

			int liscont = (int) Math
					.ceil((float) le / (float) ServerSoundStreamMessageHandler.sendspeed);

			for (int i = 0; i < liscont; i++) {
				if (ServerSoundStreamMessageHandler.stopsends.get(key)) {
					break;
				}
				try {
					sleep(10);
				} catch (InterruptedException e) {
				}

				if (le - contl >= ServerSoundStreamMessageHandler.sendspeed) {

					byte[] b = new byte[ServerSoundStreamMessageHandler.sendspeed];
					for (int ai = 0; ai < ServerSoundStreamMessageHandler.sendspeed; ai++) {
						b[ai] = ServerSoundStreamMessageHandler.dwonloadbuf.get(wsk)[contl + ai + oft];
					}

					PacketHandler.INSTANCE.send(
							PacketDistributor.PLAYER.with(() -> SPE),
							new ClientSoundStreamMessage(key, b, le, false));
					contl += ServerSoundStreamMessageHandler.sendspeed;
				} else {
					byte[] b = new byte[le - contl];
					for (int ai = 0; ai < le - contl; ai++) {
						b[ai] = ServerSoundStreamMessageHandler.dwonloadbuf.get(wsk)[contl + ai + oft];
					}

					PacketHandler.INSTANCE.send(
							PacketDistributor.PLAYER.with(() -> SPE),
							new ClientSoundStreamMessage(key, b, le, false));

					contl += le - contl;

				}
			}
		} catch (UnsupportedTagException | InvalidDataException | IOException e1) {

		}
	}

}