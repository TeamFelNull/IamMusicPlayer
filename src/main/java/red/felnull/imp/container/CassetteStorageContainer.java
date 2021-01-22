package red.felnull.imp.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import red.felnull.otyacraftengine.container.IkisugiContainer;


public class CassetteStorageContainer extends IkisugiContainer {
    public CassetteStorageContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, BlockPos pos) {
        super(IMPContainerTypes.CASSETTE_STORAGE, windowId, playerInventory, inventory, pos);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return false;
    }
}
