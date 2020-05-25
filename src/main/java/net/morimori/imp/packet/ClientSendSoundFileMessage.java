package net.morimori.imp.packet;

import net.minecraft.network.PacketBuffer;

public class ClientSendSoundFileMessage {
	public byte[] soundbyte;
	public boolean isFrist;
	public int bytecont;
	public String name;
	public boolean isPlayerFile;
	public int id;

	public ClientSendSoundFileMessage(byte[] filebytedata1, boolean frist, int cont, String filename,
			boolean playerfile, int id) {
		this.soundbyte = filebytedata1;
		this.isFrist = frist;
		this.bytecont = cont;
		this.name = filename;
		this.isPlayerFile = playerfile;
		this.id = id;
	}

	public static ClientSendSoundFileMessage decodeMessege(PacketBuffer buffer) {

		return new ClientSendSoundFileMessage(buffer.readByteArray(), buffer.readBoolean(), buffer.readInt(),
				buffer.readString(32767), buffer.readBoolean(),buffer.readInt());
	}

	public static void encodeMessege(ClientSendSoundFileMessage messegeIn, PacketBuffer buffer) {

		buffer.writeByteArray(messegeIn.soundbyte);
		buffer.writeBoolean(messegeIn.isFrist);
		buffer.writeInt(messegeIn.bytecont);
		buffer.writeString(messegeIn.name);
		buffer.writeBoolean(messegeIn.isPlayerFile);
		buffer.writeInt(messegeIn.id);
	}
}
