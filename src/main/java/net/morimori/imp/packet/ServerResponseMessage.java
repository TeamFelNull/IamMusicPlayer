package net.morimori.imp.packet;

import net.minecraft.network.PacketBuffer;

public class ServerResponseMessage {
	public int num;

	public ServerResponseMessage(int num) {
		this.num = num;
	}

	public static ServerResponseMessage decodeMessege(PacketBuffer buffer) {
		return new ServerResponseMessage(buffer.readInt());
	}

	public static void encodeMessege(ServerResponseMessage messegeIn, PacketBuffer buffer) {
		buffer.writeInt(messegeIn.num);
	}
}
