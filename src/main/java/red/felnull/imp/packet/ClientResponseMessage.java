package red.felnull.imp.packet;

import net.minecraft.network.PacketBuffer;

public class ClientResponseMessage {
	public int num;
	public int id;
	public String st;

	public ClientResponseMessage(int num, int id, String st) {
		this.num = num;
		this.id = id;
		this.st = st;
	}

	public static ClientResponseMessage decodeMessege(PacketBuffer buffer) {
		return new ClientResponseMessage(buffer.readInt(), buffer.readInt(), buffer.readString(32767));
	}

	public static void encodeMessege(ClientResponseMessage messegeIn, PacketBuffer buffer) {
		buffer.writeInt(messegeIn.num);
		buffer.writeInt(messegeIn.id);
		buffer.writeString(messegeIn.st);
	}
}
