package red.felnull.imp.packet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import red.felnull.imp.music.resource.PlayImage;

public class PlayListCreateRequestMessage {
    public String name;
    public PlayImage image;
    public boolean anyone;

    public PlayListCreateRequestMessage(String name, PlayImage image, boolean anyone) {
        this.name = name;
        this.anyone = anyone;
        this.image = image;
    }

    public static PlayListCreateRequestMessage decodeMessege(PacketBuffer buffer) {
        return new PlayListCreateRequestMessage(buffer.readString(32767), new PlayImage(buffer.readCompoundTag()), buffer.readBoolean());
    }

    public static void encodeMessege(PlayListCreateRequestMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.name);
        buffer.writeCompoundTag(messegeIn.image.write(new CompoundNBT()));
        buffer.writeBoolean(messegeIn.anyone);
    }
}
