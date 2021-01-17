package red.felnull.imp.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.CompoundNBT;
import red.felnull.imp.item.CassetteTapeItem;
import red.felnull.imp.item.IMPItemTags;
import red.felnull.imp.music.resource.PlayMusic;

public class ItemHelper {
    public static boolean isAntenna(ItemStack stack) {
        return IMPItemTags.PARABOLIC_ANTENNA.contains(stack.getItem());
    }

    public static boolean isCassetteTape(ItemStack stack) {
        return stack.getItem() instanceof CassetteTapeItem;
    }

    public static boolean isWrittenCassetteTape(ItemStack itemstack) {
        CompoundNBT tag = itemstack.getTag();
        return tag != null && tag.contains("MusicUUID") && tag.contains("Music");
    }

    public static boolean isMusicItem(ItemStack stack) {
        return stack.getItem() instanceof MusicDiscItem;
    }

    public static ItemStack writtenCassetteTape(ItemStack casstape, PlayMusic music) {
        ItemStack itemstack = casstape.copy();
        CompoundNBT tag = itemstack.getOrCreateTag();
        tag.putString("MusicUUID", music.getUUID());
        tag.put("Music", music.write(new CompoundNBT()));
        return itemstack;
    }

    public static ItemStack erasureCassetteTape(ItemStack casstape) {
        if (!isWrittenCassetteTape(casstape))
            return casstape;
        ItemStack itemstack = casstape.copy();
        CompoundNBT tag = itemstack.getOrCreateTag();
        tag.remove("MusicUUID");
        tag.remove("Music");
        return itemstack;
    }


    public static PlayMusic getPlayMusicByItem(ItemStack itemStack) {
        CompoundNBT tag = itemStack.getTag();
        if (tag != null)
            return new PlayMusic(tag.getString("MusicUUID"), tag.getCompound("Music"));
        return null;
    }
}
