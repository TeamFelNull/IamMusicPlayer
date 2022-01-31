package dev.felnull.imp.block;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.blockentity.IMPBlockEntitys;
import dev.felnull.otyacraftengine.util.OEVoxelShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BoomboxBlock extends IMPBaseEntityBlock {
    private static final OEVoxelShapeUtil.DirectionVoxelShapes SHAPE = OEVoxelShapeUtil.makeAllDirection(OEVoxelShapeUtil.getShapeFromResource(new ResourceLocation(IamMusicPlayer.MODID, "boombox"), MusicManagerBlock.class));
    private static final OEVoxelShapeUtil.DirectionVoxelShapes SHAPE_NO_RAISED = OEVoxelShapeUtil.makeAllDirection(OEVoxelShapeUtil.getShapeFromResource(new ResourceLocation(IamMusicPlayer.MODID, "boombox_no_raised"), MusicManagerBlock.class));
    public static final BooleanProperty RAISED = IMPBlockStateProperties.RAISE;

    protected BoomboxBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(RAISED, true));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.isCrouching()) {
            var be = level.getBlockEntity(blockPos);
            if (be instanceof BoomboxBlockEntity boombox) {
                if (blockHitResult.getDirection() == Direction.UP) {
                    if (boombox.cycleRaisedHandle()) {
                        level.playSound(null, blockPos, boombox.isHandleRaising() ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                        return InteractionResult.sidedSuccess(level.isClientSide());
                    }
                } else if (blockHitResult.getDirection() == blockState.getValue(FACING)) {
                    if (boombox.cycleLidOpen()) {
                        level.playSound(null, blockPos, boombox.isHandleRaising() ? SoundEvents.WOODEN_DOOR_OPEN : SoundEvents.WOODEN_DOOR_CLOSE, SoundSource.BLOCKS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                        return InteractionResult.sidedSuccess(level.isClientSide());
                    }
                }
            }
        } else {
            return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BoomboxBlockEntity(blockPos, blockState);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        var shp = blockState.getValue(RAISED) ? SHAPE : SHAPE_NO_RAISED;
        return shp.getShape(blockState.getValue(FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(RAISED);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, IMPBlockEntitys.BOOMBOX, BoomboxBlockEntity::tick);
    }
}
