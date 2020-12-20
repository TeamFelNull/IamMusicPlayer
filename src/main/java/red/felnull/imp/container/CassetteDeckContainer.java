package red.felnull.imp.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import red.felnull.otyacraftengine.container.IkisugiContainer;

public class CassetteDeckContainer extends IkisugiContainer {
    public CassetteDeckContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, BlockPos pos) {
        super(IMPContainerTypes.CASSETTE_DECK, windowId, playerInventory, inventory, pos, 8, 145);
    }

    @Override
    protected void setSlot() {
        this.addSlot(new AntennaSlot(inventory, 0, 182, 163));
        this.addSlot(new CassetteTapeSlot(inventory, 1, 95, 109));
        this.addSlot(new WrittenCassetteTapeSlot(inventory, 2, 145, 109));
        this.addSlot(new MusicItemSlot(inventory, 3, 45, 109));
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return inventory.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotitem = slot.getStack();
            itemstack = slotitem.copy();
            if (index >= 0 && index <= 3) {
                if (!this.mergeItemStack(slotitem, 4, 40, false)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotitem, itemstack);
            } else {
                if (!this.mergeItemStack(slotitem, 0, 4, false)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotitem, itemstack);
            }
        }
        return itemstack;
    }
}
