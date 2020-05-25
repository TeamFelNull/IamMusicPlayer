package net.morimori.imp.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.morimori.imp.IkisugiMusicPlayer;
import net.morimori.imp.tileentity.CassetteDeckTileEntity;
import net.morimori.imp.util.PlayerHelper;

public class CassetteDeckBlock extends Block implements IWaterLoggable {
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final BooleanProperty ON = IMPBooleanPropertys.ON;
	public static final EnumProperty<CassetteDeckStates> CASSETTE_DECK_STATES = IMPBooleanPropertys.CASSETTE_DECK_STATES;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public CassetteDeckBlock(Properties properties) {
		super(properties);
		this.setDefaultState(
				this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(ON, Boolean.valueOf(false))
						.with(CASSETTE_DECK_STATES, CassetteDeckStates.NONE).with(WATERLOGGED, Boolean.valueOf(false)));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return !state.get(WATERLOGGED);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().rotateY()).with(WATERLOGGED,
				Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
	}

	@SuppressWarnings("deprecation")
	public IFluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));

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

		if (!(tileentity instanceof CassetteDeckTileEntity))
			return;
		int cont = 0;
		for (ItemStack its : ((CassetteDeckTileEntity) tileentity).getItems()) {
			if (!its.isEmpty()) {
				worldIn.playEvent(1010, pos, 0);
				ItemStack dropCassette = its.copy();

				ItemEntity dropItem = new ItemEntity(worldIn, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d,
						dropCassette);

				dropItem.setDefaultPickupDelay();
				worldIn.addEntity(dropItem);
				((CassetteDeckTileEntity) tileentity).setItem(cont, its);
			}
			cont++;
		}
	}

	public ActionResultType func_225533_a_(BlockState p_225533_1_, World worldIn, BlockPos pos,
			PlayerEntity playerIn, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
		if (!worldIn.isRemote) {

			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (!(tileentity instanceof CassetteDeckTileEntity))
				return ActionResultType.SUCCESS;

			NetworkHooks.openGui((ServerPlayerEntity) playerIn, (INamedContainerProvider) tileentity, pos);
		}
		return ActionResultType.SUCCESS;

	}

	@Override
	public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, ON, CASSETTE_DECK_STATES, WATERLOGGED);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new CassetteDeckTileEntity();
	}

	@Override
	public final boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		Direction direction = state.get(FACING);

		switch (direction) {
		case NORTH:
			return CassetteDeckVoxelShape.NORTH_AXIS_AABB;
		case SOUTH:
			return CassetteDeckVoxelShape.SOUTH_AXIS_AABB;
		case EAST:
			return CassetteDeckVoxelShape.EAST_AXIS_AABB;
		case WEST:
			return CassetteDeckVoxelShape.WEST_AXIS_AABB;
		default:
			return CassetteDeckVoxelShape.NORTH_AXIS_AABB;
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
		return facing == Direction.DOWN && !this.isValidPosition(stateIn, worldIn, currentPos)
				? Blocks.AIR.getDefaultState()
				: super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return func_220055_a(worldIn, pos.down(), Direction.UP);

	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {

		if (!(worldIn.getTileEntity(pos) instanceof CassetteDeckTileEntity))
			return;

		CassetteDeckTileEntity tileentity = (CassetteDeckTileEntity) worldIn.getTileEntity(pos);
		Minecraft mc = IkisugiMusicPlayer.proxy.getMinecraft();

		if (stateIn.get(CASSETTE_DECK_STATES) == CassetteDeckStates.PLAY
				&& !tileentity.lisnFinishedPlayers.contains(PlayerHelper.getUUID(mc.player))) {
			worldIn.addParticle(ParticleTypes.NOTE, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.7D,
					(double) pos.getZ() + 0.5D, (double) this.RANDOM.nextInt(25) / 24.0D, 0.0D, 0.0D);
		}
	}
}
