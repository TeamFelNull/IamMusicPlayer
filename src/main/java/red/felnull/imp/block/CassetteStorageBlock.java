package red.felnull.imp.block;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
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
}
