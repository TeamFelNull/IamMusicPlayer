package net.morimori.imp.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.IWorldReader;
import net.morimori.imp.block.BoomboxBlock;
import net.morimori.imp.block.IMPBooleanProperties;

public class BoomBoxItem extends BlockItem {

    public BoomBoxItem(Block floorBlock, Properties propertiesIn) {
        super(floorBlock, propertiesIn);
        this.addPropertyOverride(new ResourceLocation("on"), (stack, world, entity) -> {

            if (stack.getItem() == this) {
                BoomBoxTileEntityStack BBTES = new BoomBoxTileEntityStack(stack);

                if (BBTES.getBlockstate() == null) {
                    return 0.0F;
                }

                return BBTES.getBlockstate().get(BoomboxBlock.ON) ? 1.0F : 0.0F;
            } else {
                return 0.0F;
            }

        });
    }

    @Override
    protected BlockState getStateForPlacement(BlockItemUseContext context) {

        BoomBoxTileEntityStack bbtes = new BoomBoxTileEntityStack(context.getItem());
        BlockState defstate = bbtes.getBlockstate() != null ? bbtes.getBlockstate()
                : this.getBlock().getStateForPlacement(context);

        defstate = defstate.with(BoomboxBlock.FACING,
                this.getBlock().getStateForPlacement(context).get(BoomboxBlock.FACING));

        defstate = defstate.with(BoomboxBlock.WALL,
                this.getBlock().getStateForPlacement(context).get(BoomboxBlock.WALL));

        defstate = defstate.with(BoomboxBlock.WATERLOGGED,
                this.getBlock().getStateForPlacement(context).get(BoomboxBlock.WATERLOGGED));

        BlockState blockstate = defstate.with(IMPBooleanProperties.WALL,
                Boolean.valueOf(true));
        BlockState blockstate1 = null;
        IWorldReader iworldreader = context.getWorld();
        BlockPos blockpos = context.getPos();

        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction != Direction.UP) {
                BlockState blockstate2 = direction == Direction.DOWN ? defstate
                        : blockstate;
                if (blockstate2 != null && blockstate2.isValidPosition(iworldreader, blockpos)) {
                    blockstate1 = blockstate2;
                    break;
                }
            }
        }

        return blockstate1 != null && iworldreader.func_226663_a_(blockstate1, blockpos, ISelectionContext.dummy())
                ? blockstate1
                : null;
    }

}
