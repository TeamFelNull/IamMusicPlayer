package dev.felnull.imp.client.gui.screen.monitor.cassette_deck;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.value.BooleanValue;
import dev.architectury.utils.value.FloatValue;
import dev.architectury.utils.value.IntValue;
import dev.felnull.fnjl.util.FNStringUtil;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.CassetteDeckBlockEntity;
import dev.felnull.imp.client.gui.components.*;
import dev.felnull.imp.client.gui.screen.CassetteDeckScreen;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.item.CassetteTapeItem;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.util.IMPItemUtil;
import dev.felnull.otyacraftengine.client.util.OEClientUtils;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PlaybackCDMonitor extends CassetteDeckMonitor {
    protected static final ResourceLocation PLAYBACK_BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/cassette_deck/monitor/playback.png");
    protected static final ResourceLocation PLAYBACK_IMAGE_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/cassette_deck/monitor/playback_image.png");
    private static final Component NO_CASSETTE_TAPE_TEXT = Component.translatable("imp.text.noCassetteTape");
    private static final Component NO_MUSIC_CASSETTE_TAPE_TEXT = Component.translatable("imp.text.noMusicCassetteTape");
    private static final Component LOADING_MUSIC_TEXT = Component.translatable("imp.text.musicLoading");
    private SmartButton backButton;
    private VolumeWidget volumeWidget;
    private PlayBackControlWidget playBackControlWidget;
    private LoopControlWidget loopControlWidget;
    private PlayProgressWidget playProgressWidget;

    public PlaybackCDMonitor(CassetteDeckBlockEntity.MonitorType monitorType, CassetteDeckScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);

        this.backButton = this.addRenderWidget(new SmartButton(getStartX() + 1, getStartY() + 44, 14, 11, Component.translatable("gui.back"), n -> insMonitor(CassetteDeckBlockEntity.MonitorType.MENU)));
        this.backButton.setHideText(true);
        this.backButton.setIcon(MusicManagerMonitor.WIDGETS_TEXTURE, 11, 123, 8, 8);

        this.volumeWidget = this.addRenderWidget(new VolumeWidget(getStartX() + 168, getStartY() + 16, new IntValue() {
            @Override
            public void accept(int value) {
                getScreen().insVolume(value);
            }

            @Override
            public int getAsInt() {
                return getVolume();
            }
        }, () -> getScreen().isMute(), n -> getScreen().insMute(n)));
        this.volumeWidget.visible = isPlayBack();

        this.playBackControlWidget = this.addRenderWidget(new PlayBackControlWidget(getStartX() + (isShortProgressBar() ? 45 : 2), getStartY() + 30, () -> getScreen().isPlaying() ? PlayBackControlWidget.StateType.STOP : PlayBackControlWidget.StateType.PLAYING, n -> {
            switch (n) {
                case PLAYING -> getScreen().insPlaying(true);
                case STOP -> getScreen().insPlaying(false);
                case PAUSE -> getScreen().insPause();
            }
        }));

        this.playBackControlWidget.visible = isPlayBack();

        this.loopControlWidget = this.addRenderWidget(new LoopControlWidget(getStartX() + 189, getStartY() + 31, new BooleanValue() {
            @Override
            public void accept(boolean t) {
                getScreen().insLoop(t);
            }

            @Override
            public boolean getAsBoolean() {
                return getScreen().isLoop();
            }
        }));

        this.loopControlWidget.visible = isPlayBack();

        this.playProgressWidget = this.addRenderWidget(new PlayProgressWidget(getStartX() + (isShortProgressBar() ? 55 : 12), getStartY() + 33, isShortProgressBar() ? 133 : 176, new FloatValue() {
            @Override
            public float getAsFloat() {
                if (!getCassetteTape().isEmpty() && IMPItemUtil.isCassetteTape(getCassetteTape())) {
                    Music music = CassetteTapeItem.getMusic(getCassetteTape());
                    if (music != null)
                        return (float) getScreen().getPosition() / (float) music.getSource().getDuration();
                }
                return 0;
            }

            @Override
            public void accept(float t) {
                if (!getCassetteTape().isEmpty() && IMPItemUtil.isCassetteTape(getCassetteTape())) {
                    Music music = CassetteTapeItem.getMusic(getCassetteTape());
                    if (music != null) {
                        long vl = (long) ((float) music.getSource().getDuration() * t);
                        getScreen().insPositionAndRestart(vl);
                    }
                }
            }
        }));

        this.playProgressWidget.visible = isPlayBack();
    }

    @Override
    public void renderAppearance(CassetteDeckBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        renderSmartButtonSprite(poseStack, multiBufferSource, 1, 44, OERenderUtils.MIN_BREADTH * 2f, 14, 11, i, j, onPxW, onPxH, monitorHeight, MusicManagerMonitor.WIDGETS_TEXTURE, 11, 123, 8, 8, 256, 256);

        if (!getCassetteTape(blockEntity).isEmpty() && IMPItemUtil.isCassetteTape(getCassetteTape(blockEntity))) {
            Music music = CassetteTapeItem.getMusic(getCassetteTape(blockEntity));
            if (music != null) {
                OERenderUtils.renderTextureSprite(PLAYBACK_BG_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtils.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
                if (!music.getImage().isEmpty())
                    OERenderUtils.renderTextureSprite(PLAYBACK_IMAGE_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtils.MIN_BREADTH * 3, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);

                int sx = 2;
                if (!music.getImage().isEmpty()) {
                    renderPlayListImage(poseStack, multiBufferSource, music.getImage(), 1, 1, OERenderUtils.MIN_BREADTH * 4, height - 3 - 13, i, j, onPxW, onPxH, monitorHeight);
                    sx += height - 2 - 13;
                }
                renderSmartCenterTextSprite(poseStack, multiBufferSource, Component.literal(OEClientUtils.getWidthOmitText(music.getName(), width - sx - 2, "...")), sx + (width - sx - 2f) / 2f, 3, OERenderUtils.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, i);

                var ptx = LOADING_MUSIC_TEXT;
                if (!blockEntity.isLoadingMusic())
                    ptx = Component.literal(FNStringUtil.getTimeProgress(blockEntity.getPosition(), music.getSource().getDuration()));

                renderSmartTextSpriteColorSprite(poseStack, multiBufferSource, ptx, 45f - (isShortProgressBar(blockEntity) ? 0 : 43f), 17f, OERenderUtils.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, 0XFF115D0E, i);

                renderVolumeSprite(poseStack, multiBufferSource, 168, 16, OERenderUtils.MIN_BREADTH * 2, i, j, onPxW, onPxH, monitorHeight, getVolume(blockEntity), isMute(blockEntity));
                renderPlayBackControl(poseStack, multiBufferSource, isShortProgressBar(blockEntity) ? 45 : 2, 30, OERenderUtils.MIN_BREADTH * 2, i, j, onPxW, onPxH, monitorHeight, blockEntity.isPlaying() ? PlayBackControlWidget.StateType.STOP : PlayBackControlWidget.StateType.PLAYING);
                renderLoopControl(poseStack, multiBufferSource, 189, 31, OERenderUtils.MIN_BREADTH * 2, i, j, onPxW, onPxH, monitorHeight, blockEntity.isLoop());

                renderPlayProgress(poseStack, multiBufferSource, isShortProgressBar(blockEntity) ? 55 : 12, 33, OERenderUtils.MIN_BREADTH * 2, i, j, onPxW, onPxH, monitorHeight, isShortProgressBar(blockEntity) ? 133 : 176, (float) blockEntity.getPosition() / (float) music.getSource().getDuration());
            } else {
                renderSmartCenterTextSprite(poseStack, multiBufferSource, NO_MUSIC_CASSETTE_TAPE_TEXT, ((float) width / 2f), (((float) height - 10f) / 2f), OERenderUtils.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, i);
            }
        } else {
            renderSmartCenterTextSprite(poseStack, multiBufferSource, NO_CASSETTE_TAPE_TEXT, ((float) width / 2f), (((float) height - 10f) / 2f), OERenderUtils.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, i);
        }

    }

    @Override
    public void render(GuiGraphics guiGraphics, float f, int mouseX, int mouseY) {
        super.render(guiGraphics, f, mouseX, mouseY);

        if (!getCassetteTape().isEmpty() && IMPItemUtil.isCassetteTape(getCassetteTape())) {
            Music music = CassetteTapeItem.getMusic(getCassetteTape());
            if (music != null) {
                OERenderUtils.drawTexture(PLAYBACK_BG_TEXTURE, guiGraphics.pose(), getStartX(), getStartY(), 0f, 0f, width, height, width, height);
                if (!music.getImage().isEmpty())
                    OERenderUtils.drawTexture(PLAYBACK_IMAGE_TEXTURE, guiGraphics.pose(), getStartX(), getStartY(), 0f, 0f, width, height, width, height);
                int sx = 2;
                if (!music.getImage().isEmpty()) {
                    getPlayImageRenderer().draw(music.getImage(), guiGraphics.pose(), getStartX() + 1, getStartY() + 1, height - 2 - 13);
                    sx += height - 2 - 13;
                }
                drawSmartCenterText(guiGraphics, Component.literal(OEClientUtils.getWidthOmitText(music.getName(), width - sx - 2, "...")), getStartX() + sx + (width - sx - 2f) / 2f, getStartY() + 3);
                var ptx = LOADING_MUSIC_TEXT;
                if (!getScreen().isLoading())
                    ptx = Component.literal(FNStringUtil.getTimeProgress(getScreen().getPosition(), music.getSource().getDuration()));
                drawSmartText(guiGraphics, ptx, getStartX() + 45 - (isShortProgressBar() ? 0 : 43), getStartY() + 17, 0XFF115D0E);
            } else {
                drawSmartCenterText(guiGraphics, NO_MUSIC_CASSETTE_TAPE_TEXT, getStartX() + width / 2f, getStartY() + (height - 10f) / 2f);
            }
        } else {
            drawSmartCenterText(guiGraphics, NO_CASSETTE_TAPE_TEXT, getStartX() + width / 2f, getStartY() + (height - 10f) / 2f);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.volumeWidget.visible = isPlayBack();
        this.playBackControlWidget.visible = isPlayBack();
        this.playBackControlWidget.setX(getStartX() + (isShortProgressBar() ? 45 : 2));
        this.loopControlWidget.visible = isPlayBack();
        this.playProgressWidget.visible = isPlayBack();
        this.playProgressWidget.setWidth(isShortProgressBar() ? 133 : 176);
        this.playProgressWidget.setX(getStartX() + (isShortProgressBar() ? 55 : 12));
    }

    private boolean isShortProgressBar(CassetteDeckBlockEntity blockEntity) {
        if (!getCassetteTape(blockEntity).isEmpty() && IMPItemUtil.isCassetteTape(getCassetteTape(blockEntity))) {
            var m = CassetteTapeItem.getMusic(getCassetteTape(blockEntity));
            if (m != null)
                return !m.getImage().isEmpty();
        }
        return false;
    }

    private boolean isShortProgressBar() {
        if (!getCassetteTape().isEmpty() && IMPItemUtil.isCassetteTape(getCassetteTape())) {
            var m = CassetteTapeItem.getMusic(getCassetteTape());
            if (m != null)
                return !m.getImage().isEmpty();
        }
        return false;
    }

    private boolean isPlayBack() {
        if (!getCassetteTape().isEmpty() && IMPItemUtil.isCassetteTape(getCassetteTape()))
            return CassetteTapeItem.getMusic(getCassetteTape()) != null;
        return false;
    }
}
