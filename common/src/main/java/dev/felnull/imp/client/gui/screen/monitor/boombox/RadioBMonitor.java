package dev.felnull.imp.client.gui.screen.monitor.boombox;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.BoomboxScreen;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.data.BoomboxData;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RadioBMonitor extends PlayBackBaseBMonitor {
    private static final ResourceLocation BACK_BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox/monitor/playback_back.png");
    private static final Component LOADING_STREAM_TEXT = new TranslatableComponent("imp.text.streamLoading");
    private static final Component PLAYING_STREAM_TEXT = new TranslatableComponent("imp.text.streamPlaying");
    private static final Component NON_PROGRESS_TEXT = new TextComponent("--:--/--:--");
    private SmartButton backButton;

    public RadioBMonitor(BoomboxData.MonitorType monitorType, BoomboxScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.backButton = this.addRenderWidget(new SmartButton(getStartX() + width - 15, getStartY() + height - 12, 14, 11, new TranslatableComponent("gui.back"), n -> setMonitor(BoomboxData.MonitorType.RADIO_SELECT)));
        this.backButton.setHideText(true);
        this.backButton.setIcon(MusicManagerMonitor.WIDGETS_TEXTURE, 11, 123, 8, 8);
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(BACK_BG_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
        var ptx = LOADING_STREAM_TEXT;
        if (!getScreen().isMusicLoading() && getScreen().getBoomBoxData().isPlaying()) {
            ptx = PLAYING_STREAM_TEXT;
        } else if (!getScreen().getBoomBoxData().isPlaying()) {
            ptx = NON_PROGRESS_TEXT;
        }
        drawSmartText(poseStack, ptx, getStartX() + 38 - (isShortProgressBar() ? 0 : 36), getStartY() + 15, 0XFF115D0E);
    }

    @Override
    public void renderAppearance(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight, BoomboxData data) {
        super.renderAppearance(poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight, data);
        OERenderUtil.renderTextureSprite(BACK_BG_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
        var ptx = LOADING_STREAM_TEXT;
        if (!data.isLoadingMusic() && data.isPlaying()) {
            ptx = PLAYING_STREAM_TEXT;
        } else if (!data.isPlaying()) {
            ptx = NON_PROGRESS_TEXT;
        }
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;

        renderSmartTextSpriteColorSprite(poseStack, multiBufferSource, ptx, 38f - (isShortProgressBar(data) ? 0 : 36f), 17f, OERenderUtil.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, 0XFF115D0E, i);

        renderSmartButtonSprite(poseStack, multiBufferSource, width - 15, height - 12, OERenderUtil.MIN_BREADTH * 2f, 14, 11, i, j, onPxW, onPxH, monitorHeight, MusicManagerMonitor.WIDGETS_TEXTURE, 11, 123, 8, 8, 256, 256);
    }

    @Override
    protected @NotNull ImageInfo getPlayBackImage(BoomboxData data) {
        return data.getRadioImage();
    }

    @Override
    protected @NotNull String getPlayBackName(BoomboxData data) {
        return data.getRadioName();
    }

    @Override
    protected @NotNull String getPlayBackAuthor(BoomboxData data) {
        return data.getRadioAuthor();
    }

    @Override
    protected @NotNull MusicSource getPlayBackSource(BoomboxData data) {
        return data.getRadioSource();
    }

    @Override
    protected boolean isShortTipProgressBar() {
        return true;
    }
}
