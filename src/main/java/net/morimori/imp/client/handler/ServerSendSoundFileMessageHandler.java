package net.morimori.imp.client.handler;

import java.util.function.Supplier;

import net.minecraftforge.fml.network.NetworkEvent;
import net.morimori.imp.file.ClientFileReceiver;
import net.morimori.imp.packet.ClientResponseMessage;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.packet.ServerSendSoundFileMessage;

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
