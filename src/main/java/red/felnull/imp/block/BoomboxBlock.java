package red.felnull.imp.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import red.felnull.imp.block.propertie.BoomboxMode;
import red.felnull.imp.block.propertie.IMPBlockStateProperties;
import red.felnull.imp.block.voxelshape.BoomboxVoxelShape;
import red.felnull.imp.tileentity.BoomboxTileEntity;

public class BoomboxBlock extends IMPAbstractEquipmentBlock {
    public static final EnumProperty<BoomboxMode> BOOMBOX_MODE = IMPBlockStateProperties.BOOMBOX_MODE;

    public BoomboxBlock(Properties properties) {
        super(properties);
        this.setDefaultState(getDefaultState().with(BOOMBOX_MODE, BoomboxMode.NONE));
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
        blockBlockStateBuilder.add(BOOMBOX_MODE);
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

    @Override
    public boolean isWallHanging() {
        return true;
    }
}
