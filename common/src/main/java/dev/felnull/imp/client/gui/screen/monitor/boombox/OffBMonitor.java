package dev.felnull.imp.client.gui.screen.monitor.boombox;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.client.gui.screen.BoomboxScreen;
import net.minecraft.client.renderer.MultiBufferSource;

public class OffBMonitor extends BoomboxMonitor {
    public OffBMonitor(BoomboxBlockEntity.MonitorType monitorType, BoomboxScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {

    }

    @Override
    public void renderAppearance(BoomboxBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {

    }
}
