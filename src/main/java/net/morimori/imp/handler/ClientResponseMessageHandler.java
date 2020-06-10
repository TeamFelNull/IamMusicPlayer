package net.morimori.imp.handler;

import java.io.File;
import java.util.function.Supplier;

import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.morimori.imp.file.ImagePictuers;
import net.morimori.imp.file.ServerFileSender;
import net.morimori.imp.packet.ClientResponseMessage;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.packet.ServerClientDataSyncMessage;
import net.morimori.imp.packet.ServerResponseMessage;
import net.morimori.imp.util.FileHelper;
import net.morimori.imp.util.PlayerHelper;

public class ClientResponseMessageHandler {
	public static void reversiveMessage(ClientResponseMessage message, Supplier<NetworkEvent.Context> ctx) {

		if (message.num == 0) {
			ServerFileSender.responseWaits.get(PlayerHelper.getUUID(ctx.get().getSender())).put(message.id, false);
		} else if (message.num == 1) {
			ServerFileSender.startSender(PlayerHelper.getUUID(ctx.get().getSender()),
					FileHelper.getWorldPlayListDataPath(ctx.get().getSender().getServer()).resolve(message.st), false,
					ctx.get().getSender().getServer());
		} else if (message.num == 2) {

			if (ServerFileSender.canSending(PlayerHelper.getUUID(ctx.get().getSender()))) {
				PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()),
						new ServerResponseMessage(1, ""));
			} else {
				PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()),
						new ServerResponseMessage(2, ""));
			}

		} else if (message.num == 3) {

			File picfile = FileHelper.getWorldPictuerPath(ctx.get().getSender().getServer())
					.resolve(message.st + ".png").toFile();

			if (picfile.exists() && picfile != null) {
				byte[] picbyte = ImagePictuers.readPictuer(message.st, ctx.get().getSender().getServer());

				PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()),
						new ServerClientDataSyncMessage(0, message.st, picbyte));
			}
		}
	}
}
