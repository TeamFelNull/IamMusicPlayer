package red.felnull.imp.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import red.felnull.imp.util.ItemHelper;

public class MusicItemSlot extends Slot {
    public MusicItemSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    public boolean isItemValid(ItemStack stack) {
        return ItemHelper.isMusicItem(stack);
    }
}
