package red.felnull.imp.handler;

import java.util.function.Supplier;

import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import red.felnull.imp.file.ServerSoundFileReceiver;
import red.felnull.imp.packet.ClientSendSoundFileMessage;
import red.felnull.imp.packet.PacketHandler;
import red.felnull.imp.packet.ServerResponseMessage;
import red.felnull.imp.util.PlayerHelper;

public class ClientSendSoundFileMessageHandler {
	public static void reversiveMessage(ClientSendSoundFileMessage message, Supplier<NetworkEvent.Context> ctx) {
		if (!message.stop) {
			if (message.frist) {
				ServerSoundFileReceiver fr = new ServerSoundFileReceiver(PlayerHelper.getUUID(ctx.get().getSender()),
						message.name, message.bytele, ctx.get().getSender().server, message.type);
				fr.start();
			}

			ServerSoundFileReceiver.addBufferBytes(PlayerHelper.getUUID(ctx.get().getSender()), message.name,
					message.soundbyte);

			PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()),
					new ServerResponseMessage(0, message.name));
		} else {
			ServerSoundFileReceiver.receiveStop(PlayerHelper.getUUID(ctx.get().getSender()), message.name);
		}

	}
}
