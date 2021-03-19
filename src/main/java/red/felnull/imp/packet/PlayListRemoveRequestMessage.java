package red.felnull.imp.packet;

import net.minecraft.network.PacketBuffer;

public class PlayListRemoveRequestMessage {
    public String name;

    public PlayListRemoveRequestMessage(String name) {
        this.name = name;
    }

    public static PlayListRemoveRequestMessage decodeMessege(PacketBuffer buffer) {
        return new PlayListRemoveRequestMessage(buffer.readString(32767));
    }

    public static void encodeMessege(PlayListRemoveRequestMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.name);
    }
}
