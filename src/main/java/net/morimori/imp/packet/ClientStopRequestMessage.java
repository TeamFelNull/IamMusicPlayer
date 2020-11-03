package net.morimori.imp.packet;

import net.minecraft.network.PacketBuffer;

public class ClientStopRequestMessage {
	public int id;

	public ClientStopRequestMessage(int id) {
		this.id = id;
	}

	public static ClientStopRequestMessage decodeMessege(PacketBuffer buffer) {
		return new ClientStopRequestMessage(buffer.readInt());
	}

	public static void encodeMessege(ClientStopRequestMessage messegeIn, PacketBuffer buffer) {
		buffer.writeInt(messegeIn.id);
	}
}
