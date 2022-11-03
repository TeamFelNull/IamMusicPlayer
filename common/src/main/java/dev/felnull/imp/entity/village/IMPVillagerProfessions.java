package dev.felnull.imp.entity.village;

import com.google.common.collect.ImmutableSet;
import dev.architectury.registry.level.entity.trade.SimpleTrade;
import dev.architectury.registry.level.entity.trade.TradeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
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
import net.minecraft.world.level.ItemLike;

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
        registerBuying(DJ.get(), 1, IMPItems.CASSETTE_TAPE.get(), 1, 1, 10, 2);
        registerBuying(DJ.get(), 1, IMPItems.CASSETTE_TAPE_GLASS.get(), 1, 1, 10, 2);
        registerBuying(DJ.get(), 2, IMPBlocks.MUSIC_MANAGER.get(), 18, 1, 1, 12);
        registerBuying(DJ.get(), 3, IMPBlocks.CASSETTE_DECK.get(), 13, 1, 1, 12);
        registerBuying(DJ.get(), 4, IMPBlocks.BOOMBOX.get(), 15, 1, 3, 12);
        registerBuying(DJ.get(), 5, IMPItems.ANTENNA.get(), 15, 1, 1, 23);
        registerBuying(DJ.get(), 5, IMPItems.PARABOLIC_ANTENNA.get(), 33, 1, 1, 23);

        registerSelling(DJ.get(), 1, Items.DRIED_KELP, 12, 15, 2);
        registerSelling(DJ.get(), 2, Items.NOTE_BLOCK, 8, 10, 13);
        registerSelling(DJ.get(), 2, Items.REDSTONE, 12, 15, 2);
        registerSelling(DJ.get(), 3, Items.JUKEBOX, 1, 5, 20);

        TradeRegistry.registerTradeForWanderingTrader(true, new SimpleTrade(new ItemStack(Items.EMERALD, 42), ItemStack.EMPTY, IMPItemUtil.createKamesutaAntenna(), 1, 10, 0.05f));
    }

    public static void registerBuying(VillagerProfession profession, int level, ItemLike item, int emeraldCost, int numberOfItems, int maxUses, int villagerXp){
        TradeRegistry.registerVillagerTrade(profession, level, OERegistryUtil.createTradeItemsForEmeralds(new ItemStack(item), emeraldCost, numberOfItems, maxUses, villagerXp));
    }

    public static void registerSelling(VillagerProfession profession, int level, ItemLike item, int cost, int maxUses, int villagerXp){
        TradeRegistry.registerVillagerTrade(profession, level, OERegistryUtil.createTradeEmeraldForItems(item, cost, maxUses, villagerXp));
    }

}
