package red.felnull.imp.packet;

import net.minecraft.network.PacketBuffer;

public class ServerClientDataSyncMessage {

	public int id;
	public String st;
	public byte[] data;

	public ServerClientDataSyncMessage(int id, String st, byte[] data) {
		this.id = id;
		this.st = st;
		this.data = data;
	}

	public static ServerClientDataSyncMessage decodeMessege(PacketBuffer buffer) {
		return new ServerClientDataSyncMessage(buffer.readInt(), buffer.readString(), buffer.readByteArray());
	}

	public static void encodeMessege(ServerClientDataSyncMessage messegeIn, PacketBuffer buffer) {
		buffer.writeInt(messegeIn.id);
		buffer.writeString(messegeIn.st);
		buffer.writeByteArray(messegeIn.data);
	}
}
