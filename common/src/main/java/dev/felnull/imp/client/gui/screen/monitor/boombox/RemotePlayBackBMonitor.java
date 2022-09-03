package dev.felnull.imp.client.gui.screen.monitor.boombox;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.value.Value;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.block.BoomboxData;
import dev.felnull.imp.client.gui.components.ContinuousWidget;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.BoomboxScreen;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RemotePlayBackBMonitor extends PlayBackFiniteBaseBMMonitor {
    private static final ResourceLocation BACK_BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox/monitor/playback_back.png");
    private SmartButton backButton;
    private ContinuousWidget continuousWidget;

    public RemotePlayBackBMonitor(BoomboxData.MonitorType monitorType, BoomboxScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.backButton = this.addRenderWidget(new SmartButton(getStartX() + width - 15, getStartY() + height - 12, 14, 11, Component.translatable("gui.back"), n -> setMonitor(BoomboxData.MonitorType.REMOTE_PLAYBACK_SELECT)));
        this.backButton.setHideText(true);
        this.backButton.setIcon(MusicManagerMonitor.WIDGETS_TEXTURE, 11, 123, 8, 8);

        this.continuousWidget = this.addRenderWidget(new ContinuousWidget(getStartX() + 112, getStartY() + 13, new Value<>() {
            @Override
            public void accept(BoomboxData.ContinuousType continuousType) {
                getScreen().insContinuousType(continuousType);
            }

            @Override
            public BoomboxData.ContinuousType get() {
                return getScreen().getBoomBoxData().getContinuousType();
            }
        }));

        this.continuousWidget.visible = canPlay();
    }

    @Override
    public void tick() {
        super.tick();

        this.continuousWidget.active = canPlay();
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtils.drawTexture(BACK_BG_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
    }

    @Override
    public void renderAppearance(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight, BoomboxData data) {
        super.renderAppearance(poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight, data);
        OERenderUtils.renderTextureSprite(BACK_BG_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtils.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;


        renderSmartButtonSprite(poseStack, multiBufferSource, width - 15, height - 12, OERenderUtils.MIN_BREADTH * 2f, 14, 11, i, j, onPxW, onPxH, monitorHeight, MusicManagerMonitor.WIDGETS_TEXTURE, 11, 123, 8, 8, 256, 256);

        renderSmartCenterTextSprite(poseStack, multiBufferSource, data.getContinuousType().getComponent(), 132, 16, OERenderUtils.MIN_BREADTH * 2f, onPxW, onPxH, monitorHeight, i, 0XFF115D0E);
    }

    @Override
    protected @NotNull ImageInfo getPlayBackImage(BoomboxData data) {
        var m = getMusic(data);
        if (m != null) return m.getImage();
        return ImageInfo.EMPTY;
    }

    @Override
    protected @NotNull String getPlayBackName(BoomboxData data) {
        var m = getMusic(data);
        if (m != null) return m.getName();
        return "";
    }

    @Override
    protected @NotNull String getPlayBackAuthor(BoomboxData data) {
        var m = getMusic(data);
        if (m != null) return m.getAuthor();
        return "";
    }

    @Override
    protected @NotNull MusicSource getPlayBackSource(BoomboxData data) {
        var m = getMusic(data);
        if (m != null) return m.getSource();
        return MusicSource.EMPTY;
    }

    private Music getMusic() {
        return getScreen().getBoomBoxData().getSelectedMusic();
    }

    private Music getMusic(BoomboxData data) {
        return data.getSelectedMusic();
    }

    @Override
    protected boolean isShortTipProgressBar() {
        return true;
    }
}
