package dev.felnull.imp.inventory;

import dev.felnull.otyacraftengine.inventory.OEBEBaseMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;

public class MusicManagerMenu extends OEBEBaseMenu {

    public MusicManagerMenu(int windowId, Inventory playerInventory, BlockPos pos, Container container) {
        super(IMPMenus.MUSIC_MANAGER.get(), windowId, playerInventory, container, pos, -1, -1);
    }

    @Override
    protected void setSlot() {

    }
}
