package red.felnull.imp.client.handler;

import java.util.ArrayList;
import java.util.function.Supplier;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.packet.ClientSoundStreamMessage;
import red.felnull.imp.sound.WorldSoundRinger;

public class ClientSoundStreamMessageHandler {
	public static void reversiveMessage(ClientSoundStreamMessage message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().setPacketHandled(true);

		if (!message.stop) {
			WorldSoundRinger.leths.put(message.key, message.alleth);

			if (!WorldSoundRinger.bytebuf.containsKey(message.key)) {
				WorldSoundRinger.bytebuf.put(message.key, new ArrayList<byte[]>());
			}

			if (WorldSoundRinger.bytebuf.get(message.key).isEmpty()
					|| WorldSoundRinger.bytebuf.get(message.key).size() <= 2) {
				WorldSoundRinger.bytebuf.get(message.key).add(message.bytes);
			} else {
				byte[] mby = WorldSoundRinger.bytebuf.get(message.key)
						.get(WorldSoundRinger.bytebuf.get(message.key).size() - 1);

				byte[] aby = ArrayUtils.addAll(mby, message.bytes);
				if (WorldSoundRinger.bytebuf.containsKey(message.key)) {
					WorldSoundRinger.bytebuf.get(message.key).set(WorldSoundRinger.bytebuf.get(message.key).size() - 1,
							aby);
				}
			}

		} else {
			WorldSoundRinger.stops.put(message.key, true);
		}

	}
}
