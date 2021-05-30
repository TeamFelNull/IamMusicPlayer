package red.felnull.imp.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import red.felnull.imp.util.ItemHelper;

public class AntennaSlot extends Slot {
    public AntennaSlot(Container container, int index, int xPosition, int yPosition) {
        super(container, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return ItemHelper.isAntenna(stack);
    }
}
