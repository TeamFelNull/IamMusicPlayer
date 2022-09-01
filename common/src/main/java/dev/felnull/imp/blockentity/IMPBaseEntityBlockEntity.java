package dev.felnull.imp.blockentity;

import dev.felnull.imp.block.IMPBaseEntityBlock;
import dev.felnull.otyacraftengine.blockentity.OEBaseContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class IMPBaseEntityBlockEntity extends OEBaseContainerBlockEntity {
    protected IMPBaseEntityBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public boolean isPowered() {
        return getBlockState().getValue(IMPBaseEntityBlock.POWERED);
    }

    public void setPower(boolean on) {
        if (on != isPowered())
            getLevel().setBlock(getBlockPos(), getBlockState().setValue(IMPBaseEntityBlock.POWERED, on), 3);
    }

    @Override
    public CompoundTag onInstruction(ServerPlayer player, String name, int num, CompoundTag data) {
        if ("power".equals(name)) {
            setPower(num == 1);
            return null;
        }
        return super.onInstruction(player, name, num, data);
    }
}
