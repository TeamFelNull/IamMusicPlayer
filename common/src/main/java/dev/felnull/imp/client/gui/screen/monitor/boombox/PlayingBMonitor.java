package dev.felnull.imp.client.gui.screen.monitor.boombox;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.client.gui.screen.BoomboxScreen;
import net.minecraft.network.chat.TextComponent;

public class PlayingBMonitor extends BoomboxMonitor {
    public PlayingBMonitor(BoomboxBlockEntity.MonitorType monitorType, BoomboxScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        drawSmartText(poseStack, new TextComponent("TEST"), getStartX() + 3, getStartY() + 3);
    }
}
