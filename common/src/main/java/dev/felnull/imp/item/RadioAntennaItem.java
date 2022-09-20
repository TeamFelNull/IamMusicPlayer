package dev.felnull.imp.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RadioAntennaItem extends Item {
    private static final Component DESC = Component.translatable("item.iammusicplayer.radio_antenna.desc").withStyle(ChatFormatting.GRAY);

    public RadioAntennaItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(DESC);
    }
}
