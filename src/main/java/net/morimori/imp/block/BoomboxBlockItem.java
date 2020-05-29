package net.morimori.imp.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.IWorldReader;

public class BoomboxBlockItem extends WallOrFloorItem {

	public BoomboxBlockItem(Block floorBlock, Properties propertiesIn) {
		super(floorBlock, floorBlock, propertiesIn);
	}

	   @Nullable
	   @Override
	   protected BlockState getStateForPlacement(BlockItemUseContext context) {
	      BlockState blockstate = this.getBlock().getStateForPlacement(context).with(BoomboxBlock.WALL, Boolean.valueOf(true));
	      BlockState blockstate1 = null;
	      IWorldReader iworldreader = context.getWorld();
	      BlockPos blockpos = context.getPos();

	      for(Direction direction : context.getNearestLookingDirections()) {
	         if (direction != Direction.UP) {
	            BlockState blockstate2 = direction == Direction.DOWN ? this.getBlock().getStateForPlacement(context) : blockstate;
	            if (blockstate2 != null && blockstate2.isValidPosition(iworldreader, blockpos)) {
	               blockstate1 = blockstate2;
	               break;
	            }
	         }
	      }

	      return blockstate1 != null && iworldreader.func_226663_a_(blockstate1, blockpos, ISelectionContext.dummy()) ? blockstate1 : null;
	   }

}
