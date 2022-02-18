package dev.felnull.imp.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.components.PlayBackControlWidget;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface IIMPSmartRender {
    @NotNull
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

    default void drawSmartCenterText(PoseStack poseStack, Component component, float x, float y) {
        OERenderUtil.drawCenterText(poseStack, component, x, y, 0xFF000000);
    }

    default void drawSmartFixedWidthText(PoseStack poseStack, Component component, float x, float y, float w) {
        drawSmartFixedWidthText(poseStack, component, x, y, w, 0xFF000000);
    }


    default void drawSmartFixedWidthText(PoseStack poseStack, Component component, float x, float y, float w, int color) {
        OERenderUtil.drawFixedWidthText(poseStack, component, x, y, color, w);
    }

    default void drawSmartText(PoseStack poseStack, Component component, float x, float y) {
        drawSmartText(poseStack, component, x, y, 0xFF000000);
    }

    default void drawSmartText(PoseStack poseStack, Component component, float x, float y, int color) {
        mc.font.draw(poseStack, component, x, y, color);
    }

    default void renderSmartButtonSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, ResourceLocation iconLocation, int iconStX, int iconStY, int iconW, int iconH, int iconTexW, int iconTexH, boolean disActive) {
        renderSmartButtonSprite(poseStack, multiBufferSource, x, y, z, w, h, i, j, onePixW, onePixH, monitorHeight, null, false, iconLocation, iconStX, iconStY, iconW, iconH, iconTexW, iconTexH, disActive);
    }

    default void renderSmartButtonSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, ResourceLocation iconLocation, int iconStX, int iconStY, int iconW, int iconH, int iconTexW, int iconTexH) {
        renderSmartButtonSprite(poseStack, multiBufferSource, x, y, z, w, h, i, j, onePixW, onePixH, monitorHeight, null, false, iconLocation, iconStX, iconStY, iconW, iconH, iconTexW, iconTexH);
    }

    default void renderSmartButtonSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, Component text, ResourceLocation iconLocation, int iconStX, int iconStY, int iconW, int iconH, int iconTexW, int iconTexH, boolean disActive) {
        renderSmartButtonSprite(poseStack, multiBufferSource, x, y, z, w, h, i, j, onePixW, onePixH, monitorHeight, text, false, iconLocation, iconStX, iconStY, iconW, iconH, iconTexW, iconTexH, disActive);
    }

    default void renderSmartButtonSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, Component text, ResourceLocation iconLocation, int iconStX, int iconStY, int iconW, int iconH, int iconTexW, int iconTexH) {
        renderSmartButtonSprite(poseStack, multiBufferSource, x, y, z, w, h, i, j, onePixW, onePixH, monitorHeight, text, false, iconLocation, iconStX, iconStY, iconW, iconH, iconTexW, iconTexH);
    }

    default void renderSmartButtonSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, Component text, boolean center, boolean disActive) {
        renderSmartButtonSprite(poseStack, multiBufferSource, x, y, z, w, h, i, j, onePixW, onePixH, monitorHeight, text, center, null, 0, 0, 0, 0, 0, 0, disActive);
    }

    default void renderSmartButtonSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, Component text, boolean center) {
        renderSmartButtonSprite(poseStack, multiBufferSource, x, y, z, w, h, i, j, onePixW, onePixH, monitorHeight, text, center, null, 0, 0, 0, 0, 0, 0);
    }

    default void renderSmartCenterTextSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, Component text, float x, float y, float z, float onePixW, float onePixH, float monitorHeight, float scale, int combinedLightIn) {
        OERenderUtil.renderCenterTextSprite(poseStack, multiBufferSource, text, onePixW * x, monitorHeight - onePixH * (y + 7), z, 0.175f * scale, 0, 0, combinedLightIn);
    }

    default void renderSmartTextSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, Component text, float x, float y, float z, float onePixW, float onePixH, float monitorHeight, float scale, int combinedLightIn) {
        OERenderUtil.renderTextSprite(poseStack, multiBufferSource, text, onePixW * x, monitorHeight - onePixH * (y + 7), z, 0.175f * scale, 0, 0, combinedLightIn);
    }

    default void renderSmartTextSpriteColorSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, Component text, float x, float y, float z, float onePixW, float onePixH, float monitorHeight, int color, int combinedLightIn) {
        OERenderUtil.renderTextSprite(poseStack, multiBufferSource, text, onePixW * x, monitorHeight - onePixH * (y + 7), z, 0.175f, 0, 0, color, combinedLightIn);
    }

    default void renderSmartCenterTextSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, Component text, float x, float y, float z, float onePixW, float onePixH, float monitorHeight, int combinedLightIn) {
        renderSmartCenterTextSprite(poseStack, multiBufferSource, text, x, y, z, onePixW, onePixH, monitorHeight, getDefaultRenderTextScale(), combinedLightIn);
    }

    default void renderSmartTextSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, Component text, float x, float y, float z, float onePixW, float onePixH, float monitorHeight, int combinedLightIn) {
        renderSmartTextSprite(poseStack, multiBufferSource, text, x, y, z, onePixW, onePixH, monitorHeight, getDefaultRenderTextScale(), combinedLightIn);
    }

    default float getDefaultRenderTextScale() {
        return 1.f;
    }

    default void renderSmartButtonSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, Component text, boolean center, ResourceLocation iconLocation, int iconStX, int iconStY, int iconW, int iconH, int iconTexW, int iconTexH) {
        renderSmartButtonSprite(poseStack, multiBufferSource, x, y, z, w, h, i, j, onePixW, onePixH, monitorHeight, text, center, iconLocation, iconStX, iconStY, iconW, iconH, iconTexW, iconTexH, false);
    }

    default void renderSmartButtonSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, Component text, boolean center, ResourceLocation iconLocation, int iconStX, int iconStY, int iconW, int iconH, int iconTexW, int iconTexH, boolean disActive) {
        float y1 = monitorHeight - onePixH * y - onePixH * h;
        renderSmartButtonBoxSprite(poseStack, multiBufferSource, x, y, z, w, h, i, j, onePixW, onePixH, monitorHeight, disActive);
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
            renderSmartTextSprite(poseStack, multiBufferSource, text, x + ax, y + (h - 5f) / 2f, z + OERenderUtil.MIN_BREADTH * 2, onePixW, onePixH, monitorHeight, i);
    }

    default void renderSmartButtonBoxSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight) {
        renderSmartButtonBoxSprite(poseStack, multiBufferSource, x, y, z, w, h, i, j, onePixW, onePixH, monitorHeight, false);
    }

    default void renderSmartButtonBoxSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, boolean disActive) {
        float y1 = monitorHeight - onePixH * y - onePixH * h;
        OERenderUtil.renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, onePixW * x, y1, z, 0, 0, 0, onePixW * w, onePixH * h, disActive ? 9 : 0, 87, 9, 4, 256, 256, i, j);
        OERenderUtil.renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, onePixW * x + onePixW, y1 + onePixH, z + OERenderUtil.MIN_BREADTH, 0, 0, 0, onePixW * w - (onePixW * 2), onePixH * h - (onePixH * 2), disActive ? 9 : 0, 83, 9, 4, 256, 256, i, j);
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
            renderSmartTextSpriteColorSprite(poseStack, multiBufferSource, new TextComponent(str), x + 3, y + 5, z + OERenderUtil.MIN_BREADTH * 3, onePixW, onePixH, monitorHeight, 0xFFFFFFFF, i);
        }
    }

    default void renderSmartRadioButtonSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, Component label, boolean selected) {
        renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, x, y, z, w, h, 18, 65 + (selected ? 20 : 0), 20, 20, 256, 256, i, j, onePixW, onePixH, monitorHeight);
        renderSmartTextSprite(poseStack, multiBufferSource, label, x + 24, y + (h - 7f) / 2f, z, onePixW, onePixH, monitorHeight, i);
    }

    default void renderPlayerFaceSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, UUID uuid, float x, float y, float z, float size, int i, int j, float onePixW, float onePixH, float monitorHeight) {
        OERenderUtil.renderPlayerFaceSprite(poseStack, multiBufferSource, uuid, onePixW * x, monitorHeight - onePixH * (y + size), z, 0, 0, 0, onePixH * size, i, j);
    }

    default void renderPlayListImage(PoseStack poseStack, MultiBufferSource multiBufferSource, ImageInfo imageInfo, float x, float y, float z, float size, int i, int j, float onePixW, float onePixH, float monitorHeight) {
        PlayImageRenderer.getInstance().renderSprite(imageInfo, poseStack, multiBufferSource, x * onePixW, monitorHeight - (y + size) * onePixH, z, size * onePixH, i, j);
    }

    default <E> void renderFixedListSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, float onePixW, float onePixH, float monitorHeight, List<E> list, int num, ListEntryRender<E> render) {
        int plsc = 0;
        if (list != null) {
            plsc = list.size();
            int on = (int) (h / num);
            for (int k = 0; k < Math.min(num, list.size()); k++) {
                var e = list.get(k);
                render.renderListEntry(poseStack, multiBufferSource, x, y + k * on, z, w - 9, on, i, j, e);
            }
        }
        renderScrollbarSprite(poseStack, multiBufferSource, x + w - 9, y, z, h, i, j, onePixW, onePixH, monitorHeight, plsc, num);
    }

    default void renderVolumeSprite(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, int i, int j, float onePixW, float onePixH, float monitorHeight, int volume, boolean mute) {
        int nv = mute ? 3 : volume / 100;
        int lfs = (nv * 2) + (volume <= 0 ? 0 : 2);
        renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, x, y, z, mute ? 4 : 4 + lfs, 8, 0, 156, mute ? 4 : 4 + lfs, 8, 256, 256, i, j, onePixW, onePixH, monitorHeight);
        renderSmartTextSpriteColorSprite(poseStack, multiBufferSource, new TextComponent(String.valueOf(volume)), x + 5 + lfs, y + 0.5f, z, onePixW, onePixH, monitorHeight, 0XFF115D0E, i);
        if (mute)
            renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, x + 3, y, z, 8, 8, 12, 156, 8, 8, 256, 256, i, j, onePixW, onePixH, monitorHeight);
    }

    default void renderPlayBackControl(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, int i, int j, float onePixW, float onePixH, float monitorHeight, PlayBackControlWidget.StateType stateType) {
        renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, x, y, z, 10, 10, stateType.ordinal() * 10 + z, 145, 10, 10, 256, 256, i, j, onePixW, onePixH, monitorHeight);
    }

    default void renderLoopControl(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, int i, int j, float onePixW, float onePixH, float monitorHeight, boolean loop) {
        renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, x, y, z, 8, 7, loop ? 8 : 0, 164, 8, 7, 256, 256, i, j, onePixW, onePixH, monitorHeight);
    }

    default void renderPlayProgress(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, int i, int j, float onePixW, float onePixH, float monitorHeight, float w, float progress) {
        renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, x, y, z, w, 3, 58, 81, w, 3, 256, 256, i, j, onePixW, onePixH, monitorHeight);
        renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, x, y, z + OERenderUtil.MIN_BREADTH, w * progress, 3, 58, 78, w * progress, 3, 256, 256, i, j, onePixW, onePixH, monitorHeight);
    }

    public static interface ListEntryRender<E> {
        public void renderListEntry(PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, int i, int j, E entry);
    }
}
