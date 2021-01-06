package red.felnull.imp.packet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import red.felnull.imp.music.resource.PlayMusic;

import java.util.UUID;

public class MusicRingMessage {
    public UUID uuid;
    public Vector3d musicPos;
    public PlayMusic music;
    public long startPos;

    public MusicRingMessage(UUID uuid, Vector3d musicPos, PlayMusic music, long startPos) {
        this.uuid = uuid;
        this.musicPos = musicPos;
        this.music = music;
        this.startPos = startPos;
    }

    public static MusicRingMessage decodeMessege(PacketBuffer buffer) {

        return new MusicRingMessage(UUID.fromString(buffer.readString(32767)), new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()), new PlayMusic(buffer.readString(32767), buffer.readCompoundTag()), buffer.readLong());
    }

    public static void encodeMessege(MusicRingMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.uuid.toString());
        buffer.writeDouble(messegeIn.musicPos.getX());
        buffer.writeDouble(messegeIn.musicPos.getY());
        buffer.writeDouble(messegeIn.musicPos.getZ());
        buffer.writeString(messegeIn.music.getUUID());
        buffer.writeCompoundTag(messegeIn.music.write(new CompoundNBT()));
        buffer.writeLong(messegeIn.startPos);
    }
}
