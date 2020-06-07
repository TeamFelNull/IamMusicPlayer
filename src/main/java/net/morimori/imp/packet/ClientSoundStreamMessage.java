package net.morimori.imp.packet;

import net.minecraft.network.PacketBuffer;

public class ClientSoundStreamMessage {
	public String key;
	public byte[] bytes;
	public int alleth;
	public boolean stop;
	public long milsec;
	public float bai;
	public float sbai;

	public ClientSoundStreamMessage(String key, byte[] bytes, int leth, boolean stop, long milsc, float bairtu,
			float labairtu) {
		this.key = key;
		this.bytes = bytes;
		this.alleth = leth;
		this.stop = stop;
		this.milsec = milsc;
		this.bai = bairtu;
		this.sbai = labairtu;
	}

	public static ClientSoundStreamMessage decodeMessege(PacketBuffer buffer) {
		return new ClientSoundStreamMessage(buffer.readString(32767), buffer.readByteArray(), buffer.readInt(),
				buffer.readBoolean(), buffer.readLong(), buffer.readFloat(), buffer.readFloat());
	}

	public static void encodeMessege(ClientSoundStreamMessage messegeIn, PacketBuffer buffer) {
		buffer.writeString(messegeIn.key);
		buffer.writeByteArray(messegeIn.bytes);
		buffer.writeInt(messegeIn.alleth);
		buffer.writeBoolean(messegeIn.stop);
		buffer.writeLong(messegeIn.milsec);
		buffer.writeFloat(messegeIn.bai);
		buffer.writeFloat(messegeIn.sbai);
	}
}
