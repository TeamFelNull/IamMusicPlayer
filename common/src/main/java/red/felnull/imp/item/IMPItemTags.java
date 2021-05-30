package red.felnull.imp.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.util.IKSGTagUtil;

public class IMPItemTags {
    public static final Tag.Named<Item> PARABOLIC_ANTENNA = bind("parabolic_antenna");

    private static Tag.Named<Item> bind(String name) {
        return IKSGTagUtil.bindItemTag(new ResourceLocation(IamMusicPlayer.MODID, name));
    }
}
