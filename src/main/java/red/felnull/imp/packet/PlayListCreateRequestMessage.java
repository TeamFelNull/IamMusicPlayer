package red.felnull.imp.packet;

import net.minecraft.network.PacketBuffer;

public class PlayListCreateRequestMessage {
    public String name;
    public String imageID;

    public PlayListCreateRequestMessage(String name, String imageID) {
        this.name = name;
        this.imageID = imageID;
    }

    public static PlayListCreateRequestMessage decodeMessege(PacketBuffer buffer) {
        return new PlayListCreateRequestMessage(buffer.readString(32767), buffer.readString(32767));
    }

    public static void encodeMessege(PlayListCreateRequestMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.name);
        buffer.writeString(messegeIn.imageID);
    }
}
