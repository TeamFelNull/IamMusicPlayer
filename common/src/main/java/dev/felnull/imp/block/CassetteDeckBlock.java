package dev.felnull.imp.block;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.CassetteDeckBlockEntity;
import dev.felnull.otyacraftengine.util.OEVoxelShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CassetteDeckBlock extends IMPBaseEntityBlock {
    private static final OEVoxelShapeUtil.DirectionVoxelShapes SHAPE = OEVoxelShapeUtil.makeAllDirection(OEVoxelShapeUtil.getShapeFromResource(new ResourceLocation(IamMusicPlayer.MODID, "cassette_deck"), BoomboxBlock.class));

    protected CassetteDeckBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CassetteDeckBlockEntity(blockPos, blockState);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE.getShape(blockState.getValue(FACING));
    }
}
