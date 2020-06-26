package net.morimori.imp.client.handler;

import java.util.function.Supplier;

import net.minecraftforge.fml.network.NetworkEvent;
import net.morimori.imp.client.file.ClientSoundFileSender;
import net.morimori.imp.file.ClientFileReceiver;
import net.morimori.imp.packet.ServerResponseMessage;

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
