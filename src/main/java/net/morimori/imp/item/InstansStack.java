package net.morimori.imp.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class InstansStack {
	private ItemStack item;

	public InstansStack() {

	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public void read(CompoundNBT tag) {

	}

	public CompoundNBT write(CompoundNBT tag) {
		return tag;
	}

}
