package red.felnull.imp.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class CassetteStoringContainer extends Container {
	public IInventory inventory;
	public BlockPos pos;

	public CassetteStoringContainer(int windowId, PlayerInventory playerInventory, IInventory inventory,
			BlockPos pos) {
		super(IMPContainerTypes.CASSETTE_STORING, windowId);
		this.inventory = inventory;
		this.pos = pos;
		inventory.openInventory(playerInventory.player);

		for (int i = 0; i < 8; ++i) {
			this.addSlot(new CassetteSlot(inventory, i, 66, 8 + 18 * i));
		}
		for (int i = 0; i < 8; ++i) {
			this.addSlot(new CassetteSlot(inventory, 8 + i, 94, 8 + 18 * i));
		}

		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 156 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 214));
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
			if (slotid >= 0 && slotid <= 15) {
				if (!this.mergeItemStack(slotitem, 16, 52, false)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(slotitem, itemstack);

			} else {
				if (!this.mergeItemStack(slotitem, 0, 16, false)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(slotitem, itemstack);
			}
		}
		return itemstack;
	}
}
