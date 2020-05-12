package net.morimori.imp.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.morimori.imp.IkisugiMusicPlayer;
import net.morimori.imp.tileentity.BoomboxTileEntity;
import net.morimori.imp.util.ItemHelper;
import net.morimori.imp.util.PlayerHelper;

public class BoomboxBlock extends Block {
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public static final BooleanProperty ON = IMPBooleanPropertys.ON;
	public static final IntegerProperty VOLUME = IMPBooleanPropertys.VOLUME_0_8;

	public BoomboxBlock(Properties properties) {
		super(properties);
		this.setDefaultState(
				this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(OPEN, Boolean.valueOf(false))
						.with(ON, Boolean.valueOf(false)).with(VOLUME, 15));

	}

	@Override
	public ActionResultType func_225533_a_(BlockState stateIn, World worldIn, BlockPos pos,
			PlayerEntity player, Hand hand, BlockRayTraceResult brtr) {

		if (!(worldIn.getTileEntity(pos) instanceof BoomboxTileEntity))
			return ActionResultType.PASS;

		BoomboxTileEntity tileentity = (BoomboxTileEntity) worldIn.getTileEntity(pos);

		ItemStack helditem = player.getHeldItem(hand);

		if (brtr.getFace() == stateIn.get(FACING).rotateY()) {

			if (player.isCrouching()) {
				stateIn = stateIn.cycle(OPEN);
				stateIn = stateIn.with(ON, false);
				worldIn.setBlockState(pos, stateIn, 2);
				worldIn.playEvent(player, 1037, pos, 0);
				return ActionResultType.SUCCESS;
			}

			if (stateIn.get(OPEN)) {
				ItemStack insertstack = helditem.copy();
				insertstack.setCount(1);
				if (ItemHelper.isCassette(
						helditem) && tileentity.getPlayCassette().isEmpty()
						&& insertCassette(worldIn, pos, insertstack)) {
					if (!player.isCreative()) {
						helditem.shrink(1);
					}
					return ActionResultType.SUCCESS;
				} else if (!tileentity.getPlayCassette().isEmpty()) {
					this.dropCassette(worldIn, pos, true);
					return ActionResultType.SUCCESS;
				}
			}

			if (!player.isCrouching() && !stateIn.get(OPEN) && ItemHelper.canPlay(tileentity.getPlayCassette())
					&& tileentity.openProgress == 0 && !worldIn.isBlockPowered(pos)) {
				stateIn = stateIn.cycle(ON);
				worldIn.setBlockState(pos, stateIn, 2);
				return ActionResultType.SUCCESS;
			}

		}

		if (brtr.getFace() == Direction.UP) {
			int vol = stateIn.get(VOLUME);

			if (player.isCrouching()) {
				if (vol > 0) {
					worldIn.setBlockState(pos, stateIn.with(VOLUME, vol - 1));
					player.sendStatusMessage(new TranslationTextComponent("message.volume.set", vol - 1), true);
					return ActionResultType.SUCCESS;
				}
			} else {
				if (vol < 32) {
					worldIn.setBlockState(pos, stateIn.with(VOLUME, vol + 1));
					player.sendStatusMessage(new TranslationTextComponent("message.volume.set", vol + 1), true);
					return ActionResultType.SUCCESS;
				}
			}

		}

		return ActionResultType.PASS;
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
			this.dropCassette(worldIn, pos, false);
		}

		super.onReplaced(before, worldIn, pos, after, isMoving);

	}

	public boolean insertCassette(World worldIn, BlockPos pos, ItemStack stackIn) {

		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (!(tileentity instanceof BoomboxTileEntity))
			return false;

		ItemStack cassette = ((BoomboxTileEntity) tileentity).getPlayCassette();

		if (cassette.isEmpty()) {
			((BoomboxTileEntity) tileentity).setCassette(stackIn.copy());
			return true;
		}
		return false;
	}

	public void dropCassette(World worldIn, BlockPos pos, boolean tossed) {

		if (worldIn.isRemote)
			return;

		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (!(tileentity instanceof BoomboxTileEntity))
			return;

		ItemStack cassette = ((BoomboxTileEntity) tileentity).getPlayCassette();

		if (!cassette.isEmpty()) {
			worldIn.playEvent(1010, pos, 0);
			ItemStack dropCassette = cassette.copy();

			ItemEntity dropItem = new ItemEntity(worldIn, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d,
					dropCassette);
			if (tossed) {
				Direction direction = worldIn.getBlockState(pos).get(FACING);
				if (direction == Direction.NORTH) {
					dropItem.setPosition(pos.getX() + 1.2d, pos.getY() + 0.2d, pos.getZ() + 0.5d);
					dropItem.setMotion(new Vec3d(0.3f, 0, 0));
				} else if (direction == Direction.SOUTH) {
					dropItem.setPosition(pos.getX() - 0.2d, pos.getY() + 0.2d, pos.getZ() + 0.5d);
					dropItem.setMotion(new Vec3d(-0.3f, 0, 0));
				} else if (direction == Direction.EAST) {
					dropItem.setPosition(pos.getX() + 0.5d, pos.getY() + 0.2d, pos.getZ() + 1.2d);
					dropItem.setMotion(new Vec3d(0, 0, 0.3f));
				} else if (direction == Direction.WEST) {
					dropItem.setPosition(pos.getX() + 0.5d, pos.getY() + 0.2d, pos.getZ() - 0.2d);
					dropItem.setMotion(new Vec3d(0, 0, -0.3f));
				}

			}
			dropItem.setDefaultPickupDelay();
			worldIn.addEntity(dropItem);
			((BoomboxTileEntity) tileentity).setCassette(ItemStack.EMPTY);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().rotateY());
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new BoomboxTileEntity();
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
		builder.add(FACING, OPEN, ON, VOLUME);
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

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		Direction direction = state.get(FACING);

		switch (direction) {
		case NORTH:
			return BoomboxVoxelShape.NORTH_AXIS_AABB;
		case SOUTH:
			return BoomboxVoxelShape.SOUTH_AXIS_AABB;
		case EAST:
			return BoomboxVoxelShape.EAST_AXIS_AABB;
		case WEST:
			return BoomboxVoxelShape.WEST_AXIS_AABB;
		default:
			return BoomboxVoxelShape.NORTH_AXIS_AABB;
		}

	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {

		if (!(worldIn.getTileEntity(pos) instanceof BoomboxTileEntity))
			return;

		BoomboxTileEntity tileentity = (BoomboxTileEntity) worldIn.getTileEntity(pos);
		Minecraft mc = IkisugiMusicPlayer.proxy.getMinecraft();

		if (stateIn.get(ON) && !tileentity.lisnFinishedPlayers.contains(PlayerHelper.getUUID(mc.player))) {
			worldIn.addParticle(ParticleTypes.NOTE, (double) pos.getX() + 0.5D, (double) pos.getY() + 1.2D,
					(double) pos.getZ() + 0.5D, (double) this.RANDOM.nextInt(25) / 24.0D, 0.0D, 0.0D);
		}
	}
}
