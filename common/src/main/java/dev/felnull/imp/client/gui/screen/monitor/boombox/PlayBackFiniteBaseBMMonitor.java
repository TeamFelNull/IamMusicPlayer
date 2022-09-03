package dev.felnull.imp.client.gui.screen.monitor.boombox;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.value.BooleanValue;
import dev.architectury.utils.value.FloatValue;
import dev.felnull.fnjl.util.FNStringUtil;
import dev.felnull.imp.block.BoomboxData;
import dev.felnull.imp.client.gui.components.LoopControlWidget;
import dev.felnull.imp.client.gui.components.PlayProgressWidget;
import dev.felnull.imp.client.gui.screen.BoomboxScreen;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

public abstract class PlayBackFiniteBaseBMMonitor extends PlayBackBaseBMonitor {
    private static final Component LOADING_MUSIC_TEXT = Component.translatable("imp.text.musicLoading");
    private LoopControlWidget loopControlWidget;
    private PlayProgressWidget playProgressWidget;

    public PlayBackFiniteBaseBMMonitor(BoomboxData.MonitorType monitorType, BoomboxScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.loopControlWidget = this.addRenderWidget(new LoopControlWidget(getStartX() + (isShortTipProgressBar() ? 156 : 189), getStartY() + (isShortTipProgressBar() ? 15 : 26), new BooleanValue() {
            @Override
            public void accept(boolean t) {
                setLoop(t);
            }

            @Override
            public boolean getAsBoolean() {
                return isLoop();
            }
        }));

        this.loopControlWidget.visible = canPlay();

        this.playProgressWidget = this.addRenderWidget(new PlayProgressWidget(getStartX() + (isShortProgressBar() ? 48 : 12), getStartY() + 28, getProgressBarWidth(getScreen().getBoomBoxData()), new FloatValue() {
            @Override
            public float getAsFloat() {
                var s = getPlayBackSource();
                if (!s.isEmpty())
                    return (float) getScreen().getMusicPosition() / (float) s.getDuration();
                return 0;
            }

            @Override
            public void accept(float t) {
                var s = getPlayBackSource();
                if (!s.isEmpty()) {
                    long vl = (long) ((float) s.getDuration() * t);
                    getScreen().insPositionAndRestart(vl);
                }
            }
        }));

        this.playProgressWidget.visible = canPlay();


    }

    @Override
    public void tick() {
        super.tick();
        this.loopControlWidget.visible = canPlay();
        this.loopControlWidget.x = getStartX() + (isShortTipProgressBar() ? 156 : 189);
        this.loopControlWidget.y = getStartY() + (isShortTipProgressBar() ? 15 : 26);
        this.playProgressWidget.visible = canPlay();
        this.playProgressWidget.setWidth(getProgressBarWidth(getScreen().getBoomBoxData()));
        this.playProgressWidget.x = getStartX() + (isShortProgressBar() ? 48 : 12);
    }

    private int getProgressBarWidth(BoomboxData data) {
        return (isShortProgressBar(data) ? 133 : 176) - (isShortTipProgressBar() ? (isShortProgressBar(data) ? 2 : 10) : 0);
    }

    private void setLoop(boolean loop) {
        getScreen().insLoop(loop);
    }

    private boolean isLoop() {
        return getScreen().isLoop();
    }

    private void setPause() {
        getScreen().insPause();
    }

    private void setPlaying(boolean playing) {
        getScreen().insPlaying(playing);
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        if (!canPlay()) return;
        var ptx = LOADING_MUSIC_TEXT;
        if (!getScreen().isMusicLoading())
            ptx = Component.translatable(FNStringUtil.getTimeProgress(getScreen().getMusicPosition(), getPlayBackSource().getDuration()));
        drawSmartText(poseStack, ptx, getStartX() + 38 - (isShortProgressBar() ? 0 : 36), getStartY() + 15, 0XFF115D0E);
    }

    @Override
    public void renderAppearance(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight, BoomboxData data) {
        super.renderAppearance(poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight, data);
        if (!canPlay(data)) return;
        var ptx = LOADING_MUSIC_TEXT;
        if (!data.isLoadingMusic())
            ptx = Component.translatable(FNStringUtil.getTimeProgress(data.getMusicPosition(), getPlayBackSource(data).getDuration()));

        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;

        renderSmartTextSpriteColorSprite(poseStack, multiBufferSource, ptx, 38f - (isShortProgressBar(data) ? 0 : 36f), 17f, OERenderUtils.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, 0XFF115D0E, i);
        renderLoopControl(poseStack, multiBufferSource, isShortTipProgressBar() ? 156 : 189, isShortTipProgressBar() ? 15 : 26, OERenderUtils.MIN_BREADTH * 2, i, j, onPxW, onPxH, monitorHeight, data.isLoop());
        renderPlayProgress(poseStack, multiBufferSource, isShortProgressBar(data) ? 48 : 12, 28, OERenderUtils.MIN_BREADTH * 2, i, j, onPxW, onPxH, monitorHeight, getProgressBarWidth(data), (float) data.getMusicPosition() / (float) getPlayBackSource(data).getDuration());
    }
}
