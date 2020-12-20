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
import red.felnull.imp.block.voxelshape.MusicSharingDeviceVoxelShape;
import red.felnull.imp.tileentity.MusicSharingDeviceTileEntity;

public class MusicSharingDeviceBlock extends IMPAbstractEquipmentBlock {
    protected MusicSharingDeviceBlock(Properties builder) {
        super(builder);
    }


    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MusicSharingDeviceTileEntity();
    }

    @Override
    public VoxelShape getFacingShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context, Direction direction) {
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
    protected void interactWith(World worldIn, BlockPos pos, PlayerEntity playerIn) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof MusicSharingDeviceTileEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, (INamedContainerProvider) tileentity, pos);
        }
    }

}
