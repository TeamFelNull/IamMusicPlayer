package net.morimori.imp.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class SoundFileUploaderContainer extends Container {
	public IInventory inventory;
	public BlockPos pos;

	public SoundFileUploaderContainer(int windowId, PlayerInventory playerInventory, IInventory inventoru,
			BlockPos pos) {
		super(IMPContainerTypes.SOUNDFILE_UPLOADER, windowId);
		this.inventory = inventoru;
		this.pos = pos;
		inventoru.openInventory(playerInventory.player);
		this.addSlot(new AntennaSlot(inventoru, 0, 183, 153));

		for (int i = 0; i < 3; ++i) {
			for (int i2 = 0; i2 < 9; ++i2) {
				this.addSlot(new Slot(playerInventory, i2 + i * 9 + 9, 8 + i2 * 18, 135 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 193));
		}
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {

		return inventory.isUsableByPlayer(playerIn);
	}

	@Override
	public void onContainerClosed(PlayerEntity p_75134_1_) {
		super.onContainerClosed(p_75134_1_);
		this.inventory.closeInventory(p_75134_1_);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity pl, int slotid) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot) this.inventorySlots.get(slotid);

		if (slot != null && slot.getHasStack()) {
			ItemStack slotitem = slot.getStack();
			itemstack = slotitem.copy();
			if (slotid == 0) {
				if (!this.mergeItemStack(slotitem, 1, 37, false)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(slotitem, itemstack);
			} else {
				if (!this.mergeItemStack(slotitem, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(slotitem, itemstack);
			}
		}
		return itemstack;
	}
}
