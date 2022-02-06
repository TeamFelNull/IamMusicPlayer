package dev.felnull.imp.block;

import dev.felnull.imp.blockentity.CassetteDeckBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CassetteDeckBlock extends IMPBaseEntityBlock {
    protected CassetteDeckBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CassetteDeckBlockEntity(blockPos, blockState);
    }
}
