package net.morimori.imp.packet;

import net.minecraft.network.PacketBuffer;

public class ServerResponseMessage {
	public int num;
	public int id;

	public ServerResponseMessage(int num, int id) {
		this.num = num;
		this.id = id;

	}

	public static ServerResponseMessage decodeMessege(PacketBuffer buffer) {
		return new ServerResponseMessage(buffer.readInt(), buffer.readInt());
	}

	public static void encodeMessege(ServerResponseMessage messegeIn, PacketBuffer buffer) {
		buffer.writeInt(messegeIn.num);
		buffer.writeInt(messegeIn.id);
	}
}
