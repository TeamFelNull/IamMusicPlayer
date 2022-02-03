package dev.felnull.imp.inventory;

import dev.felnull.otyacraftengine.inventory.OEBEBaseMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;

public class MusicManagerMenu extends OEBEBaseMenu {

    public MusicManagerMenu(int windowId, Inventory playerInventory, Container container, BlockPos pos) {
        super(IMPMenus.MUSIC_MANAGER, windowId, playerInventory, container, pos, -1, -1);
    }

    @Override
    protected void setSlot() {

    }
}
