package red.felnull.imp.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import red.felnull.imp.tileentity.CassetteStoringTileEntity;

public class CassetteStoringBlock extends Block implements IWaterLoggable {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty WALL = IMPBooleanProperties.WALL;

    public CassetteStoringBlock(Properties properties) {
        super(properties);
        this.setDefaultState(
                this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED,
                        Boolean.valueOf(false)).with(WALL, Boolean.valueOf(false)));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CassetteStoringTileEntity();
    }

    public void dropItem(World worldIn, BlockPos pos) {
        if (worldIn.isRemote)
            return;
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (!(tileentity instanceof CassetteStoringTileEntity))
            return;
        int cont = 0;
        for (ItemStack its : ((CassetteStoringTileEntity) tileentity).getItems()) {
            if (!its.isEmpty()) {
                worldIn.playEvent(1010, pos, 0);
                ItemStack dropCassette = its.copy();

                ItemEntity dropItem = new ItemEntity(worldIn, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d,
                        dropCassette);

                dropItem.setDefaultPickupDelay();
                worldIn.addEntity(dropItem);
                ((CassetteStoringTileEntity) tileentity).setItem(cont, its);
            }
            cont++;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState before, World worldIn, BlockPos pos, BlockState after,
                           boolean isMoving) {

        if (before.getBlock() != after.getBlock()) {
            dropItem(worldIn, pos);
        }
        super.onReplaced(before, worldIn, pos, after, isMoving);
    }

    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().rotateY()).with(WATERLOGGED,
                Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));

    }

    @Override
    public final boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState p_225533_1_, World worldIn, BlockPos pos,
                                             PlayerEntity playerIn, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
        if (!worldIn.isRemote) {

            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (!(tileentity instanceof CassetteStoringTileEntity))
                return ActionResultType.SUCCESS;

            NetworkHooks.openGui((ServerPlayerEntity) playerIn, (INamedContainerProvider) tileentity, pos);
        }
        return ActionResultType.SUCCESS;

    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return !state.get(WATERLOGGED);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
                                          BlockPos currentPos, BlockPos facingPos) {

        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = state.get(FACING);

        if (state.get(WALL)) {
            switch (direction) {
                case NORTH:
                    return CassetteStoringVoxelShape.NORTH_WALL_AXIS_AABB;
                case SOUTH:
                    return CassetteStoringVoxelShape.SOUTH_WALL_AXIS_AABB;
                case EAST:
                    return CassetteStoringVoxelShape.EAST_WALL_AXIS_AABB;
                case WEST:
                    return CassetteStoringVoxelShape.WEST_WALL_AXIS_AABB;
                default:
                    return CassetteStoringVoxelShape.NORTH_WALL_AXIS_AABB;
            }
        } else {
            switch (direction) {
                case NORTH:
                    return CassetteStoringVoxelShape.NORTH_AXIS_AABB;
                case SOUTH:
                    return CassetteStoringVoxelShape.SOUTH_AXIS_AABB;
                case EAST:
                    return CassetteStoringVoxelShape.EAST_AXIS_AABB;
                case WEST:
                    return CassetteStoringVoxelShape.WEST_AXIS_AABB;
                default:
                    return CassetteStoringVoxelShape.EAST_AXIS_AABB;
            }
        }

    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));

    }

    @Override
    public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, WALL);
    }
}
