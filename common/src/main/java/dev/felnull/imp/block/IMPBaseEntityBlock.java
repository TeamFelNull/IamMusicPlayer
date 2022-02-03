package dev.felnull.imp.block;

import dev.felnull.otyacraftengine.block.HorizontalDirectionalEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class IMPBaseEntityBlock extends HorizontalDirectionalEntityBlock {
    public static final BooleanProperty POWERED = IMPBlockStateProperties.POWER;

    protected IMPBaseEntityBlock(Properties properties) {
        super(properties.lightLevel((state) -> state.getValue(POWERED) ? 13 : 0));
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false));
        setAnalogOutput(true);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }
}
