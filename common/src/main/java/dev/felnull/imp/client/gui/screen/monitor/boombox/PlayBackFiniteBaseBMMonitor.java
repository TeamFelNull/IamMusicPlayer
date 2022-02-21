package dev.felnull.imp.client.gui.screen.monitor.boombox;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.fnjl.util.FNStringUtil;
import dev.felnull.imp.client.gui.screen.BoomboxScreen;
import dev.felnull.imp.data.BoomboxData;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public abstract class PlayBackFiniteBaseBMMonitor extends PlayBackBaseBMonitor {
    private static final Component LOADING_MUSIC_TEXT = new TranslatableComponent("imp.text.musicLoading");

    public PlayBackFiniteBaseBMMonitor(BoomboxData.MonitorType monitorType, BoomboxScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        if (!canPlay()) return;
        var ptx = LOADING_MUSIC_TEXT;
        if (!getScreen().isMusicLoading())
            ptx = new TextComponent(FNStringUtil.getTimeProgress(getScreen().getMusicPosition(), getPlayBackSource().getDuration()));
        drawSmartText(poseStack, ptx, getStartX() + 38 - (isShortProgressBar() ? 0 : 36), getStartY() + 15, 0XFF115D0E);
    }

    @Override
    public void renderAppearance(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight, BoomboxData data) {
        super.renderAppearance(poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight, data);
        if (!canPlay(data)) return;
        var ptx = LOADING_MUSIC_TEXT;
        if (!data.isLoadingMusic())
            ptx = new TextComponent(FNStringUtil.getTimeProgress(data.getMusicPosition(), getPlayBackSource(data).getDuration()));

        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;

        renderSmartTextSpriteColorSprite(poseStack, multiBufferSource, ptx, 38f - (isShortProgressBar(data) ? 0 : 36f), 17f, OERenderUtil.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, 0XFF115D0E, i);
    }
}
