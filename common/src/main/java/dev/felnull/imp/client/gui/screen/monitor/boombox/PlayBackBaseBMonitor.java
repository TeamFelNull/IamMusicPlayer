package dev.felnull.imp.client.gui.screen.monitor.boombox;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.value.IntValue;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.gui.components.PlayBackControlWidget;
import dev.felnull.imp.client.gui.components.VolumeWidget;
import dev.felnull.imp.client.gui.screen.BoomboxScreen;
import dev.felnull.imp.data.BoomboxData;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class PlayBackBaseBMonitor extends BoomboxMonitor {
    protected static final ResourceLocation PLAYING_BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox/monitor/playing.png");
    protected static final ResourceLocation PLAYING_IMAGE_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox/monitor/playing_image.png");
    private VolumeWidget volumeWidget;
    private PlayBackControlWidget playBackControlWidget;

    public PlayBackBaseBMonitor(BoomboxData.MonitorType monitorType, BoomboxScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);

        this.volumeWidget = this.addRenderWidget(new VolumeWidget(getStartX() + 168, getStartY() + 14, new IntValue() {
            @Override
            public void accept(int value) {
                setVolume(value);
            }

            @Override
            public int getAsInt() {
                return getScreen().getVolume();
            }
        }, () -> getScreen().isMute(), null));

        this.volumeWidget.visible = canPlay();

        this.playBackControlWidget = this.addRenderWidget(new PlayBackControlWidget(getStartX() + (isShortProgressBar() ? 38 : 2), getStartY() + 25, () -> getScreen().isPlaying() ? PlayBackControlWidget.StateType.STOP : PlayBackControlWidget.StateType.PLAYING, n -> {
            switch (n) {
                case PLAYING -> getScreen().insPlaying(true);
                case STOP -> getScreen().insPlaying(false);
                case PAUSE -> getScreen().insPause();
            }
        }));

        this.playBackControlWidget.visible = canPlay();
    }

    @Override
    public void tick() {
        super.tick();
        this.volumeWidget.visible = canPlay();

        this.playBackControlWidget.visible = canPlay();
        this.playBackControlWidget.x = getStartX() + (isShortProgressBar() ? 38 : 2);
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        if (!canPlay()) return;
        OERenderUtil.drawTexture(PLAYING_BG_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
        if (!getPlayBackImage().isEmpty())
            OERenderUtil.drawTexture(PLAYING_IMAGE_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);

        int sx = 2;
        if (!getPlayBackImage().isEmpty()) {
            getPlayImageRenderer().draw(getPlayBackImage(), poseStack, getStartX() + 1, getStartY() + 1, height - 2);
            sx += height - 2;
        }
        drawSmartCenterText(poseStack, new TextComponent(OERenderUtil.getWidthString(getPlayBackName(), width - sx - 2, "...")), getStartX() + sx + (width - sx - 2f) / 2f, getStartY() + 3);
    }

    @Override
    public void renderAppearance(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight, BoomboxData data) {
        super.renderAppearance(poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight, data);
        if (!canPlay(data)) return;
        OERenderUtil.renderTextureSprite(PLAYING_BG_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
        if (!getPlayBackImage(data).isEmpty())
            OERenderUtil.renderTextureSprite(PLAYING_IMAGE_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 3, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);

        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;

        int sx = 2;
        if (!getPlayBackImage(data).isEmpty()) {
            getPlayImageRenderer().renderSprite(getPlayBackImage(data), poseStack, multiBufferSource, 1 * onPxW, monitorHeight - (1 + height - 2) * onPxH, OERenderUtil.MIN_BREADTH * 4, (height - 3) * onPxH, i, j);
            sx += height - 2;
        }
        renderSmartCenterTextSprite(poseStack, multiBufferSource, new TextComponent(OERenderUtil.getWidthString(getPlayBackName(data), width - sx - 2, "...")), sx + (width - sx - 2f) / 2f, 4, OERenderUtil.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, i);

        renderVolumeSprite(poseStack, multiBufferSource, 168, 14, OERenderUtil.MIN_BREADTH * 2, i, j, onPxW, onPxH, monitorHeight, data.getVolume(), data.isMute());
        renderPlayBackControl(poseStack, multiBufferSource, isShortProgressBar(data) ? 38 : 2, 25, OERenderUtil.MIN_BREADTH * 2, i, j, onPxW, onPxH, monitorHeight, data.isPlaying() ? PlayBackControlWidget.StateType.STOP : PlayBackControlWidget.StateType.PLAYING);
    }

    private void setVolume(int volume) {
        getScreen().insVolume(volume);
    }

    @NotNull
    protected ImageInfo getPlayBackImage() {
        return getPlayBackImage(getScreen().getBoomBoxData());
    }

    @NotNull
    abstract protected ImageInfo getPlayBackImage(BoomboxData data);

    @NotNull
    protected String getPlayBackName() {
        return getPlayBackName(getScreen().getBoomBoxData());
    }

    @NotNull
    abstract protected String getPlayBackName(BoomboxData data);

    @NotNull
    protected String getPlayBackAuthor() {
        return getPlayBackAuthor(getScreen().getBoomBoxData());
    }

    @NotNull
    abstract protected String getPlayBackAuthor(BoomboxData data);

    @NotNull
    protected MusicSource getPlayBackSource() {
        return getPlayBackSource(getScreen().getBoomBoxData());
    }

    @NotNull
    abstract protected MusicSource getPlayBackSource(BoomboxData data);

    protected boolean canPlay() {
        return canPlay(getScreen().getBoomBoxData());
    }

    protected boolean canPlay(BoomboxData data) {
        return true;
    }

    protected boolean isShortProgressBar(BoomboxData data) {
        return !getPlayBackImage(data).isEmpty();
    }

    protected boolean isShortProgressBar() {
        return isShortProgressBar(getScreen().getBoomBoxData());
    }

    protected boolean isShortTipProgressBar() {
        return false;
    }
}
