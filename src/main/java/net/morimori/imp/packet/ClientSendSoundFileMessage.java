package net.morimori.imp.packet;

import net.minecraft.network.PacketBuffer;
import net.morimori.imp.client.file.ClientSoundFileSender.SendFolderType;

public class ClientSendSoundFileMessage {
    public byte[] soundbyte;
    public boolean frist;
    public int bytele;
    public String name;
    public SendFolderType type;
    public boolean stop;

    public ClientSendSoundFileMessage(String name, boolean stop) {
        this(new byte[1], false, 0, name, SendFolderType.MAIN, stop);
    }

    public ClientSendSoundFileMessage(byte[] byetes, String name) {
        this(byetes, false, 0, name, SendFolderType.MAIN);
    }

    public ClientSendSoundFileMessage(byte[] byetes, boolean frist, int length, String name,
                                      SendFolderType type) {
        this(byetes, frist, length, name, type, false);
    }

    public ClientSendSoundFileMessage(byte[] byetes, boolean frist, int length, String name,
                                      SendFolderType type, boolean stop) {
        this.soundbyte = byetes;
        this.frist = frist;
        this.bytele = length;
        this.name = name;
        this.type = type;
        this.stop = stop;
    }

    public static ClientSendSoundFileMessage decodeMessege(PacketBuffer buffer) {

        return new ClientSendSoundFileMessage(buffer.readByteArray(), buffer.readBoolean(), buffer.readInt(),
                buffer.readString(32767), SendFolderType.valueOf(buffer.readString(32767).toUpperCase()),
                buffer.readBoolean());
    }

    public static void encodeMessege(ClientSendSoundFileMessage messegeIn, PacketBuffer buffer) {

        buffer.writeByteArray(messegeIn.soundbyte);
        buffer.writeBoolean(messegeIn.frist);
        buffer.writeInt(messegeIn.bytele);
        buffer.writeString(messegeIn.name);
        buffer.writeString(messegeIn.type.name());
        buffer.writeBoolean(messegeIn.stop);
    }
}
