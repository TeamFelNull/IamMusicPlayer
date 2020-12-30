package red.felnull.imp.tileentity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.container.BoomboxContainer;

public class BoomboxTileEntity extends IMPAbstractEquipmentTileEntity {
    protected NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public BoomboxTileEntity() {
        super(IMPTileEntityTypes.BOOMBOX);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.boombox");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new BoomboxContainer(id, player, this, getPos());
    }
}
