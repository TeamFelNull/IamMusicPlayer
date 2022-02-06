package dev.felnull.imp.inventory;

import dev.felnull.imp.inventory.slot.CassetteTapeSlot;
import dev.felnull.otyacraftengine.inventory.OEBEBaseMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;

public class CassetteDeckMenu extends OEBEBaseMenu {
    public CassetteDeckMenu(int windowId, Inventory playerInventory, Container container, BlockPos pos) {
        super(IMPMenus.CASSETTE_DECK, windowId, playerInventory, container, pos, 8, 94);
    }

    @Override
    protected void setSlot() {
        this.addSlot(new CassetteTapeSlot(getContainer(), 0, 183, 99));
    }
}
