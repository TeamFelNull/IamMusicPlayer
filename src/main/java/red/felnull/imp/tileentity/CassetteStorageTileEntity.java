package red.felnull.imp.tileentity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.container.BoomboxContainer;
import red.felnull.imp.container.CassetteStorageContainer;

public class CassetteStorageTileEntity extends IMPAbstractTileEntity {

    public CassetteStorageTileEntity() {
        super(IMPTileEntityTypes.CASSETTE_STORAGE);
    }

    @Override
    protected int getInventorySize() {
        return 16;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.cassette_storage");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new CassetteStorageContainer(id, player, this, getPos());
    }

}


