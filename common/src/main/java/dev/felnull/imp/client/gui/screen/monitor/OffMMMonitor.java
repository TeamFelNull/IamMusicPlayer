package dev.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import net.minecraft.client.renderer.MultiBufferSource;

public class OffMMMonitor extends MusicManagerMonitor {
    public OffMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
        this.header = false;
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
    }
}
