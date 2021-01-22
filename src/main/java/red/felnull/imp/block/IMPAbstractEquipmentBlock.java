package red.felnull.imp.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import red.felnull.imp.block.propertie.IMPBlockStateProperties;
import red.felnull.imp.ffmpeg.FFmpegManeger;
import red.felnull.imp.tileentity.IMPAbstractEquipmentTileEntity;
import red.felnull.otyacraftengine.util.IKSGEntityUtil;

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
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            FFmpegManeger maneger = FFmpegManeger.instance();
            if (maneger.canUseFFmpeg()) {
                this.interactWith(worldIn, pos, playerIn);
            } else {
                maneger.cantFFmpegCaution(playerIn);
            }
            return ActionResultType.CONSUME;
        }
    }

    protected abstract void interactWith(World worldIn, BlockPos pos, PlayerEntity player);


    @Override
    public boolean isStickingBlock() {
        return true;
    }
}
