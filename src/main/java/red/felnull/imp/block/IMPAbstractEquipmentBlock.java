package red.felnull.imp.block;

import net.minecraft.block.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import red.felnull.imp.block.propertie.IMPBlockStateProperties;
import red.felnull.imp.ffmpeg.FFmpegManeger;
import red.felnull.imp.tileentity.IMPAbstractEquipmentTileEntity;
import red.felnull.otyacraftengine.util.IKSGEntityUtil;

import java.util.function.ToIntFunction;

public abstract class IMPAbstractEquipmentBlock extends HorizontalBlock implements IWaterLoggable {

    public static final BooleanProperty ON = IMPBlockStateProperties.ON;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public IMPAbstractEquipmentBlock(Properties builder) {
        super(builder.setLightLevel(getLightValueOn(13)));
        this.setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, Boolean.valueOf(false)).with(ON, Boolean.valueOf(false)));
    }

    private static ToIntFunction<BlockState> getLightValueOn(int lightValue) {
        return (state) -> state.get(ON) ? lightValue : 0;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        World world = context.getWorld();
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(ON, false).with(WATERLOGGED, Boolean.valueOf(world.getFluidState(blockpos).getFluid() == Fluids.WATER));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> blockBlockStateBuilder) {
        blockBlockStateBuilder.add(HORIZONTAL_FACING, ON, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = state.get(HorizontalBlock.HORIZONTAL_FACING);
        return getFacingShape(state, worldIn, pos, context, direction);
    }

    public abstract VoxelShape getFacingShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context, Direction direction);

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        return facing == Direction.DOWN && !this.isValidPosition(stateIn, worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return hasEnoughSolidSide(worldIn, pos.down(), Direction.UP);
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
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public void onReplaced(BlockState before, World worldIn, BlockPos pos, BlockState after, boolean isMoving) {
        if (before.getBlock() != after.getBlock() && worldIn.getTileEntity(pos) instanceof IMPAbstractEquipmentTileEntity) {
            IMPAbstractEquipmentTileEntity tileentity = (IMPAbstractEquipmentTileEntity) worldIn.getTileEntity(pos);
            tileentity.getItems().forEach(n -> {
                ItemEntity dropItem = IKSGEntityUtil.createItemEntity(n, worldIn, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
                worldIn.addEntity(dropItem);
            });
            tileentity.clear();
        }
        super.onReplaced(before, worldIn, pos, after, isMoving);
    }
}
