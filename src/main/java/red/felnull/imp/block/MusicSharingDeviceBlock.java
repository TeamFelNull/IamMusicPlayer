package red.felnull.imp.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
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
import net.minecraftforge.fml.network.NetworkHooks;
import red.felnull.imp.block.propertie.IMPBlockStateProperties;
import red.felnull.imp.block.voxelshape.MusicSharingDeviceVoxelShape;
import red.felnull.imp.ffmpeg.FFmpegManeger;
import red.felnull.imp.tileentity.MusicSharingDeviceTileEntity;

public class MusicSharingDeviceBlock extends HorizontalBlock {
    protected MusicSharingDeviceBlock(Properties builder) {
        super(builder);
    }

    public static final BooleanProperty ON = IMPBlockStateProperties.ON;

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return this.getDefaultState().with(HORIZONTAL_FACING, p_196258_1_.getPlacementHorizontalFacing().getOpposite()).with(ON, false);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> blockBlockStateBuilder) {
        blockBlockStateBuilder.add(HORIZONTAL_FACING, ON);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = state.get(HorizontalBlock.HORIZONTAL_FACING);
        switch (direction) {
            case NORTH:
                return MusicSharingDeviceVoxelShape.NORTH_AXIS_AABB;
            case SOUTH:
                return MusicSharingDeviceVoxelShape.SOUTH_AXIS_AABB;
            case EAST:
                return MusicSharingDeviceVoxelShape.EAST_AXIS_AABB;
            case WEST:
                return MusicSharingDeviceVoxelShape.WEST_AXIS_AABB;
            default:
                return MusicSharingDeviceVoxelShape.NORTH_AXIS_AABB;
        }
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !this.isValidPosition(stateIn, worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return hasEnoughSolidSide(worldIn, pos.down(), Direction.UP);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
        FFmpegManeger maneger = FFmpegManeger.instance();
        if (!worldIn.isRemote) {
            if (maneger.canUseFFmpeg()) {
                TileEntity tileentity = worldIn.getTileEntity(pos);
                if (!(tileentity instanceof MusicSharingDeviceTileEntity))
                    return ActionResultType.PASS;
                NetworkHooks.openGui((ServerPlayerEntity) playerIn, (INamedContainerProvider) tileentity, pos);
            } else {
                maneger.cantFFmpegCaution(playerIn);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MusicSharingDeviceTileEntity();
    }

    @Override
    public final boolean hasTileEntity(BlockState state) {
        return true;
    }
}
