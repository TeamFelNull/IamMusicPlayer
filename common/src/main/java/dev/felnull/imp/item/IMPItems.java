package dev.felnull.imp.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.imp.IamMusicPlayer;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class IMPItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(IamMusicPlayer.MODID, Registry.ITEM_REGISTRY);
    // public static final Item TEST_SOUND = register("test_sound", new TestSoundItem(new Item.Properties().tab(IMPCreativeModeTab.MOD_TAB)));
    public static final RegistrySupplier<Item> ANTENNA = register("antenna", () -> new AntennaItem(new Item.Properties().tab(IMPCreativeModeTab.MOD_TAB).stacksTo(1)));
    public static final RegistrySupplier<Item> PARABOLIC_ANTENNA = register("parabolic_antenna", () -> new ParabolicAntennaItem(new Item.Properties().tab(IMPCreativeModeTab.MOD_TAB).stacksTo(1)));
    public static final RegistrySupplier<Item> CASSETTE_TAPE = register("cassette_tape", () -> new CassetteTapeItem(new Item.Properties().tab(IMPCreativeModeTab.MOD_TAB).stacksTo(1), CassetteTapeItem.BaseType.NORMAL));
    public static final RegistrySupplier<Item> CASSETTE_TAPE_GLASS = register("cassette_tape_glass", () -> new CassetteTapeItem(new Item.Properties().tab(IMPCreativeModeTab.MOD_TAB).stacksTo(1), CassetteTapeItem.BaseType.GLASS));
    public static final RegistrySupplier<Item> MANUAL = register("manual", () -> new ManualItem(new Item.Properties().tab(IMPCreativeModeTab.MOD_TAB).stacksTo(1)));

    private static RegistrySupplier<Item> register(String name) {
        return register(name, () -> new Item(new Item.Properties().tab(IMPCreativeModeTab.MOD_TAB)));
    }

    private static RegistrySupplier<Item> register(String name, Supplier<Item> item) {
        return ITEMS.register(name, item);
    }

    public static void init() {
        ITEMS.register();
    }
}
