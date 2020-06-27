package red.felnull.imp.client.handler;

import java.util.function.Supplier;

import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.file.ClientFileReceiver;
import red.felnull.imp.packet.ClientResponseMessage;
import red.felnull.imp.packet.PacketHandler;
import red.felnull.imp.packet.ServerSendSoundFileMessage;

public class ServerSendSoundFileMessageHandler {
	public static void reversiveMessage(ServerSendSoundFileMessage message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().setPacketHandled(true);

		if (message.isFrist) {
			ClientFileReceiver CFR = new ClientFileReceiver(message.id, message.bytecont, message.name,
					message.isDownload, message.souuid);
			CFR.start();
		}
		ClientFileReceiver.addBufferBytes(message.id, message.soundbyte);

		PacketHandler.INSTANCE.sendToServer(new ClientResponseMessage(0, message.id, "null"));

	}
}
