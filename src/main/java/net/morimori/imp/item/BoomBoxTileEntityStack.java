package net.morimori.imp.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class BoomBoxTileEntityStack extends TileEntityStack {
	protected ItemStack cassette = ItemStack.EMPTY;
	public int openProgress;

	public BoomBoxTileEntityStack(ItemStack item) {
		super(item);

	}

	public void read(CompoundNBT tag) {
		super.read(tag);
		this.cassette = ItemStack.read(tag.getCompound("CassetteItem"));
		this.openProgress = tag.getInt("OpenProgress");
	}

	public CompoundNBT write(CompoundNBT tag) {
		tag.put("CassetteItem", cassette.write(new CompoundNBT()));
		tag.putInt("OpenProgress", this.openProgress);
		return super.write(tag);
	}

}
