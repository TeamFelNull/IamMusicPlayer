package red.felnull.imp.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;

import java.util.ArrayList;
import java.util.List;

public class IMPItems {
    public static List<Item> MOD_ITEMS = new ArrayList<Item>();

    //  public static final Item TEST_SOUND = register("test_sound", new TestSoundItem(new Item.Properties().group(IMPItemGroup.MOD_TAB)));
    public static final Item PARABOLIC_ANTENNA = register("parabolic_antenna", new ParabolicAntennaItem(new Item.Properties().maxStackSize(1).group(IMPItemGroup.MOD_TAB), new ResourceLocation(IamMusicPlayer.MODID, "item/parabolic_antenna"), 1));
    public static final Item KAMESUTA_ANTENNA = register("kamesuta_antenna", new ParabolicAntennaItem(new Item.Properties().maxStackSize(1), new ResourceLocation(IamMusicPlayer.MODID, "item/kamesuta_antenna"), 10));
    public static final Item KATYOU_ANTENNA = register("katyou_antenna", new ParabolicAntennaItem(new Item.Properties().maxStackSize(1), new ResourceLocation(IamMusicPlayer.MODID, "item/katyou_antenna"), 10));
    public static final Item IKISUGI_ANTENNA = register("ikisugi_antenna", new ParabolicAntennaItem(new Item.Properties().maxStackSize(1), new ResourceLocation(IamMusicPlayer.MODID, "item/ikisugi_antenna"), 10));
    public static final Item CASSETTE_TAPE = register("cassette_tape", new CassetteTapeItem(new Item.Properties().maxStackSize(1).group(IMPItemGroup.MOD_TAB)));
    public static final Item ACOUSTIC_AMPLIFIER = register("acoustic_amplifier");
    public static final Item CASSETTE_PLAYER = register("cassette_player");
    public static final Item IRON_BOWL = register("iron_bowl");
    public static final Item IRON_STICK = register("iron_stick");
    public static final Item KEYBOARD = register("keyboard");
    public static final Item MAGNETIC_TAPE = register("magnetic_tape");
    public static final Item RECEIVER = register("receiver");
    public static final Item SPEAKER = register("speaker");
    public static final Item COMPUTER = register("computer");
    public static final Item MONITOR = register("monitor");

    private static Item register(String name) {
        return register(name, new Item(new Item.Properties().group(IMPItemGroup.MOD_TAB)));
    }

    private static Item register(String name, Item item) {
        MOD_ITEMS.add(item.setRegistryName(IamMusicPlayer.MODID, name));
        return item;
    }

}
