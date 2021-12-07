package dev.felnull.imp.inventory;

import dev.felnull.otyacraftengine.inventory.OEBaseContainerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;

public class MusicManagerMenu extends OEBaseContainerMenu {

    public MusicManagerMenu(int windowId, BlockPos pos, Container container, Inventory playerInventory) {
        super(IMPMenus.MUSIC_MANAGER, windowId, pos, container, playerInventory, -1, -1);
    }

    @Override
    protected void setSlot() {

    }
}
