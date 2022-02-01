package dev.felnull.imp.item;

import dev.felnull.otyacraftengine.item.IEquipmentItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class ParabolicAntennaItem extends AntennaItem implements IEquipmentItem {
    public ParabolicAntennaItem(Properties properties) {
        super(properties);
    }

    @Override
    public EquipmentSlot getEquipmentSlotType(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }
}
