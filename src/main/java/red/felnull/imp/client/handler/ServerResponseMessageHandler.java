package red.felnull.imp.client.handler;

import java.util.function.Supplier;

import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.client.file.ClientSoundFileSender;
import red.felnull.imp.file.ClientFileReceiver;
import red.felnull.imp.packet.ServerResponseMessage;

public class ServerResponseMessageHandler {
	public static void reversiveMessage(ServerResponseMessage message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().setPacketHandled(true);

		if (message.num == 0) {
			if (ClientSoundFileSender.getSender().containsKey(message.name)) {
				ClientSoundFileSender.getSender().get(message.name).response = true;
			}

		} else if (message.num == 1) {
			ClientFileReceiver.canReceiving = true;
		} else if (message.num == 2) {
			ClientFileReceiver.canReceiving = false;
		}

	}
}
