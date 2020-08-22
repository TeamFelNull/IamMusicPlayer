package red.felnull.imp.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import red.felnull.otyacraftengine.container.IkisugiContainer;

public class MusicSharingDeviceContainer extends IkisugiContainer {
    public MusicSharingDeviceContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, BlockPos pos) {
        super(IMPContainerTypes.MUSIC_SHARING_DEVICE, windowId, playerInventory, inventory, pos, 8, 160);
    }

    @Override
    protected void setSlot() {
        this.addSlot(new AntennaSlot(inventory, 0, 183, 178));
    }


    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return inventory.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotitem = slot.getStack();
            itemstack = slotitem.copy();
            if (index == 0) {
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
