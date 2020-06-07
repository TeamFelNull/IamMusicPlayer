package net.morimori.imp.client.handler;

import java.util.ArrayList;
import java.util.function.Supplier;

import net.minecraftforge.fml.network.NetworkEvent;
import net.morimori.imp.packet.ClientSoundStreamMessage;
import net.morimori.imp.sound.WorldSoundRinger;

public class ClientSoundStreamMessageHandler {
	public static void reversiveMessage(ClientSoundStreamMessage message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().setPacketHandled(true);

		if (!message.stop) {
			WorldSoundRinger.leths.put(message.key, message.alleth);
			WorldSoundRinger.milisecs.put(message.key, message.milsec);
			WorldSoundRinger.bairitus.put(message.key, message.bai);

			if (!WorldSoundRinger.bytebuf.containsKey(message.key)) {
				WorldSoundRinger.bytebuf.put(message.key, new ArrayList<byte[]>());
			}
			WorldSoundRinger.bytebuf.get(message.key).add(message.bytes);
		} else {
			WorldSoundRinger.stops.put(message.key, true);
		}

	}
}
