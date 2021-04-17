package red.felnull.imp.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import red.felnull.imp.blockentity.IMPBlockEntitys;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;

public class MusicSharingDeviceBlock extends IMPEquipmentBaseBlock {
    public MusicSharingDeviceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMPBlockEntitys.MUSIC_SHARING_DEVICE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MusicSharingDeviceBlockEntity(blockPos, blockState);
    }
}
