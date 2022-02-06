package dev.felnull.imp.blockentity;

import dev.felnull.imp.block.IMPBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class CassetteDeckBlockEntity extends IMPBaseEntityBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    public CassetteDeckBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IMPBlockEntitys.CASSETTE_DECK, blockPos, blockState);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected Component getDefaultName() {
        return IMPBlocks.CASSETTE_DECK.getName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }
}
