package dev.felnull.imp.client.gui.screen.monitor.cassette_deck;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.blockentity.CassetteDeckBlockEntity;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.CassetteDeckScreen;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

public class MenuCDMonitor extends CassetteDeckMonitor {
    private static final Component WRITE = Component.translatable("imp.button.write");
    private static final Component PLAYBACK = Component.translatable("imp.button.playback");

    public MenuCDMonitor(CassetteDeckBlockEntity.MonitorType monitorType, CassetteDeckScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.addRenderWidget(new SmartButton(getStartX() + (width - 100) / 2, getStartY() + (height - 14 * 3) / 2, 100, 14, WRITE, n -> {
            insMonitor(CassetteDeckBlockEntity.MonitorType.WRITE);
        }));

        this.addRenderWidget(new SmartButton(getStartX() + (width - 100) / 2, getStartY() + (height - 14 * 3) / 2 + 14 * 2, 100, 14, PLAYBACK, n -> {
            insMonitor(CassetteDeckBlockEntity.MonitorType.PLAYBACK);
        }));
    }

    @Override
    public void renderAppearance(CassetteDeckBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        renderSmartButtonSprite(poseStack, multiBufferSource, (width - 100f) / 2f, (height - 14f * 3f) / 2f, OERenderUtils.MIN_BREADTH * 2, 100, 14, i, j, onPxW, onPxH, monitorHeight, WRITE, true);
        renderSmartButtonSprite(poseStack, multiBufferSource, (width - 100f) / 2f, (height - 14f * 3f) / 2f + 14f * 2f, OERenderUtils.MIN_BREADTH * 2, 100, 14, i, j, onPxW, onPxH, monitorHeight, PLAYBACK, true);
    }
}
