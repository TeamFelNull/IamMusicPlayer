package net.morimori.imp.client.handler;

import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.morimori.imp.client.screen.CassetteDeckScreen;
import net.morimori.imp.file.ClientFileReceiver;
import net.morimori.imp.file.ClientFileSender;
import net.morimori.imp.sound.SoundWaitThread;

public class ClientHandler {

	//private static Minecraft mc = Minecraft.getInstance();
	private static int soundwaitcheckTimer;
	private static int stringTimer;

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent e) {

		if (RenderHandler.waitThreadCrash > 0) {
			RenderHandler.waitThreadCrash--;
		}

		if (soundwaitcheckTimer < 20) {
			soundwaitcheckTimer++;
		} else {
			soundwaitcheckTimer = 0;
			if (!SoundWaitThread.cheking) {
				RenderHandler.waitThreadCrash = 100;
				SoundWaitThread.startSoundWaiter();
			}
			SoundWaitThread.cheking = false;

		}

		if (stringTimer < 20) {
			stringTimer++;
		} else {
			stringTimer = 0;

			if (CassetteDeckScreen.maxsliselectfilestringL != 0) {
				if (CassetteDeckScreen.sliselectfilestringL < CassetteDeckScreen.maxsliselectfilestringL) {
					CassetteDeckScreen.sliselectfilestringL++;
				} else {
					CassetteDeckScreen.sliselectfilestringL = 0;
				}
			}
		}

	}

	@SubscribeEvent
	public static void onPlayerLogOut(PlayerLoggedOutEvent e) {
		ClientFileSender.stopSend();
		ClientFileReceiver.stopReceiver();
		ClientFileReceiver.receiverBufer.clear();
		ClientFileReceiver.stop.clear();

	}
}
