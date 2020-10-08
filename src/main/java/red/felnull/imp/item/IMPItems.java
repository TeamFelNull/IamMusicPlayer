package red.felnull.imp.item;

import net.minecraft.item.Item;
import red.felnull.imp.IamMusicPlayer;

import java.util.ArrayList;
import java.util.List;

public class IMPItems {
    public static List<Item> MOD_ITEMS = new ArrayList<Item>();

   // public static final Item TEST_SOUND = register("test_sound", new TestSoundItem(new Item.Properties().group(IMPItemGroup.MOD_TAB)));
    public static final Item PARABOLIC_ANTENNA = register("parabolic_antenna", new ParabolicAntennaItem(new Item.Properties().maxStackSize(1).group(IMPItemGroup.MOD_TAB)));
    public static final Item KAMESUTA_ANTENNA = register("kamesuta_antenna", new KamesutaParabolicAntennaItem(new Item.Properties().maxStackSize(1)));

    private static Item register(String name) {
        return register(name, new Item(new Item.Properties().group(IMPItemGroup.MOD_TAB)));
    }

    private static Item register(String name, Item item) {
        MOD_ITEMS.add(item.setRegistryName(IamMusicPlayer.MODID, name));
        return item;
    }

}
