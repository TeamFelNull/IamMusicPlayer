package net.morimori.imp.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class SoundFileUploaderMessage {
    public int dim;
    public BlockPos pos;
    public int state;
    public String string;

    public SoundFileUploaderMessage(int dimID, BlockPos postion, int state) {
        this(dimID, postion, state, "null");
    }

    public SoundFileUploaderMessage(int dimID, BlockPos postion, int state, String st) {
        this.dim = dimID;
        this.pos = postion;
        this.state = state;
        this.string = st;
    }

    public static SoundFileUploaderMessage decodeMessege(PacketBuffer buffer) {

        return new SoundFileUploaderMessage(buffer.readInt(),
                new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()), buffer.readInt(),
                buffer.readString(32767));
    }

    public static void encodeMessege(SoundFileUploaderMessage messegeIn, PacketBuffer buffer) {
        buffer.writeInt(messegeIn.dim);
        buffer.writeInt(messegeIn.pos.getX());
        buffer.writeInt(messegeIn.pos.getY());
        buffer.writeInt(messegeIn.pos.getZ());
        buffer.writeInt(messegeIn.state);
        buffer.writeString(messegeIn.string);

    }
}
