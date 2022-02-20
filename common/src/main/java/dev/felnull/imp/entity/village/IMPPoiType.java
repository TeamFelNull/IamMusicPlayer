package dev.felnull.imp.entity.village;

import dev.architectury.registry.registries.DeferredRegister;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.otyacraftengine.util.OERegistryUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;

public class IMPPoiType {
    private static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(IamMusicPlayer.MODID, Registry.POINT_OF_INTEREST_TYPE_REGISTRY);
    public static final PoiType DJ = register("dj", IMPBlocks.BOOMBOX, 1, 1);

    private static PoiType register(String name, Block block, int i, int j) {
        var poiType = OERegistryUtil.createPoiType(new ResourceLocation(IamMusicPlayer.MODID, name), OERegistryUtil.getPoiTypeBlockStates(block), i, j);
        POI_TYPES.register(name, () -> poiType);
        OERegistryUtil.registerPoiTypeBlockStates(poiType);
        return poiType;
    }

    public static void init() {
        POI_TYPES.register();
    }
}
