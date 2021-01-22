package red.felnull.imp.block;

import net.minecraft.block.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import red.felnull.imp.block.propertie.IMPBlockStateProperties;
import red.felnull.imp.tileentity.IMPAbstractEquipmentTileEntity;
import red.felnull.imp.tileentity.IMPAbstractTileEntity;
import red.felnull.otyacraftengine.util.IKSGEntityUtil;

public abstract class IMPAbstractBlock extends HorizontalBlock implements IWaterLoggable {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty WALL = IMPBlockStateProperties.WALL;

    public IMPAbstractBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, false));
        if (isWallHanging())
            this.setDefaultState(getDefaultState().with(WALL, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        World world = context.getWorld();
        BlockState state = this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(WATERLOGGED, world.getFluidState(blockpos).getFluid() == Fluids.WATER);
        if (isWallHanging()) {
            Direction direction = context.getFace();
            if (direction != Direction.DOWN && direction != Direction.UP) {
                state = state.with(WALL, true);
                state = state.with(HORIZONTAL_FACING, direction);
            }
        }
        return state;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> blockBlockStateBuilder) {
        blockBlockStateBuilder.add(HORIZONTAL_FACING, WATERLOGGED);
        if (isWallHanging())
            blockBlockStateBuilder.add(WALL);
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

        if (isWallHanging() && stateIn.get(WALL)) {
            Direction direction = stateIn.get(HORIZONTAL_FACING);
            return facing == direction.rotateY().rotateY() && !this.isValidPosition(stateIn, worldIn, currentPos) ? Blocks.AIR.getDefaultState() : stateIn;
        }

        return facing == Direction.DOWN && !this.isValidPosition(stateIn, worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        if (!isStickingBlock())
            return super.isValidPosition(state, worldIn, pos);

        if (isWallHanging() && state.get(WALL)) {
            Direction direction = state.get(HORIZONTAL_FACING);
            return hasEnoughSolidSide(worldIn, pos.offset(direction.rotateY().rotateY()), Direction.UP);
        }
        return hasEnoughSolidSide(worldIn, pos.down(), Direction.UP);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public void onReplaced(BlockState before, World worldIn, BlockPos pos, BlockState after, boolean isMoving) {
        if (before.getBlock() != after.getBlock() && this.hasTileEntity(before) && worldIn.getTileEntity(pos) instanceof IMPAbstractTileEntity) {
            IMPAbstractTileEntity tileentity = (IMPAbstractTileEntity) worldIn.getTileEntity(pos);
            tileentity.getItems().forEach(n -> {
                ItemEntity dropItem = IKSGEntityUtil.createItemEntity(n, worldIn, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
                worldIn.addEntity(dropItem);
            });
            tileentity.clear();
        }
        super.onReplaced(before, worldIn, pos, after, isMoving);
    }

    public boolean isWallHanging() {
        return false;
    }

    public boolean isStickingBlock() {
        return false;
    }
}
