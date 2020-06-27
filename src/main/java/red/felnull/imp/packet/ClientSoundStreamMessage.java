package red.felnull.imp.packet;

import net.minecraft.network.PacketBuffer;

public class ClientSoundStreamMessage {
	public String key;
	public byte[] bytes;
	public int alleth;
	public boolean stop;

	public ClientSoundStreamMessage(String key, byte[] bytes, int leth, boolean stop) {
		this.key = key;
		this.bytes = bytes;
		this.alleth = leth;
		this.stop = stop;
	}

	public static ClientSoundStreamMessage decodeMessege(PacketBuffer buffer) {
		return new ClientSoundStreamMessage(buffer.readString(32767), buffer.readByteArray(), buffer.readInt(),
				buffer.readBoolean());
	}

	public static void encodeMessege(ClientSoundStreamMessage messegeIn, PacketBuffer buffer) {
		buffer.writeString(messegeIn.key);
		buffer.writeByteArray(messegeIn.bytes);
		buffer.writeInt(messegeIn.alleth);
		buffer.writeBoolean(messegeIn.stop);
	}
}
