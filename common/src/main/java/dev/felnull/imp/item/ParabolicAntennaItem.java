package dev.felnull.imp.item;

import dev.felnull.otyacraftengine.item.EquipmentItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ParabolicAntennaItem extends RadioAntennaItem implements EquipmentItem {
    private static final Component DESC = Component.translatable("item.iammusicplayer.parabolic_antenna.desc").withStyle(ChatFormatting.GRAY);

    public ParabolicAntennaItem(Properties properties) {
        super(properties);
    }

    @Override
    public EquipmentSlot getEquipmentSlotType(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(DESC);
    }
}
