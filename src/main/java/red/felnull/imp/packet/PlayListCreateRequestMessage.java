package red.felnull.imp.packet;

import net.minecraft.network.PacketBuffer;

public class PlayListCreateRequestMessage {
    public String name;
    public String imageID;
    public int w;
    public int h;
    public boolean anyone;

    public PlayListCreateRequestMessage(String name, String imageID, int w, int h, boolean anyone) {
        this.name = name;
        this.imageID = imageID;
        this.w = w;
        this.h = h;
        this.anyone = anyone;
    }

    public static PlayListCreateRequestMessage decodeMessege(PacketBuffer buffer) {
        return new PlayListCreateRequestMessage(buffer.readString(32767), buffer.readString(32767), buffer.readInt(), buffer.readInt(), buffer.readBoolean());
    }

    public static void encodeMessege(PlayListCreateRequestMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.name);
        buffer.writeString(messegeIn.imageID);
        buffer.writeInt(messegeIn.w);
        buffer.writeInt(messegeIn.h);
        buffer.writeBoolean(messegeIn.anyone);
    }
}
