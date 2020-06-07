package net.morimori.imp.client.handler;

import java.util.Map.Entry;

import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.morimori.imp.client.screen.CassetteDeckScreen;
import net.morimori.imp.client.screen.CassetteStoringScreen;
import net.morimori.imp.file.ClientFileReceiver;
import net.morimori.imp.file.ClientFileSender;
import net.morimori.imp.sound.ClientSoundPlayer;
import net.morimori.imp.sound.SoundWaitThread;

public class ClientHandler {

	//private static Minecraft mc = Minecraft.getInstance();
	private static int soundwaitcheckTimer;
	private static int stringTimer;

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent e) {

		ClientSoundPlayer.INSTANS.tick();

		for (Entry<String, Integer> exs : RenderHandler.expations.entrySet()) {
			if (exs.getValue() > 0) {
				RenderHandler.expations.put(exs.getKey(), exs.getValue() - 1);
			} else {
				RenderHandler.expations.remove(exs.getKey());
			}
		}

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

			for (Entry<Integer, Integer> nume : CassetteStoringScreen.slipmaxs.entrySet()) {

				if (CassetteStoringScreen.slips.containsKey(nume.getKey())) {

					if (CassetteStoringScreen.slips.get(nume.getKey()) < CassetteStoringScreen.slipmaxs
							.get(nume.getKey())) {
						CassetteStoringScreen.slips.put(nume.getKey(),
								CassetteStoringScreen.slips.get(nume.getKey()) + 1);
					} else {
						CassetteStoringScreen.slips.put(nume.getKey(), 0);
					}

				} else {
					CassetteStoringScreen.slips.put(nume.getKey(), 1);
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
