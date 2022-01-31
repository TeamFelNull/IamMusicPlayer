package dev.felnull.imp.inventory;

import dev.felnull.otyacraftengine.inventory.OEItemBEBaseMenu;
import dev.felnull.otyacraftengine.item.location.IPlayerItemLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BoomboxMenu extends OEItemBEBaseMenu {

    public BoomboxMenu(int i, Inventory playerInventory, Container container, BlockPos pos, ItemStack itemStack, IPlayerItemLocation location) {
        super(IMPMenus.BOOMBOX, i, playerInventory, container, pos, itemStack, location, 8, 83);
    }

    @Override
    protected void setSlot() {
        this.addSlot(new Slot(getContainer(), 0, 151, 47));
        this.addSlot(new Slot(getContainer(), 1, 151, 47 + 32));
    }
}
