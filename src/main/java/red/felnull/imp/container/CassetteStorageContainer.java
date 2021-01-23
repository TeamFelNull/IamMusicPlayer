package red.felnull.imp.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import red.felnull.otyacraftengine.container.IkisugiContainer;

import java.util.stream.IntStream;


public class CassetteStorageContainer extends IkisugiContainer {
    public CassetteStorageContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, BlockPos pos) {
        super(IMPContainerTypes.CASSETTE_STORAGE, windowId, playerInventory, inventory, pos, 8, 179);
    }

    @Override
    protected void setPlayerSlot(int x, int y) {
        if (this.playerInventory != null) {
            IntStream.range(0, 3).forEach(n -> IntStream.range(0, 9).forEach(n1 -> this.addSlot(new Slot(this.playerInventory, n1 + n * 9 + 9, x + n1 * 18, y + n * 18))));
            IntStream.range(0, 3).forEach(n -> IntStream.range(0, 3).forEach(n1 -> this.addSlot(new Slot(this.playerInventory, n1 + n * 3, x + 166 + n1 * 18, y + n * 18))));
        }
    }

    @Override
    protected void setSlot() {
        IntStream.range(0, 8).forEach(n -> this.addSlot(new WrittenCassetteTapeSlot(inventory, n, 98, 18 + n * 18)));
        IntStream.range(0, 8).forEach(n -> this.addSlot(new WrittenCassetteTapeSlot(inventory, n + 8, 211, 18 + n * 18)));
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
            if (index >= 0 && index <= 15) {
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
