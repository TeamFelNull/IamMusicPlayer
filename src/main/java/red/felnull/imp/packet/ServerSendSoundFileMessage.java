package red.felnull.imp.packet;

import net.minecraft.network.PacketBuffer;

public class ServerSendSoundFileMessage {
	public byte[] soundbyte;
	public boolean isFrist;
	public int id;
	public int bytecont;
	public String name;
	public boolean isDownload;
	public String souuid;

	public ServerSendSoundFileMessage(byte[] filebytedata1, int id, boolean frist, int cont, String filename,
			boolean download, String sounduuid) {
		this.soundbyte = filebytedata1;
		this.isFrist = frist;
		this.bytecont = cont;
		this.id = id;
		this.name = filename;
		this.isDownload = download;
		this.souuid = sounduuid;
	}

	public static ServerSendSoundFileMessage decodeMessege(PacketBuffer buffer) {

		return new ServerSendSoundFileMessage(buffer.readByteArray(), buffer.readInt(), buffer.readBoolean(),
				buffer.readInt(), buffer.readString(32767), buffer.readBoolean(), buffer.readString(32767));
	}

	public static void encodeMessege(ServerSendSoundFileMessage messegeIn, PacketBuffer buffer) {

		buffer.writeByteArray(messegeIn.soundbyte);
		buffer.writeInt(messegeIn.id);
		buffer.writeBoolean(messegeIn.isFrist);
		buffer.writeInt(messegeIn.bytecont);
		buffer.writeString(messegeIn.name);
		buffer.writeBoolean(messegeIn.isDownload);
		buffer.writeString(messegeIn.souuid);
	}
}
