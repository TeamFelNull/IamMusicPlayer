package dev.felnull.imp.entity.village;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.otyacraftengine.util.OERegisterUtils;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class IMPPoiType {
    private static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(IamMusicPlayer.MODID, Registry.POINT_OF_INTEREST_TYPE_REGISTRY);
    public static final RegistrySupplier<PoiType> DJ = register("dj", IMPBlocks.BOOMBOX);

    private static RegistrySupplier<PoiType> register(String name, Supplier<Block> blockSupplier) {
        return POI_TYPES.register(name, () -> new PoiType(OERegisterUtils.getPoiTypeBlockStates(blockSupplier.get()), 1, 1));
    }

    public static void init() {
        POI_TYPES.register();
        OERegisterUtils.registerPoiTypeBlockStates(DJ);
    }
}
