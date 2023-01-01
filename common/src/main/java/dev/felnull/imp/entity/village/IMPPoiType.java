package dev.felnull.imp.entity.village;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.fnjl.util.FNDataUtil;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.otyacraftengine.util.OERegisterUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;
import java.util.function.Supplier;

public class IMPPoiType {
    private static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(IamMusicPlayer.MODID, Registries.POINT_OF_INTEREST_TYPE);
    private static final Function<PoiType, ResourceKey<PoiType>> RESOURCE_KEY_CACHE = FNDataUtil.memoize(poi -> BuiltInRegistries.POINT_OF_INTEREST_TYPE.getResourceKey(poi).orElseThrow());

    public static final RegistrySupplier<PoiType> DJ = register("dj", IMPBlocks.BOOMBOX, 1, 1);

    private static RegistrySupplier<PoiType> register(String name, Supplier<Block> block, int maxTickets, int validRange) {
        return POI_TYPES.register(name, () -> new PoiType(OERegisterUtils.getPoiTypeBlockStates(block.get()), maxTickets, validRange));
    }

    public static ResourceKey<PoiType> getResourceKey(PoiType poiType) {
        return RESOURCE_KEY_CACHE.apply(poiType);
    }

    public static void init() {
        POI_TYPES.register();
        OERegisterUtils.registerPoiTypeBlockStates(DJ);
    }
}
