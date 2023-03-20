package dev.felnull.imp.client.gui.screen;

import dev.felnull.imp.blockentity.IMPBaseEntityBlockEntity;
import dev.felnull.otyacraftengine.client.gui.screen.OEBEContainerBasedScreen;
import dev.felnull.otyacraftengine.inventory.OEBEBaseMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class IMPBaseContainerScreen<T extends OEBEBaseMenu> extends OEBEContainerBasedScreen<T> {
    public IMPBaseContainerScreen(T abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    public void insPower(boolean on) {
        var tag = new CompoundTag();
        tag.putBoolean("power", on);
        instruction("power", tag);
    }

    public boolean isPowered() {
        if (getBlockEntity() instanceof IMPBaseEntityBlockEntity impBase)
            return impBase.isPowered();
        return false;
    }
}
