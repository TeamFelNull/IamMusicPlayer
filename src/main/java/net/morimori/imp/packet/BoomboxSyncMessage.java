package net.morimori.imp.packet;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class BoomboxSyncMessage {
	public int dim;
	public BlockPos pos;
	public ItemStack cassette;
	public int openProgress;
	public Set<String> lisnFinishedPlayers;

	public BoomboxSyncMessage(int dimID, BlockPos postion, ItemStack cassetteItem, int opProgress,
			Set<String> finishedplayes) {
		this.dim = dimID;
		this.pos = postion;
		this.cassette = cassetteItem;
		this.openProgress = opProgress;
		this.lisnFinishedPlayers = finishedplayes;
	}

	public static BoomboxSyncMessage decodeMessege(PacketBuffer buffer) {
		return new BoomboxSyncMessage(buffer.readInt(),
				new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()),
				ItemStack.read(buffer.readCompoundTag()), buffer.readInt(), readSet(buffer));
	}

	private static Set<String> readSet(PacketBuffer buffer) {

		Set<String> stmap = new HashSet<String>();
		CompoundNBT ptmnbt = buffer.readCompoundTag();
		for (String key : ptmnbt.keySet()) {
			stmap.add(ptmnbt.getString(key));
		}

		return stmap;
	}

	public static void encodeMessege(BoomboxSyncMessage messegeIn, PacketBuffer buffer) {
		buffer.writeInt(messegeIn.dim);
		buffer.writeInt(messegeIn.pos.getX());
		buffer.writeInt(messegeIn.pos.getY());
		buffer.writeInt(messegeIn.pos.getZ());
		buffer.writeCompoundTag(messegeIn.cassette.write(new CompoundNBT()));
		buffer.writeInt(messegeIn.openProgress);
		CompoundNBT ptmnbt = new CompoundNBT();
		int cont = 0;
		for (String st : messegeIn.lisnFinishedPlayers) {
			ptmnbt.putString(String.valueOf(cont), st);
			cont++;
		}
		buffer.writeCompoundTag(ptmnbt);
	}
}
