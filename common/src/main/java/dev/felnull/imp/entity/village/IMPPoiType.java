package dev.felnull.imp.entity.village;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.block.IMPBlocks;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;

public class IMPPoiType {
    private static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(IamMusicPlayer.MODID, Registry.POINT_OF_INTEREST_TYPE_REGISTRY);
    public static final RegistrySupplier<PoiType> DJ = register("dj", IMPBlocks.BOOMBOX);

    private static RegistrySupplier<PoiType> register(String name, RegistrySupplier<Block> block) {
        /*return POI_TYPES.register(name, () -> {
            //var poiType = new PoiType(ImmutableSet.of(block.get()))//OERegistryUtil.createPoiType(new ResourceLocation(IamMusicPlayer.MODID, name), OERegistryUtil.getPoiTypeBlockStates(block.get()), 1, 1);
            //OERegistryUtil.registerPoiTypeBlockStates(poiType);
            return new PoiType(ImmutableSet.of(block.get().defaultBlockState()), 1, 1);
        });*/
        return null;
    }

    public static void init() {
        //  POI_TYPES.register();
    }
}
