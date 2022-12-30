package dev.felnull.imp.entity.village;

import dev.architectury.registry.registries.DeferredRegister;
import dev.felnull.imp.IamMusicPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class IMPPoiType {
    private static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(IamMusicPlayer.MODID, Registries.POINT_OF_INTEREST_TYPE);
    /*public static final RegistrySupplier<PoiType> DJ = register("dj", IMPBlocks.BOOMBOX);

    private static RegistrySupplier<PoiType> register(String name, Supplier<Block> blockSupplier) {
        return POI_TYPES.register(name, () -> new PoiType(OERegisterUtils.getPoiTypeBlockStates(blockSupplier.get()), 1, 1));
    }*/

    public static void init() {
        POI_TYPES.register();
      //  OERegisterUtils.registerPoiTypeBlockStates(DJ);
    }
}
