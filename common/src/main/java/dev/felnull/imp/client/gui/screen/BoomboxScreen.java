package dev.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.inventory.BoomboxMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BoomboxScreen extends AbstractContainerScreen<BoomboxMenu> {
    public BoomboxScreen(BoomboxMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float f, int i, int j) {

    }
}
