package red.felnull.imp.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.container.CassetteDeckContainer;

public class CassetteDeckTileEntity extends IMPAbstractEquipmentTileEntity {
    protected NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);

    public CassetteDeckTileEntity() {
        super(IMPTileEntityTypes.CASSETTE_DECK);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.cassette_deck");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new CassetteDeckContainer(id, player, this, getPos());
    }

    @Override
    public void readByIKSG(BlockState state, CompoundNBT tag) {
        super.readByIKSG(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        return tag;
    }
}
