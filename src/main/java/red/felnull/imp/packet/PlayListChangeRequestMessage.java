package red.felnull.imp.packet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import red.felnull.imp.music.resource.PlayImage;

public class PlayListChangeRequestMessage {
    public String uuid;
    public String name;
    public PlayImage image;
    public boolean anyone;

    public PlayListChangeRequestMessage(String uuid, String name, PlayImage image, boolean anyone) {
        this.uuid = uuid;
        this.name = name;
        this.anyone = anyone;
        this.image = image;
    }

    public static PlayListChangeRequestMessage decodeMessege(PacketBuffer buffer) {
        return new PlayListChangeRequestMessage(buffer.readString(32767), buffer.readString(32767), new PlayImage(buffer.readCompoundTag()), buffer.readBoolean());
    }

    public static void encodeMessege(PlayListChangeRequestMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.uuid);
        buffer.writeString(messegeIn.name);
        buffer.writeCompoundTag(messegeIn.image.write(new CompoundNBT()));
        buffer.writeBoolean(messegeIn.anyone);
    }
}
