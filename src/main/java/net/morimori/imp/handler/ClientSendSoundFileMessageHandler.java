package net.morimori.imp.handler;

import java.util.function.Supplier;

import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.morimori.imp.file.ServerFileReceiver;
import net.morimori.imp.packet.ClientSendSoundFileMessage;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.packet.ServerResponseMessage;
import net.morimori.imp.util.PlayerHelper;

public class ClientSendSoundFileMessageHandler {
	public static void reversiveMessage(ClientSendSoundFileMessage message, Supplier<NetworkEvent.Context> ctx) {

		if (message.isFrist) {
			ServerFileReceiver fr = new ServerFileReceiver(PlayerHelper.getUUID(ctx.get().getSender()),
					message.bytecont,
					message.name, ctx.get().getSender().server, message.isPlayerFile,message.sd);
			fr.start();
		}

		ServerFileReceiver.addBufferBytes(PlayerHelper.getUUID(ctx.get().getSender()), message.soundbyte);

		PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()),
				new ServerResponseMessage(0));
	}
}
