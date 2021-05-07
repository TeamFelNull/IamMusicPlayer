package red.felnull.imp.item;

import me.shedaniel.architectury.registry.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import red.felnull.imp.IamMusicPlayer;

public class IMPItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(IamMusicPlayer.MODID, Registry.ITEM_REGISTRY);
    public static final Item TEST_SOUND = register("test_sound", new TestSoundItem(new Item.Properties().tab(IMPCreativeModeTab.MOD_TAB)));
    public static final Item MAGNETIC_TAPE = register("magnetic_tape");

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
