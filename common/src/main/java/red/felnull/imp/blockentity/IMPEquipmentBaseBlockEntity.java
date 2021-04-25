package red.felnull.imp.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import red.felnull.imp.block.IMPEquipmentBaseBlock;
import red.felnull.otyacraftengine.blockentity.container.IkisugiItemContainerBlockEntity;

public abstract class IMPEquipmentBaseBlockEntity extends IkisugiItemContainerBlockEntity {
    protected IMPEquipmentBaseBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public CompoundTag instructionFromClient(ServerPlayer player, String name, CompoundTag data) {
        if (name.equals("Power")) {
            setBlockStated(getBlockState().setValue(IMPEquipmentBaseBlock.ON, data.getBoolean("On")));
        }
        return super.instructionFromClient(player, name, data);
    }

    public boolean isPowerOn() {
        return getBlockState().getValue(IMPEquipmentBaseBlock.ON);
    }

}