package net.morimori.imp.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.morimori.imp.util.VoxelShapeHelper;

public class CassetteStoringBlock extends Block {
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

	public CassetteStoringBlock(Properties properties) {
		super(properties);
		this.setDefaultState(
				this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().rotateY());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		Direction direction = state.get(FACING);

		switch (direction) {
		case NORTH:
			return VoxelShapeHelper.addCuboidShaoe90(0.45, 0, 0.85, 15.2, 16, 6.2);
		case SOUTH:
			return VoxelShapeHelper.addCuboidShaoe270(0.45, 0, 0.85, 15.2, 16, 6.2);
		case EAST:
			return VoxelShapeHelper.addCuboidShaoe0(0.45, 0, 0.85, 15.2, 16, 6.2);
		case WEST:
			return VoxelShapeHelper.addCuboidShaoe180(0.45, 0, 0.85, 15.2, 16, 6.2);
		default:
			return VoxelShapeHelper.addCuboidShaoe0(0.45, 0, 0.85, 15.2, 16, 6.2);
		}

	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));

	}

	@Override
	public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
