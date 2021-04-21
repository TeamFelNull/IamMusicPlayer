package red.felnull.imp.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AntennaSlot extends Slot {
    public AntennaSlot(Container container, int index, int xPosition, int yPosition) {
        super(container, index, xPosition, yPosition);
    }

    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() == Items.APPLE;
    }
}
