package net.morimori.imp.handler;

import java.util.function.Supplier;

import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.morimori.imp.file.ServerSoundFileReceiver;
import net.morimori.imp.packet.ClientSendSoundFileMessage;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.packet.ServerResponseMessage;
import net.morimori.imp.util.PlayerHelper;

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
