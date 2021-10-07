package red.felnull.imp.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import red.felnull.imp.block.propertie.IMPBlockStateProperties;

public abstract class IMPAbstractEquipmentBlock extends IMPAbstractBlock {
    public static final BooleanProperty ON = IMPBlockStateProperties.ON;

    public IMPAbstractEquipmentBlock(Properties builder) {
        super(builder.setLightLevel((state) -> state.get(ON) ? 13 : 0));
        this.setDefaultState(getDefaultState().with(ON, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> blockBlockStateBuilder) {
        super.fillStateContainer(blockBlockStateBuilder);
        blockBlockStateBuilder.add(ON);
    }

    @Override
    public final boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote)
            this.interactWith(worldIn, pos, playerIn);

        return ActionResultType.func_233537_a_(worldIn.isRemote);
    }

    protected abstract void interactWith(World worldIn, BlockPos pos, PlayerEntity player);


    @Override
    public boolean isStickingBlock() {
        return true;
    }
}
