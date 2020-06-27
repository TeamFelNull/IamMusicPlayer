package red.felnull.imp.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class CassetteDeckMessage {
	public int dim;
	public BlockPos pos;
	public int state;
	public String string;

	public CassetteDeckMessage(int dimID, BlockPos postion, int state) {
		this(dimID, postion, state, "null");
	}

	public CassetteDeckMessage(int dimID, BlockPos postion, int state, String st) {
		this.dim = dimID;
		this.pos = postion;
		this.state = state;
		this.string = st;
	}

	public static CassetteDeckMessage decodeMessege(PacketBuffer buffer) {

		return new CassetteDeckMessage(buffer.readInt(),
				new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()), buffer.readInt(),
				buffer.readString(32767));
	}

	public static void encodeMessege(CassetteDeckMessage messegeIn, PacketBuffer buffer) {
		buffer.writeInt(messegeIn.dim);
		buffer.writeInt(messegeIn.pos.getX());
		buffer.writeInt(messegeIn.pos.getY());
		buffer.writeInt(messegeIn.pos.getZ());
		buffer.writeInt(messegeIn.state);
		buffer.writeString(messegeIn.string);

	}
}
