package dev.felnull.imp.block;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.otyacraftengine.util.OEVoxelShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BoomboxBlock extends IMPBaseEntityBlock {
    private static final OEVoxelShapeUtil.DirectionVoxelShapes SHAPE = OEVoxelShapeUtil.makeAllDirection(OEVoxelShapeUtil.getShapeFromResource(new ResourceLocation(IamMusicPlayer.MODID, "boombox"), MusicManagerBlock.class));
    private static final OEVoxelShapeUtil.DirectionVoxelShapes SHAPE_CLOSE = OEVoxelShapeUtil.makeAllDirection(OEVoxelShapeUtil.getShapeFromResource(new ResourceLocation(IamMusicPlayer.MODID, "boombox_close_handle"), MusicManagerBlock.class));


    protected BoomboxBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BoomboxBlockEntity(blockPos, blockState);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE.getShape(blockState.getValue(FACING));
    }
}
