package dev.felnull.imp.inventory.slot;

import dev.felnull.imp.item.ParabolicAntennaItem;
import dev.felnull.imp.util.IMPItemUtil;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AntennaSlot extends Slot {
    public AntennaSlot(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
       return IMPItemUtil.isAntenna(itemStack);
    }
}
