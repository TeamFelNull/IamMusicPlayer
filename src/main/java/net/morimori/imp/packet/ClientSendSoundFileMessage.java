package net.morimori.imp.packet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.morimori.imp.sound.SoundData;

public class ClientSendSoundFileMessage {
	public byte[] soundbyte;
	public boolean isFrist;
	public int bytecont;
	public String name;
	public boolean isPlayerFile;
	public SoundData sd;

	public ClientSendSoundFileMessage(byte[] filebytedata1, boolean frist, int cont, String filename,
			boolean playerfile, SoundData sd) {
		this.soundbyte = filebytedata1;
		this.isFrist = frist;
		this.bytecont = cont;
		this.name = filename;
		this.isPlayerFile = playerfile;
		this.sd = sd;
	}

	public static ClientSendSoundFileMessage decodeMessege(PacketBuffer buffer) {

		return new ClientSendSoundFileMessage(buffer.readByteArray(), buffer.readBoolean(), buffer.readInt(),
				buffer.readString(32767), buffer.readBoolean(), new SoundData(buffer.readCompoundTag()));
	}

	public static void encodeMessege(ClientSendSoundFileMessage messegeIn, PacketBuffer buffer) {

		buffer.writeByteArray(messegeIn.soundbyte);
		buffer.writeBoolean(messegeIn.isFrist);
		buffer.writeInt(messegeIn.bytecont);
		buffer.writeString(messegeIn.name);
		buffer.writeBoolean(messegeIn.isPlayerFile);
		buffer.writeCompoundTag(messegeIn.sd.writeNBT(new CompoundNBT()));
	}
}
