package red.felnull.imp.tileentity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.block.BoomboxBlock;
import red.felnull.imp.block.propertie.BoomboxMode;
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
    public CompoundNBT instructionFromClient(ServerPlayerEntity player, String s, CompoundNBT tag) {
        if (s.equals("Mode")) {
            setMode(BoomboxMode.getScreenByName(tag.getString("name")));
        }
        return super.instructionFromClient(player, s, tag);
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new BoomboxContainer(id, player, this, getPos());
    }

    public BoomboxMode getMode() {
        return getBlockState().get(BoomboxBlock.BOOMBOX_MODE);
    }

    public void setMode(BoomboxMode mode) {
        setBlockState(getBlockState().with(BoomboxBlock.BOOMBOX_MODE, mode));
    }
}
