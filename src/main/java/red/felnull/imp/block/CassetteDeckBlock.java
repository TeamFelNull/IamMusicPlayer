package red.felnull.imp.block;


import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import red.felnull.imp.block.voxelshape.CassetteDeckVoxelShape;
import red.felnull.imp.tileentity.CassetteDeckTileEntity;

public class CassetteDeckBlock extends IMPAbstractEquipmentBlock {
    protected CassetteDeckBlock(Properties builder) {
        super(builder);
    }

    @Override
    public VoxelShape getFacingShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context, Direction direction) {
        switch (direction) {
            case NORTH:
                return CassetteDeckVoxelShape.NORTH_AXIS_AABB;
            case SOUTH:
                return CassetteDeckVoxelShape.SOUTH_AXIS_AABB;
            case EAST:
                return CassetteDeckVoxelShape.EAST_AXIS_AABB;
            case WEST:
                return CassetteDeckVoxelShape.WEST_AXIS_AABB;
            default:
                return CassetteDeckVoxelShape.NORTH_AXIS_AABB;
        }
    }

    @Override
    protected void interactWith(World worldIn, BlockPos pos, PlayerEntity playerIn) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof CassetteDeckTileEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, (INamedContainerProvider) tileentity, pos);
        }
    }


    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CassetteDeckTileEntity();
    }
}
