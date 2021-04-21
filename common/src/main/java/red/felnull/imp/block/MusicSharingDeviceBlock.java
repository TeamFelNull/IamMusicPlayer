package red.felnull.imp.block;

import me.shedaniel.architectury.registry.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import red.felnull.imp.block.shape.MusicSharingDeviceShape;
import red.felnull.imp.blockentity.IMPBlockEntitys;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;

public class MusicSharingDeviceBlock extends IMPEquipmentBaseBlock {
    public MusicSharingDeviceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMPBlockEntitys.MUSIC_SHARING_DEVICE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MusicSharingDeviceBlockEntity(blockPos, blockState);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        Direction direction = blockState.getValue(IMPEquipmentBaseBlock.FACING);
        switch (direction) {
            case NORTH:
                return MusicSharingDeviceShape.NORTH_AXIS_AABB;
            case SOUTH:
                return MusicSharingDeviceShape.SOUTH_AXIS_AABB;
            case EAST:
                return MusicSharingDeviceShape.EAST_AXIS_AABB;
            case WEST:
                return MusicSharingDeviceShape.WEST_AXIS_AABB;
            default:
                return MusicSharingDeviceShape.NORTH_AXIS_AABB;
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(blockPos);
            if (be instanceof MusicSharingDeviceBlockEntity) {
                MenuRegistry.openExtendedMenu((ServerPlayer) player, (MenuProvider) be, n -> {
                    n.writeBlockPos(blockPos);
                    n.writeInt(((MusicSharingDeviceBlockEntity) be).getContainerSize());
                });
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
