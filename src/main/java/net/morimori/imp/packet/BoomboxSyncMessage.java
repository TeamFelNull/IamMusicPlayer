package net.morimori.imp.packet;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class BoomboxSyncMessage {
	public int dim;
	public BlockPos pos;
	public ItemStack cassette;
	public int openProgress;
	public long position;
	public long lasttime;
	public float volume;
	public boolean canplay;

	public BoomboxSyncMessage(int dimID, BlockPos postion, ItemStack cassetteItem, int opProgress,
			long position, long lasttime, float volume, boolean canplay) {
		this.dim = dimID;
		this.pos = postion;
		this.cassette = cassetteItem;
		this.openProgress = opProgress;
		this.position = position;
		this.lasttime = lasttime;
		this.volume = volume;
		this.canplay = canplay;
	}

	public static BoomboxSyncMessage decodeMessege(PacketBuffer buffer) {
		return new BoomboxSyncMessage(buffer.readInt(),
				new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()),
				ItemStack.read(buffer.readCompoundTag()), buffer.readInt(), buffer.readLong(), buffer.readLong(),
				buffer.readFloat(), buffer.readBoolean());
	}

	public static void encodeMessege(BoomboxSyncMessage messegeIn, PacketBuffer buffer) {
		buffer.writeInt(messegeIn.dim);
		buffer.writeInt(messegeIn.pos.getX());
		buffer.writeInt(messegeIn.pos.getY());
		buffer.writeInt(messegeIn.pos.getZ());
		buffer.writeCompoundTag(messegeIn.cassette.write(new CompoundNBT()));
		buffer.writeInt(messegeIn.openProgress);
		buffer.writeLong(messegeIn.position);
		buffer.writeLong(messegeIn.lasttime);
		buffer.writeFloat(messegeIn.volume);
		buffer.writeBoolean(messegeIn.canplay);
	}
}
