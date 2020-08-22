package red.felnull.imp.item;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import red.felnull.imp.IamMusicPlayer;

public class IMPItemTags {
    public static final ITag.INamedTag<Item> PARABOLIC_ANTENNA = impTag("parabolic_antenna");

    private static ITag.INamedTag<Item> fgTag(String name) {
        return ItemTags.makeWrapperTag("forge:" + name);
    }

    private static ITag.INamedTag<Item> impTag(String name) {
        return ItemTags.makeWrapperTag(IamMusicPlayer.MODID + ":" + name);
    }
}
