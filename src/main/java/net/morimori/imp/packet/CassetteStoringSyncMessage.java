package net.morimori.imp.packet;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class CassetteStoringSyncMessage {
	public int dim;
	public BlockPos pos;
	public NonNullList<ItemStack> items;

	public CassetteStoringSyncMessage(int dimID, BlockPos postion, NonNullList<ItemStack> Item) {
		this.dim = dimID;
		this.pos = postion;
		this.items = Item;
	}

	public static CassetteStoringSyncMessage decodeMessege(PacketBuffer buffer) {
		NonNullList<ItemStack> itemsa = NonNullList.withSize(16, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(buffer.readCompoundTag(), itemsa);

		return new CassetteStoringSyncMessage(buffer.readInt(),
				new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()), itemsa);
	}

	public static void encodeMessege(CassetteStoringSyncMessage messegeIn, PacketBuffer buffer) {
		buffer.writeCompoundTag(ItemStackHelper.saveAllItems(new CompoundNBT(), messegeIn.items));
		buffer.writeInt(messegeIn.dim);
		buffer.writeInt(messegeIn.pos.getX());
		buffer.writeInt(messegeIn.pos.getY());
		buffer.writeInt(messegeIn.pos.getZ());
	}
}
