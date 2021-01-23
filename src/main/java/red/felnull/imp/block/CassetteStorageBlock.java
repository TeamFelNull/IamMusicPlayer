package red.felnull.imp.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import red.felnull.imp.block.voxelshape.CassetteStorageVoxelShape;
import red.felnull.imp.tileentity.CassetteStorageTileEntity;

import javax.annotation.Nullable;

public class CassetteStorageBlock extends IMPAbstractBlock {

    protected CassetteStorageBlock(Properties builder) {
        super(builder);
    }

    @Override
    public VoxelShape getFacingShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context, Direction direction) {
        if (!state.get(WALL)) {
            switch (direction) {
                case NORTH:
                    return CassetteStorageVoxelShape.SOUTH_AXIS_AABB;
                case SOUTH:
                    return CassetteStorageVoxelShape.NORTH_AXIS_AABB;
                case EAST:
                    return CassetteStorageVoxelShape.WEST_AXIS_AABB;
                case WEST:
                    return CassetteStorageVoxelShape.EAST_AXIS_AABB;
                default:
                    return CassetteStorageVoxelShape.NORTH_AXIS_AABB;
            }
        } else {
            switch (direction) {
                case NORTH:
                    return CassetteStorageVoxelShape.SOUTH_WALL_AXIS_AABB;
                case SOUTH:
                    return CassetteStorageVoxelShape.NORTH_WALL_AXIS_AABB;
                case EAST:
                    return CassetteStorageVoxelShape.WEST_WALL_AXIS_AABB;
                case WEST:
                    return CassetteStorageVoxelShape.EAST_WALL_AXIS_AABB;
                default:
                    return CassetteStorageVoxelShape.NORTH_WALL_AXIS_AABB;
            }
        }
    }

    @Override
    public boolean isWallHanging() {
        return true;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CassetteStorageTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof CassetteStorageTileEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) playerIn, (INamedContainerProvider) tileentity, pos);
            }
        }
        return ActionResultType.func_233537_a_(worldIn.isRemote);
    }

}
