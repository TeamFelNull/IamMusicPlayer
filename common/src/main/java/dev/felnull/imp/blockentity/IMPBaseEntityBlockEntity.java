package dev.felnull.imp.blockentity;

import dev.felnull.imp.block.IMPBaseEntityBlock;
import dev.felnull.otyacraftengine.blockentity.OEBaseContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class IMPBaseEntityBlockEntity extends OEBaseContainerBlockEntity {
    protected IMPBaseEntityBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public boolean isPower() {
        return getBlockState().getValue(IMPBaseEntityBlock.POWERED);
    }
}
