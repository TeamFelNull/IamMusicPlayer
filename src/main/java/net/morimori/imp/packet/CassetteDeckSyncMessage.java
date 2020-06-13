package net.morimori.imp.packet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class CassetteDeckSyncMessage {
	public int dim;
	public BlockPos pos;
	public NonNullList<ItemStack> items;
	public int pitch;
	public int yaw;
	public String selectedfolder;
	public String selectfile;
	public Map<String, String> playerstager;
	public int recordingPrograse;
	public Set<String> lisnFinishedPlayers;
	public int copyingPrograse;
	public int deletingPrograse;

	public CassetteDeckSyncMessage(int dimID, BlockPos postion, NonNullList<ItemStack> Item, int Pitch, int Yaw,
			String foldername, String filename, Map<String, String> playerstager, int recordingPrograse,
			Set<String> finishedplayes,
			int copyingprograse, int deletingprograse) {
		this.dim = dimID;
		this.pos = postion;
		this.items = Item;
		this.pitch = Pitch;
		this.yaw = Yaw;
		this.selectedfolder = foldername;
		this.selectfile = filename;
		this.playerstager = playerstager;
		this.recordingPrograse = recordingPrograse;
		this.lisnFinishedPlayers = finishedplayes;
		this.copyingPrograse = copyingprograse;
		this.deletingPrograse = deletingprograse;
	}

	public static CassetteDeckSyncMessage decodeMessege(PacketBuffer buffer) {
		NonNullList<ItemStack> itemsa = NonNullList.withSize(3, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(buffer.readCompoundTag(), itemsa);
		return new CassetteDeckSyncMessage(buffer.readInt(),
				new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()),
				itemsa, buffer.readInt(), buffer.readInt(), buffer.readString(32767), buffer.readString(32767),
				readMap(buffer),
				buffer.readInt(), readSet(buffer), buffer.readInt(), buffer.readInt());
	}

	private static Map<String, String> readMap(PacketBuffer buffer) {

		Map<String, String> stmap = new HashMap<String, String>();
		CompoundNBT ptmnbt = buffer.readCompoundTag();
		for (String key : ptmnbt.keySet()) {
			stmap.put(key, ptmnbt.getString(key));
		}

		return stmap;
	}

	private static Set<String> readSet(PacketBuffer buffer) {

		Set<String> stmap = new HashSet<String>();
		CompoundNBT ptmnbt = buffer.readCompoundTag();
		for (String key : ptmnbt.keySet()) {
			stmap.add(ptmnbt.getString(key));
		}

		return stmap;
	}

	public static void encodeMessege(CassetteDeckSyncMessage messegeIn, PacketBuffer buffer) {
		buffer.writeCompoundTag(ItemStackHelper.saveAllItems(new CompoundNBT(), messegeIn.items));
		buffer.writeInt(messegeIn.dim);
		buffer.writeInt(messegeIn.pos.getX());
		buffer.writeInt(messegeIn.pos.getY());
		buffer.writeInt(messegeIn.pos.getZ());
		buffer.writeInt(messegeIn.pitch);
		buffer.writeInt(messegeIn.yaw);
		buffer.writeString(messegeIn.selectedfolder);
		buffer.writeString(messegeIn.selectfile);

		CompoundNBT ptmnbt = new CompoundNBT();
		for (Entry<String, String> ent : messegeIn.playerstager.entrySet()) {
			ptmnbt.putString(ent.getKey(), ent.getValue());
		}
		buffer.writeCompoundTag(ptmnbt);
		buffer.writeInt(messegeIn.recordingPrograse);
		CompoundNBT ptamnbt = new CompoundNBT();
		int cont = 0;
		for (String st : messegeIn.lisnFinishedPlayers) {
			ptamnbt.putString(String.valueOf(cont), st);
			cont++;
		}
		buffer.writeCompoundTag(ptamnbt);
		buffer.writeInt(messegeIn.copyingPrograse);
		buffer.writeInt(messegeIn.deletingPrograse);
	}
}
