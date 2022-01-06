package dev.felnull.imp.client.gui.screen;

import dev.felnull.imp.blockentity.IMPBaseEntityBlockEntity;
import dev.felnull.otyacraftengine.client.gui.screen.OEBaseContainerScreen;
import dev.felnull.otyacraftengine.inventory.OEBaseContainerMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class IMPBaseContainerScreen<T extends OEBaseContainerMenu> extends OEBaseContainerScreen<T> {
    public IMPBaseContainerScreen(T abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    public void insPower(boolean on) {
        instruction("power", on ? 1 : 0, new CompoundTag());
    }

    public boolean isPower() {
        if (getBlockEntity() instanceof IMPBaseEntityBlockEntity impBase)
            return impBase.isPower();
        return false;
    }
}
