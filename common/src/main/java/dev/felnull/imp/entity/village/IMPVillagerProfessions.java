package dev.felnull.imp.entity.village;

import com.google.common.collect.ImmutableSet;
import dev.architectury.registry.level.entity.trade.SimpleTrade;
import dev.architectury.registry.level.entity.trade.TradeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.item.IMPItems;
import dev.felnull.imp.util.IMPItemUtil;
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
    public static final VillagerProfession DJ = register("dj", IMPPoiType.DJ, SoundEvents.LANTERN_PLACE);

    private static VillagerProfession register(String name, PoiType poiType, SoundEvent soundEvent) {
        var pr = OERegistryUtil.createVillagerProfession(new ResourceLocation(IamMusicPlayer.MODID, name), poiType, ImmutableSet.of(), ImmutableSet.of(), soundEvent);
        VILLAGER_PROFESSIONS.register(name, () -> pr);
        return pr;
    }

    public static void init() {
        VILLAGER_PROFESSIONS.register();
        TradeRegistry.registerVillagerTrade(DJ, 1, OERegistryUtil.createTradeItemsForEmeralds(new ItemStack(IMPItems.CASSETTE_TAPE), 1, 1, 10, 2));
        TradeRegistry.registerVillagerTrade(DJ, 1, OERegistryUtil.createTradeItemsForEmeralds(new ItemStack(IMPItems.CASSETTE_TAPE_GLASS), 1, 1, 10, 2));
        TradeRegistry.registerVillagerTrade(DJ, 2, OERegistryUtil.createTradeItemsForEmeralds(new ItemStack(IMPBlocks.getItemByBlock(IMPBlocks.MUSIC_MANAGER)), 18, 1, 1, 12));
        TradeRegistry.registerVillagerTrade(DJ, 3, OERegistryUtil.createTradeItemsForEmeralds(new ItemStack(IMPBlocks.getItemByBlock(IMPBlocks.CASSETTE_DECK)), 13, 1, 1, 12));
        TradeRegistry.registerVillagerTrade(DJ, 4, OERegistryUtil.createTradeItemsForEmeralds(new ItemStack(IMPBlocks.getItemByBlock(IMPBlocks.BOOMBOX)), 15, 1, 3, 12));
        TradeRegistry.registerVillagerTrade(DJ, 5, OERegistryUtil.createTradeItemsForEmeralds(new ItemStack(IMPItems.ANTENNA), 15, 1, 1, 23));
        TradeRegistry.registerVillagerTrade(DJ, 5, OERegistryUtil.createTradeItemsForEmeralds(new ItemStack(IMPItems.PARABOLIC_ANTENNA), 33, 1, 1, 23));

        TradeRegistry.registerVillagerTrade(DJ, 1, OERegistryUtil.createTradeEmeraldForItems(Items.DRIED_KELP, 12, 15, 2));
        TradeRegistry.registerVillagerTrade(DJ, 2, OERegistryUtil.createTradeEmeraldForItems(Items.NOTE_BLOCK, 8, 10, 13));
        TradeRegistry.registerVillagerTrade(DJ, 2, OERegistryUtil.createTradeEmeraldForItems(Items.REDSTONE, 12, 15, 2));
        TradeRegistry.registerVillagerTrade(DJ, 3, OERegistryUtil.createTradeEmeraldForItems(Items.JUKEBOX, 1, 5, 20));

        TradeRegistry.registerTradeForWanderingTrader(true, new SimpleTrade(new ItemStack(Items.EMERALD, 42), ItemStack.EMPTY, IMPItemUtil.createKamesutaAntenna(), 1, 10, 0.05f));
    }
}
