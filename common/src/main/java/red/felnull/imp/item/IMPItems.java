package red.felnull.imp.item;

import me.shedaniel.architectury.registry.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import red.felnull.imp.IamMusicPlayer;

public class IMPItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(IamMusicPlayer.MODID, Registry.ITEM_REGISTRY);
    public static final Item TEST_SOUND = register("test_sound", new TestSoundItem(new Item.Properties().tab(IMPCreativeModeTab.MOD_TAB)));
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
        return register(name, new Item(new Item.Properties().tab(IMPCreativeModeTab.MOD_TAB)));
    }

    private static Item register(String name, Item item) {
        ITEMS.register(name, () -> item);
        return item;
    }

    public static void init() {
        ITEMS.register();
    }
}
