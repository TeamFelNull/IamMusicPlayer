package red.felnull.imp.handler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import red.felnull.imp.block.IMPBlocks;
import red.felnull.imp.item.IMPItems;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler {
    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> e) {
        IMPItems.MOD_ITEMS.forEach(n -> e.getRegistry().register(n));
        IMPBlocks.MOD_BLOCKITEMS.forEach(n -> e.getRegistry().register(n));
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> e) {
        IMPBlocks.MOD_BLOCKS.forEach(n -> e.getRegistry().register(n));
    }
}
