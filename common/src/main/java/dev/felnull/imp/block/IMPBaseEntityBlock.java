package dev.felnull.imp.block;

import dev.felnull.otyacraftengine.block.HorizontalDirectionalEntityBlock;
import dev.felnull.otyacraftengine.blockentity.OEBaseContainerBlockEntity;
import dev.felnull.otyacraftengine.util.OEMenuUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public abstract class IMPBaseEntityBlock extends HorizontalDirectionalEntityBlock {
    public static final BooleanProperty POWERED = IMPBlockStateProperties.POWER;

    protected IMPBaseEntityBlock(Properties properties) {
        super(properties.lightLevel((state) -> state.getValue(POWERED) ? 13 : 0));
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            this.openContainer(level, blockPos, (ServerPlayer) player, blockState, blockHitResult.getDirection());
            return InteractionResult.CONSUME;
        }
    }

    protected void openContainer(Level level, BlockPos blockPos, ServerPlayer player, BlockState blockState, Direction direction) {
        if (level.getBlockEntity(blockPos) instanceof OEBaseContainerBlockEntity oeBaseContainerBlock)
            OEMenuUtil.openBlockMenu(player, oeBaseContainerBlock, blockPos, oeBaseContainerBlock.getContainerSize());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }
}
