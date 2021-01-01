package red.felnull.imp.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.CompoundNBT;
import red.felnull.imp.item.CassetteTapeItem;
import red.felnull.imp.item.IMPItemTags;
import red.felnull.imp.musicplayer.PlayMusic;

public class ItemHelper {
    public static boolean isAntenna(ItemStack stack) {
        return IMPItemTags.PARABOLIC_ANTENNA.contains(stack.getItem());
    }

    public static boolean isCassetteTape(ItemStack stack) {
        return stack.getItem() instanceof CassetteTapeItem;
    }

    public static boolean isWrittenCassetteTape(ItemStack stack) {
        return stack.getItem() == Items.GOLDEN_APPLE;
    }

    public static boolean isMusicItem(ItemStack stack) {
        return stack.getItem() instanceof MusicDiscItem;
    }

    public static ItemStack writtenCassetteTape(ItemStack casstape, PlayMusic music) {
        ItemStack itemstack = casstape.copy();
        CompoundNBT tag = itemstack.getOrCreateTag();
        tag.put("music", createMusicNBTTag(music));
        return itemstack;
    }

    public static CompoundNBT createMusicNBTTag(PlayMusic music) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("name", music.getName());
        tag.putString("uuid", music.getUUID());
        return tag;
    }
}
