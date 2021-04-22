package red.felnull.imp.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import red.felnull.otyacraftengine.inventory.IkisugiBaseContainerMenu;

public class MusicSharingDeviceMenu extends IkisugiBaseContainerMenu {

    public MusicSharingDeviceMenu(int windowId, BlockPos pos, Container container, Inventory inventory) {
        super(IMPMenus.MUSIC_SHARING_DEVICE, windowId, pos, container, inventory, 8, 160);
    }

    @Override
    protected void setSlot() {
        this.addSlot(new AntennaSlot(getContainer(), 0, 183, 178));
    }

}
