package red.felnull.imp.item;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import red.felnull.imp.IamMusicPlayer;

public class IMPItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(IamMusicPlayer.MODID, Registry.ITEM_REGISTRY);
    public static final Item TEST_SOUND = register("test_sound", new TestSoundItem(new Item.Properties().tab(IMPCreativeModeTab.MOD_TAB)));
    public static final Item MAGNETIC_TAPE = register("magnetic_tape");

    public static final Item PARABOLIC_ANTENNA = register("parabolic_antenna", new ParabolicAntennaItem(new Item.Properties().tab(IMPCreativeModeTab.MOD_TAB), 1f));
    public static final Item KAMESUTA_ANTENNA = register("kamesuta_antenna", new ParabolicAntennaItem(new Item.Properties(), 3f));
    public static final Item IKISUGI_ANTENNA = register("ikisugi_antenna", new ParabolicAntennaItem(new Item.Properties(), 19f));
    public static final Item KATYOU_ANTENNA = register("katyou_antenna", new ParabolicAntennaItem(new Item.Properties(), 2f));
    public static final Item FCOH_ANTENNA = register("fcoh_antenna", new ParabolicAntennaItem(new Item.Properties(), 2f));

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
