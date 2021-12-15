package dev.felnull.imp.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.screen.monitor.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface IIMPSmartRender {
    Minecraft mc = Minecraft.getInstance();

    default void drawFill(PoseStack poseStack, int x, int y, int w, int h, int col) {
        GuiComponent.fill(poseStack, x, y, x + w, y + h, col);
    }

    default void drawSmartButtonBox(PoseStack poseStack, int x, int y, int w, int h, boolean press) {
        drawSmartButtonBox(poseStack, x, y, w, h, press ? 2 : 1);
    }

    default void drawSmartButtonBox(PoseStack poseStack, int x, int y, int w, int h, int st) {
        int c1 = st == 0 ? 0xFF383838 : st == 1 ? 0xFFa9a9a9 : 0xFF9ebdd1;
        int c2 = st == 0 ? 0xFF454545 : st == 1 ? 0xFFb9b9b9 : 0xFFb3d4e9;

        drawFill(poseStack, x, y, w, h, c1);
        drawFill(poseStack, x + 1, y + 1, w - 2, h - 2, c2);
    }

    default void drawSmartCenterString(PoseStack poseStack, Component component, float x, float y) {
        OERenderUtil.drawCenterText(poseStack, component, x, y, 0xFF000000);
    }

    default void drawSmartString(PoseStack poseStack, Component component, float x, float y) {
        mc.font.draw(poseStack, component, x, y, 0xFF000000);
    }

    default void renderSmartButtonSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, Component text, ResourceLocation iconLocation, int iconStX, int iconStY, int iconW, int iconH, int iconTexW, int iconTexH) {
        renderSmartButtonSprite(poseStack, multiBufferSource, x, y, z, w, h, i, j, onePixW, onePixH, monitorHeight, text, false, iconLocation, iconStX, iconStY, iconW, iconH, iconTexW, iconTexH);
    }

    default void renderSmartButtonSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, Component text, boolean center) {
        renderSmartButtonSprite(poseStack, multiBufferSource, x, y, z, w, h, i, j, onePixW, onePixH, monitorHeight, text, center, null, 0, 0, 0, 0, 0, 0);
    }

    default void renderSmartStringSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, Component text, float x, float y, float z, float onePixW, float onePixH, float monitorHeight) {
        OERenderUtil.renderTextSprite(poseStack, multiBufferSource, text, onePixW * x, monitorHeight - onePixH * (y + 7), z, 0.175f, 0, 0);
    }

    default void renderSmartButtonSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, Component text, boolean center, ResourceLocation iconLocation, int iconStX, int iconStY, int iconW, int iconH, int iconTexW, int iconTexH) {
        float y1 = monitorHeight - onePixH * y - onePixH * h;
        OERenderUtil.renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, onePixW * x, y1, z, 0, 0, 0, onePixW * w, onePixH * h, 0, 87, 9, 4, 256, 256, i, j);
        OERenderUtil.renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, onePixW * x + onePixW, y1 + onePixH, z + OERenderUtil.MIN_BREADTH, 0, 0, 0, onePixW * w - (onePixW * 2), onePixH * h - (onePixH * 2), 0, 83, 9, 4, 256, 256, i, j);

        float ax = 1;

        if (center)
            ax = (w - mc.font.width(text)) / 2f;

        if (iconLocation != null) {
            float itx = 2;
            float ity = (h - iconH) / 2f;
            ax += itx + iconW;
            OERenderUtil.renderTextureSprite(iconLocation, poseStack, multiBufferSource, onePixW * (x + itx), y1 + onePixH * ity, z + OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, onePixW * iconW, onePixH * iconH, iconStX, iconStY, iconW, iconH, iconTexW, iconTexH, i, j);
        }

        renderSmartStringSprite(poseStack, multiBufferSource, text, x + ax, y + 2, z + OERenderUtil.MIN_BREADTH * 2, onePixW, onePixH, monitorHeight);
    }
}
