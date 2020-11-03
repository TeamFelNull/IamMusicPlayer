package net.morimori.imp.packet;

import net.minecraft.network.PacketBuffer;

public class ServerResponseMessage {
	public int num;
	public String name;

	public ServerResponseMessage(int num, String name) {
		this.num = num;
		this.name = name;

	}

	public static ServerResponseMessage decodeMessege(PacketBuffer buffer) {
		return new ServerResponseMessage(buffer.readInt(), buffer.readString(32767));
	}

	public static void encodeMessege(ServerResponseMessage messegeIn, PacketBuffer buffer) {
		buffer.writeInt(messegeIn.num);
		buffer.writeString(messegeIn.name);
	}
}
