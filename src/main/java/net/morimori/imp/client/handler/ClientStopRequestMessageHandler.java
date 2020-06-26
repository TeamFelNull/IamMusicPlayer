package net.morimori.imp.client.handler;

import java.util.function.Supplier;

import net.minecraftforge.fml.network.NetworkEvent;
import net.morimori.imp.file.ClientFileReceiver;
import net.morimori.imp.packet.ClientStopRequestMessage;

public class ClientStopRequestMessageHandler {
    public static void reversiveMessage(ClientStopRequestMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);

        ClientFileReceiver.stopReceiver(message.id);

    }
}
