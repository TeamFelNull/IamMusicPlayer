package red.felnull.imp.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import red.felnull.imp.block.propertie.IMPBlockStateProperties;
import red.felnull.imp.block.voxelshape.BoomboxVoxelShape;
import red.felnull.imp.tileentity.BoomboxTileEntity;

public class BoomboxBlock extends IMPAbstractEquipmentBlock {
    public static final BooleanProperty WALL = IMPBlockStateProperties.WALL;

    public BoomboxBlock(Properties properties) {
        super(properties);
        this.setDefaultState(getDefaultState().with(WALL, Boolean.valueOf(false)));
    }

    @Override
    public VoxelShape getFacingShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context, Direction direction) {
        if (!state.get(WALL)) {
            switch (direction) {
                case NORTH:
                    return BoomboxVoxelShape.NORTH_AXIS_AABB;
                case SOUTH:
                    return BoomboxVoxelShape.SOUTH_AXIS_AABB;
                case EAST:
                    return BoomboxVoxelShape.EAST_AXIS_AABB;
                case WEST:
                    return BoomboxVoxelShape.WEST_AXIS_AABB;
                default:
                    return BoomboxVoxelShape.NORTH_AXIS_AABB;
            }
        } else {
            switch (direction) {
                case NORTH:
                    return BoomboxVoxelShape.NORTH_WALL_AXIS_AABB;
                case SOUTH:
                    return BoomboxVoxelShape.SOUTH_WALL_AXIS_AABB;
                case EAST:
                    return BoomboxVoxelShape.EAST_WALL_AXIS_AABB;
                case WEST:
                    return BoomboxVoxelShape.WEST_WALL_AXIS_AABB;
                default:
                    return BoomboxVoxelShape.NORTH_WALL_AXIS_AABB;
            }
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> blockBlockStateBuilder) {
        super.fillStateContainer(blockBlockStateBuilder);
        blockBlockStateBuilder.add(WALL);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        Direction direction = context.getFace();
        if (direction != Direction.DOWN && direction != Direction.UP) {
            state = state.with(WALL, true);
            state = state.with(HORIZONTAL_FACING, direction);
        }
        return state;
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WALL)) {
            if (stateIn.get(WATERLOGGED)) {
                worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
            }
            Direction direction = stateIn.get(HORIZONTAL_FACING);
            return facing == direction.rotateY().rotateY() && !this.isValidPosition(stateIn, worldIn, currentPos) ? Blocks.AIR.getDefaultState() : stateIn;
        }
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        if (state.get(WALL)) {
            Direction direction = state.get(HORIZONTAL_FACING);
            return hasEnoughSolidSide(worldIn, pos.offset(direction.rotateY().rotateY()), Direction.UP);
        }
        return super.isValidPosition(state, worldIn, pos);
    }


    @Override
    protected void interactWith(World worldIn, BlockPos pos, PlayerEntity playerIn) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof BoomboxTileEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, (INamedContainerProvider) tileentity, pos);
        }
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BoomboxTileEntity();
    }
}
