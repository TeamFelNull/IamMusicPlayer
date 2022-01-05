package dev.felnull.imp.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.screen.monitor.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

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

    default void drawSmartFixedWidthString(PoseStack poseStack, Component component, float x, float y, float w) {
        drawSmartFixedWidthString(poseStack, component, x, y, w, 0xFF000000);
    }

    default void drawSmartFixedWidthString(PoseStack poseStack, Component component, float x, float y, float w, int color) {
        OERenderUtil.drawFixedWidthText(poseStack, component, x, y, color, w);
    }

    default void drawSmartString(PoseStack poseStack, Component component, float x, float y) {
        mc.font.draw(poseStack, component, x, y, 0xFF000000);
    }

    default void renderSmartButtonSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, ResourceLocation iconLocation, int iconStX, int iconStY, int iconW, int iconH, int iconTexW, int iconTexH) {
        renderSmartButtonSprite(poseStack, multiBufferSource, x, y, z, w, h, i, j, onePixW, onePixH, monitorHeight, null, false, iconLocation, iconStX, iconStY, iconW, iconH, iconTexW, iconTexH);
    }

    default void renderSmartButtonSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, Component text, ResourceLocation iconLocation, int iconStX, int iconStY, int iconW, int iconH, int iconTexW, int iconTexH) {
        renderSmartButtonSprite(poseStack, multiBufferSource, x, y, z, w, h, i, j, onePixW, onePixH, monitorHeight, text, false, iconLocation, iconStX, iconStY, iconW, iconH, iconTexW, iconTexH);
    }

    default void renderSmartButtonSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, Component text, boolean center) {
        renderSmartButtonSprite(poseStack, multiBufferSource, x, y, z, w, h, i, j, onePixW, onePixH, monitorHeight, text, center, null, 0, 0, 0, 0, 0, 0);
    }

    default void renderSmartTextSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, Component text, float x, float y, float z, float onePixW, float onePixH, float monitorHeight, float scale, int combinedLightIn) {
        OERenderUtil.renderTextSprite(poseStack, multiBufferSource, text, onePixW * x, monitorHeight - onePixH * (y + 7), z, 0.175f * scale, 0, 0, combinedLightIn);
    }

    default void renderSmartTextSpriteColor(PoseStack poseStack, MultiBufferSource multiBufferSource, Component text, float x, float y, float z, float onePixW, float onePixH, float monitorHeight, int color, int combinedLightIn) {
        OERenderUtil.renderTextSprite(poseStack, multiBufferSource, text, onePixW * x, monitorHeight - onePixH * (y + 7), z, 0.175f, 0, 0, color, combinedLightIn);
    }

    default void renderSmartTextSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, Component text, float x, float y, float z, float onePixW, float onePixH, float monitorHeight, int combinedLightIn) {
        renderSmartTextSprite(poseStack, multiBufferSource, text, x, y, z, onePixW, onePixH, monitorHeight, 1.0f, combinedLightIn);
    }

    default void renderSmartButtonSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, Component text, boolean center, ResourceLocation iconLocation, int iconStX, int iconStY, int iconW, int iconH, int iconTexW, int iconTexH) {
        float y1 = monitorHeight - onePixH * y - onePixH * h;
        renderSmartButtonBoxSprite(poseStack, multiBufferSource, x, y, z, w, h, i, j, onePixW, onePixH, monitorHeight);
        float ax = 1;

        if (text != null && center)
            ax = (w - mc.font.width(text)) / 2f;

        if (iconLocation != null) {
            float itx = text != null ? 2 : (w - iconW) / 2f;
            float ity = (h - iconH) / 2f;
            ax += itx + iconW;
            OERenderUtil.renderTextureSprite(iconLocation, poseStack, multiBufferSource, onePixW * (x + itx), y1 + onePixH * ity, z + OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, onePixW * iconW, onePixH * iconH, iconStX, iconStY, iconW, iconH, iconTexW, iconTexH, i, j);
        }

        if (text != null)
            renderSmartTextSprite(poseStack, multiBufferSource, text, x + ax, y + 2, z + OERenderUtil.MIN_BREADTH * 2, onePixW, onePixH, monitorHeight, i);
    }

    default void renderSmartButtonBoxSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight) {
        float y1 = monitorHeight - onePixH * y - onePixH * h;
        OERenderUtil.renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, onePixW * x, y1, z, 0, 0, 0, onePixW * w, onePixH * h, 0, 87, 9, 4, 256, 256, i, j);
        OERenderUtil.renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, onePixW * x + onePixW, y1 + onePixH, z + OERenderUtil.MIN_BREADTH, 0, 0, 0, onePixW * w - (onePixW * 2), onePixH * h - (onePixH * 2), 0, 83, 9, 4, 256, 256, i, j);
    }

    default void renderScrollbarSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, int comp, int total) {
        float s = Mth.clamp(h / ((float) comp / (float) total), 10, h) / h;
        renderScrollbarSprite(poseStack, multiBufferSource, x, y, z, h, i, j, onePixW, onePixH, monitorHeight, s);
    }

    default void renderScrollbarSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, float size) {
        renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, x, y, z, 9, 3, 9, 20, 9, 3, 256, 256, i, j, onePixW, onePixH, monitorHeight);
        renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, x, y + h - 3, z, 9, 3, 9, 39, 9, 3, 256, 256, i, j, onePixW, onePixH, monitorHeight);

        float h1 = h - 6;
        int ct = (int) h1 / 16;
        float am = h1 - ct * 16;
        for (int k = 0; k < ct; k++) {
            renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, x, y + k * 16 + 3, z, 9, 16, 9, 23, 9, 16, 256, 256, i, j, onePixW, onePixH, monitorHeight);
        }
        renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, x, y + ct * 16 + 3, z, 9, am, 9, 23, 9, am, 256, 256, i, j, onePixW, onePixH, monitorHeight);

        float h2 = h - 2;
        float h3 = h2 * size;
        renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, x + 1, y + 1, z + OERenderUtil.MIN_BREADTH, 7, 3, 0, 42, 7, 3, 256, 256, i, j, onePixW, onePixH, monitorHeight);
        renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, x + 1, y + 1 + h3 - 3, z + OERenderUtil.MIN_BREADTH, 7, 3, 0, 59, 7, 3, 256, 256, i, j, onePixW, onePixH, monitorHeight);

        float h4 = h3 - 6;
        int ict = (int) h4 / 14;
        float iam = h4 - ict * 14;

        for (int k = 0; k < ict; k++) {
            renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, x + 1, y + 1 + k * 14 + 3, z + OERenderUtil.MIN_BREADTH, 7, 14, 0, 45, 7, 14, 256, 256, i, j, onePixW, onePixH, monitorHeight);
        }
        renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, x + 1, y + 1 + ict * 14 + 3, z + OERenderUtil.MIN_BREADTH, 7, iam, 0, 45, 7, iam, 256, 256, i, j, onePixW, onePixH, monitorHeight);
    }

    default void renderTextureSprite(ResourceLocation location, PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, float texStartX, float texStartY, float texFinishX, float texFinishY, float texSizeW, float texSizeH, int combinedLightIn, int combinedOverlayIn, float onePixW, float onePixH, float monitorHeight) {
        float y1 = monitorHeight - onePixH * y - onePixH * h;
        OERenderUtil.renderTextureSprite(location, poseStack, multiBufferSource, onePixW * x, y1, z, 0, 0, 0, onePixW * w, onePixH * h, texStartX, texStartY, texFinishX, texFinishY, texSizeW, texSizeH, combinedLightIn, combinedOverlayIn);
    }

    default void renderSmartEditBoxSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, String text) {
        y--;
        h += 2;
        float y1 = monitorHeight - onePixH * y - onePixH * h;
        OERenderUtil.renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, onePixW * x, y1, z, 0, 0, 0, onePixW * w, onePixH * h, 0, 91, 9, 4, 256, 256, i, j);
        OERenderUtil.renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, onePixW * x + onePixW, y1 + onePixH, z + OERenderUtil.MIN_BREADTH, 0, 0, 0, onePixW * w - (onePixW * 2), onePixH * h - (onePixH * 2), 0, 95, 9, 4, 256, 256, i, j);
        if (text != null) {
            String str;
            if (mc.font.width(text) > w - 6) {
                StringBuilder sb = new StringBuilder();
                for (char c : text.toCharArray()) {
                    sb.append(c);
                    if (mc.font.width(sb.toString()) > w - 6) {
                        sb.deleteCharAt(sb.length() - 1);
                        break;
                    }
                }
                str = sb.toString();
            } else {
                str = text;
            }
            renderSmartTextSpriteColor(poseStack, multiBufferSource, new TextComponent(str), x + 3, y + 5, z + OERenderUtil.MIN_BREADTH * 3, onePixW, onePixH, monitorHeight, 0xFFFFFFFF, i);
        }
    }

}
