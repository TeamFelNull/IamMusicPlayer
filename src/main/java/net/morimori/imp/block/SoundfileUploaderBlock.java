package net.morimori.imp.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.morimori.imp.client.screen.SoundFileUploaderMonitorTextures;
import net.morimori.imp.client.screen.SoundFileUploaderWindwos;
import net.morimori.imp.tileentity.SoundFileUploaderTileEntity;

public class SoundfileUploaderBlock extends Block {
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final BooleanProperty ON = IMPBooleanPropertys.ON;
	public static final EnumProperty<SoundFileUploaderMonitorTextures> SOUNDFILE_UPLOADER_MONITOR = IMPBooleanPropertys.SOUNDFILE_UPLOADER_MONITOR;
	public static final EnumProperty<SoundFileUploaderWindwos> SOUNDFILE_UPLOADER_WINDWOS = IMPBooleanPropertys.SOUNDFILE_UPLOADER_WINDWOS;

	public SoundfileUploaderBlock(Properties properties) {
		super(properties);
		this.setDefaultState(
				this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(ON, Boolean.valueOf(false)));

	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().rotateY()).with(ON, false)

				.with(SOUNDFILE_UPLOADER_MONITOR, SoundFileUploaderMonitorTextures.OFF)
				.with(SOUNDFILE_UPLOADER_WINDWOS, SoundFileUploaderWindwos.NONE);
	}

	@Override
	@Deprecated
	public int getLightValue(BlockState state) {
		return state.get(ON) ? 5 : 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState before, World worldIn, BlockPos pos, BlockState after,
			boolean isMoving) {

		if (before.getBlock() != after.getBlock()) {
			dropItem(worldIn, pos);
		}
		super.onReplaced(before, worldIn, pos, after, isMoving);
	}

	public void dropItem(World worldIn, BlockPos pos) {
		if (worldIn.isRemote)
			return;
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (!(tileentity instanceof SoundFileUploaderTileEntity))
			return;

		ItemStack antenna = ((SoundFileUploaderTileEntity) tileentity).getAntenna();

		if (!antenna.isEmpty()) {
			worldIn.playEvent(1010, pos, 0);
			ItemStack dropCassette = antenna.copy();

			ItemEntity dropItem = new ItemEntity(worldIn, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d,
					dropCassette);

			dropItem.setDefaultPickupDelay();
			worldIn.addEntity(dropItem);
			((SoundFileUploaderTileEntity) tileentity).setAntenna(ItemStack.EMPTY);
		}

	}

	public ActionResultType func_225533_a_(BlockState stateIn, World worldIn, BlockPos pos,
			PlayerEntity playerIn, Hand hand, BlockRayTraceResult brtr) {
		if (!worldIn.isRemote) {
			if (brtr.getFace() == stateIn.get(FACING).rotateY()) {
				TileEntity tileentity = worldIn.getTileEntity(pos);

				if (!(tileentity instanceof SoundFileUploaderTileEntity))
					return ActionResultType.SUCCESS;

				NetworkHooks.openGui((ServerPlayerEntity) playerIn, (INamedContainerProvider) tileentity, pos);
			}
		}
		return ActionResultType.PASS;

	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new SoundFileUploaderTileEntity();
	}

	@Override
	public final boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@Override
	public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, ON, SOUNDFILE_UPLOADER_MONITOR, SOUNDFILE_UPLOADER_WINDWOS);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		Direction direction = state.get(FACING);

		switch (direction) {
		case NORTH:
			return SoundfileUploaderVoxelShape.NORTH_AXIS_AABB;
		case SOUTH:
			return SoundfileUploaderVoxelShape.SOUTH_AXIS_AABB;
		case EAST:
			return SoundfileUploaderVoxelShape.EAST_AXIS_AABB;
		case WEST:
			return SoundfileUploaderVoxelShape.WEST_AXIS_AABB;
		default:
			return SoundfileUploaderVoxelShape.NORTH_AXIS_AABB;
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && !this.isValidPosition(stateIn, worldIn, currentPos)
				? Blocks.AIR.getDefaultState()
				: super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return func_220055_a(worldIn, pos.down(), Direction.UP);

	}
}
