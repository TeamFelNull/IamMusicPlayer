package red.felnull.imp.handler;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import red.felnull.imp.block.IMPBlocks;
import red.felnull.imp.container.IMPContainerTypes;
import red.felnull.imp.item.IMPItems;
import red.felnull.imp.tileentity.IMPTileEntityTypes;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler {
    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> e) {
        IMPItems.registerItem(e.getRegistry());
        IMPBlocks.registerItem(e.getRegistry());
    }

    @SubscribeEvent
    public static void onBlockRegistry(final RegistryEvent.Register<Block> e) {
        IMPBlocks.registerBlock(e.getRegistry());
    }

    @SubscribeEvent
    public static void onTileEntityTypeRegistry(final RegistryEvent.Register<TileEntityType<?>> e) {
        IMPTileEntityTypes.registerTileEntityType(e.getRegistry());
    }

    @SubscribeEvent
    public static void onContainerTypeRegistry(final RegistryEvent.Register<ContainerType<?>> e) {
        IMPContainerTypes.registerContainerType(e.getRegistry());
    }
}
