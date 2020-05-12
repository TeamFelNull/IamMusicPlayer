package net.morimori.imp.file;

import java.nio.file.Path;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.morimori.imp.IkisugiMusicPlayer;
import net.morimori.imp.packet.ClientSendSoundFileMessage;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.sound.SoundData;
import net.morimori.imp.util.FileLoader;
import net.morimori.imp.util.NarratorHelper;
import net.morimori.imp.util.StringHelper;

public class ClientFileSender extends Thread {

	private static Minecraft mc = Minecraft.getInstance();

	public static int sendingprograse;
	public static int sendingall;

	public static boolean sending;

	public static String FileName;

	public static int bytespeed = 5000;

	public static boolean responseWait;

	public static boolean stop;

	private Path path;
	public boolean playerfile;

	public ClientFileSender(Path path, boolean playerfile) {
		this.path = path;
		this.playerfile = playerfile;
	}

	public static void stopSend() {
		stop = true;
	}

	public static void startSend(Path path, boolean playerfile) {

		if (sending || mc.player == null)
			return;

		stop = false;
		sending = true;
		sendingprograse = 0;
		ClientFileSender fs = new ClientFileSender(path, playerfile);
		fs.start();

	}

	public static String getPrograsePar() {

		return Math.round(((float) sendingprograse / (float) sendingall) * 100) + " %";
	}

	public void run() {
		byte[] bytes = FileLoader.fileBytesReader(this.path);
		boolean frist = true;
		long fristtime = System.currentTimeMillis();
		long logtime = System.currentTimeMillis();
		long time = System.currentTimeMillis();
		FileName = this.path.toFile().getName();
		if (bytes == null) {
			IkisugiMusicPlayer.LOGGER.info("Null Sender File : " + this.path.toFile().toString());
			sending = false;
			return;
		}
		int cont = 0;

		IkisugiMusicPlayer.LOGGER.info("Client File Sender Start : " + " Name "
				+ this.path.toFile().getName() + " Size "
				+ StringHelper.fileCapacityNotation(this.path.toFile().length())
				+ " Target " + (playerfile ? "Main" : "Everyone"));

		for (int i = 0; i < bytes.length; i += bytespeed) {
			byte[] bi = new byte[bytes.length - i >= bytespeed ? bytespeed : bytes.length - i];
			for (int c = 0; c < bytespeed; c++) {
				if ((i + c) < bytes.length) {
					bi[c] = bytes[i + c];
					cont++;
				}
			}
			responseWait = true;
			try {

				if (mc.player == null) {
					finishSend();
					return;
				}

				PacketHandler.INSTANCE
						.sendToServer(
								new ClientSendSoundFileMessage(bi, frist, bytes.length, this.path.toFile().getName(),
										playerfile, new SoundData(this.path)));
				frist = false;
				sendingprograse = cont;
				sendingall = bytes.length;

				if (stop) {
					IkisugiMusicPlayer.LOGGER.error("Client File Sending Stop :"
							+ " Name "
							+ this.path.toFile().getName() + " Sent " + StringHelper.fileCapacityNotation(cont)
							+ " Target "
							+ (playerfile ? "Main" : "Everyone") + " Elapsed "
							+ (System.currentTimeMillis() - fristtime)
							+ "ms " + getPrograsePar());
					NarratorHelper.say(I18n.format("narrator.fileuploadstop", this.path.toFile().getName()));
					finishSend();
					return;
				}

				while (responseWait) {

					if (stop) {
						IkisugiMusicPlayer.LOGGER.error("Client File Sending Stop : Player "
								+ " Name "
								+ this.path.toFile().getName() + " Sent " + StringHelper.fileCapacityNotation(cont)
								+ " Target "
								+ (playerfile ? "Main" : "Everyone") + " Elapsed "
								+ (System.currentTimeMillis() - fristtime)
								+ "ms " + getPrograsePar());
						NarratorHelper.say(I18n.format("narrator.fileuploadstop", this.path.toFile().getName()));
						finishSend();
						return;
					}

					Thread.sleep(1);

					if (System.currentTimeMillis() - time >= 10000) {
						IkisugiMusicPlayer.LOGGER.error("Client File Sender Time Out : Player "
								+ " Name "
								+ this.path.toFile().getName() + " Sent " + StringHelper.fileCapacityNotation(cont)
								+ " Target "
								+ (playerfile ? "Main" : "Everyone") + " Elapsed "
								+ (System.currentTimeMillis() - fristtime)
								+ "ms " + getPrograsePar());
						NarratorHelper.say(I18n.format("narrator.fileuploadtimeout", this.path.toFile().getName()));
						finishSend();
						return;
					}

				}
			} catch (InterruptedException e) {

			}

			if (System.currentTimeMillis() - logtime >= 5000) {

				logtime = System.currentTimeMillis();
				IkisugiMusicPlayer.LOGGER.info("Client File Sending :" + " Name " + this.path.toFile().getName()
						+ " Sent " + StringHelper.fileCapacityNotation(cont) + " Target "
						+ (playerfile ? "Main" : "Everyone") + " Elapsed "
						+ (System.currentTimeMillis() - fristtime) + "ms " + getPrograsePar());
				NarratorHelper.say(I18n.format("narrator.fileupload", this.path.toFile().getName(), getPrograsePar()));
			}
			time = System.currentTimeMillis();
		}

		IkisugiMusicPlayer.LOGGER.info("Client File Sender was Success Full :" + " Name "
				+ this.path.toFile().getName() + " Size " + StringHelper.fileCapacityNotation(cont) + " Target "
				+ (playerfile ? "Main" : "Everyone") + " Elapsed " + (System.currentTimeMillis() - fristtime) + "ms");
		NarratorHelper.say(I18n.format("narrator.fileuploadsuccess", this.path.toFile().getName()));
		finishSend();
	}

	public void finishSend() {
		sending = false;
		sendingprograse = 0;
		sendingall = 0;
		FileName = "";
		path = null;

	}
}
