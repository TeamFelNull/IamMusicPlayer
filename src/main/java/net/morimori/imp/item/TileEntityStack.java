package net.morimori.imp.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;

public class TileEntityStack {
	private ItemStack item;
	public BlockState blockstate;

	public TileEntityStack(ItemStack item) {
		this.item = item;
		if (item.getOrCreateTag().contains("BlockEntityTag"))
			read((CompoundNBT) item.getOrCreateTag().get("BlockEntityTag"));
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public void read(CompoundNBT tag) {

		if (tag.contains("BlockState"))
			this.blockstate = NBTUtil.readBlockState((CompoundNBT) tag.get("BlockState"));
	}

	public CompoundNBT write(CompoundNBT tag) {

		tag.put("BlockState", NBTUtil.writeBlockState(blockstate));

		return tag;
	}

	public void load(TileEntity tile) {

		CompoundNBT tag = tile.write(new CompoundNBT());
		tag.put("BlockState", NBTUtil.writeBlockState(tile.getBlockState()));

		read(tag);
	}

	public void save() {
		CompoundNBT tag = item.getOrCreateTag();
		tag.put("BlockEntityTag", write(new CompoundNBT()));
	}

	public BlockState getBlockstate() {

		return blockstate;
	}

}
