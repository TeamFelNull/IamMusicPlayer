package red.felnull.imp.blockentity;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.block.IMPBlocks;
import red.felnull.otyacraftengine.util.IKSGBlockEntityUtil;

public class IMPBlockEntitys {
    private static final DeferredRegister<BlockEntityType<?>> ENTITY_TYPES = DeferredRegister.create(IamMusicPlayer.MODID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    public static final BlockEntityType<MusicSharingDeviceBlockEntity> MUSIC_SHARING_DEVICE = register("music_sharing_device", MusicSharingDeviceBlockEntity::new, IMPBlocks.MUSIC_SHARING_DEVICE);

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, IKSGBlockEntityUtil.IKSGBlockEntitySupplier<? extends BlockEntity> blockEntitySupplier, Block... blocks) {
        BlockEntityType<T> be = IKSGBlockEntityUtil.craeteBlockEntityType(blockEntitySupplier, blocks);
        ENTITY_TYPES.register(name, () -> be);
        return be;
    }

    public static void init() {
        ENTITY_TYPES.register();
    }
}
