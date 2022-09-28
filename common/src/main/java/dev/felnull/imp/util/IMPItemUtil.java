package dev.felnull.imp.util;

import dev.felnull.imp.item.CassetteTapeItem;
import dev.felnull.imp.item.IMPItems;
import dev.felnull.imp.item.RadioAntennaItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

public class IMPItemUtil {
    public static boolean isCassetteTape(ItemStack itemStack) {
        return itemStack.getItem() instanceof CassetteTapeItem;
    }

    public static boolean isAntenna(ItemStack itemStack) {
        return itemStack.getItem() instanceof RadioAntennaItem;
    }

    public static boolean isRemotePlayBackAntenna(ItemStack stack) {
        return isAntenna(stack) && stack.is(IMPItems.PARABOLIC_ANTENNA.get());
    }

    public static boolean isRadioAntenna(ItemStack stack) {
        return isAntenna(stack) && stack.is(IMPItems.RADIO_ANTENNA.get());
    }

    public static ItemStack createKamesutaAntenna() {
        var st = new ItemStack(IMPItems.PARABOLIC_ANTENNA.get());
        st.setHoverName(Component.literal("Kamesuta").withStyle(ChatFormatting.GREEN).withStyle(Style.EMPTY.withItalic(false)));
        return st;
    }
}
