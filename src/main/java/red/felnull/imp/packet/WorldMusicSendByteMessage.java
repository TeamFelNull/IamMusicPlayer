package red.felnull.imp.packet;

import net.minecraft.network.PacketBuffer;

public class WorldMusicSendByteMessage {
    public byte[] data;
    public String byteUUID;

    public WorldMusicSendByteMessage(String byteUUID, byte[] data) {
        this.byteUUID = byteUUID;
        this.data = data;
    }

    public static WorldMusicSendByteMessage decodeMessege(PacketBuffer buffer) {
        return new WorldMusicSendByteMessage(buffer.readString(), buffer.readByteArray());
    }

    public static void encodeMessege(WorldMusicSendByteMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.byteUUID);
        buffer.writeByteArray(messegeIn.data);
    }
}
