package dev.felnull.imp.entity.village;

import com.google.common.collect.ImmutableSet;
import dev.architectury.registry.level.entity.trade.SimpleTrade;
import dev.architectury.registry.level.entity.trade.TradeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.item.IMPItems;
import dev.felnull.otyacraftengine.util.OERegistryUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class IMPVillagerProfessions {
    private static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(IamMusicPlayer.MODID, Registry.VILLAGER_PROFESSION_REGISTRY);
    public static final RegistrySupplier<VillagerProfession> DJ = register("dj", IMPPoiType.DJ, SoundEvents.LANTERN_PLACE);

    private static RegistrySupplier<VillagerProfession> register(String name, RegistrySupplier<PoiType> poiType, SoundEvent soundEvent) {
        return VILLAGER_PROFESSIONS.register(name, () -> OERegistryUtil.createVillagerProfession(new ResourceLocation(IamMusicPlayer.MODID, name), poiType.get(), ImmutableSet.of(), ImmutableSet.of(), soundEvent));
    }

    public static void init() {
        VILLAGER_PROFESSIONS.register();
    }

    public static void setup() {
        TradeRegistry.registerVillagerTrade(DJ.get(), 1, OERegistryUtil.createTradeItemsForEmeralds(new ItemStack(IMPItems.CASSETTE_TAPE.get()), 1, 1, 10, 2));
        TradeRegistry.registerVillagerTrade(DJ.get(), 1, OERegistryUtil.createTradeItemsForEmeralds(new ItemStack(IMPItems.CASSETTE_TAPE_GLASS.get()), 1, 1, 10, 2));
        TradeRegistry.registerVillagerTrade(DJ.get(), 2, OERegistryUtil.createTradeItemsForEmeralds(new ItemStack(IMPBlocks.MUSIC_MANAGER.get()), 18, 1, 1, 12));
        TradeRegistry.registerVillagerTrade(DJ.get(), 3, OERegistryUtil.createTradeItemsForEmeralds(new ItemStack(IMPBlocks.CASSETTE_DECK.get()), 13, 1, 1, 12));
        TradeRegistry.registerVillagerTrade(DJ.get(), 4, OERegistryUtil.createTradeItemsForEmeralds(new ItemStack(IMPBlocks.BOOMBOX.get()), 15, 1, 3, 12));
        TradeRegistry.registerVillagerTrade(DJ.get(), 5, OERegistryUtil.createTradeItemsForEmeralds(new ItemStack(IMPItems.ANTENNA.get()), 15, 1, 1, 23));
        TradeRegistry.registerVillagerTrade(DJ.get(), 5, OERegistryUtil.createTradeItemsForEmeralds(new ItemStack(IMPItems.PARABOLIC_ANTENNA.get()), 33, 1, 1, 23));

        TradeRegistry.registerVillagerTrade(DJ.get(), 1, OERegistryUtil.createTradeEmeraldForItems(Items.DRIED_KELP, 12, 15, 2));
        TradeRegistry.registerVillagerTrade(DJ.get(), 2, OERegistryUtil.createTradeEmeraldForItems(Items.NOTE_BLOCK, 8, 10, 13));
        TradeRegistry.registerVillagerTrade(DJ.get(), 2, OERegistryUtil.createTradeEmeraldForItems(Items.REDSTONE, 12, 15, 2));
        TradeRegistry.registerVillagerTrade(DJ.get(), 3, OERegistryUtil.createTradeEmeraldForItems(Items.JUKEBOX, 1, 5, 20));

//        TradeRegistry.registerTradeForWanderingTrader(true, new SimpleTrade(new ItemStack(Items.EMERALD, 42), ItemStack.EMPTY, IMPItemUtil.createKamesutaAntenna(), 1, 10, 0.05f));
        TradeRegistry.registerTradeForWanderingTrader(true, new SimpleTrade(new ItemStack(Items.EMERALD, 42), ItemStack.EMPTY, new ItemStack(IMPItems.PARABOLIC_ANTENNA.get()), 1, 10, 0.05f));
    }
}
