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
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class PlaybackCDMonitor extends CassetteDeckMonitor {
    protected static final ResourceLocation PLAYBACK_BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/cassette_deck/monitor/playback.png");
    protected static final ResourceLocation PLAYBACK_IMAGE_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/cassette_deck/monitor/playback_image.png");
    private static final Component NO_CASSETTE_TAPE_TEXT = new TranslatableComponent("imp.text.noCassetteTape");
    private static final Component NO_MUSIC_CASSETTE_TAPE_TEXT = new TranslatableComponent("imp.text.noMusicCassetteTape");
    private static final Component LOADING_MUSIC_TEXT = new TranslatableComponent("imp.text.musicLoading");
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

        this.backButton = this.addRenderWidget(new SmartButton(getStartX() + 1, getStartY() + 44, 14, 11, new TranslatableComponent("gui.back"), n -> insMonitor(CassetteDeckBlockEntity.MonitorType.MENU)));
        this.backButton.setHideText(true);
        this.backButton.setIcon(MusicManagerMonitor.WIDGETS_TEXTURE, 11, 123, 8, 8);

        this.volumeWidget = this.addRenderWidget(new VolumeWidget(getStartX() + 168, getStartY() + 14, new IntValue() {
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

        this.playBackControlWidget = this.addRenderWidget(new PlayBackControlWidget(getStartX() + (isShortProgressBar() ? 45 : 2), getStartY() + 32, () -> {
            if (getScreen().isPlaying())
                return PlayBackControlWidget.StateType.PLAYING;
            return getScreen().getPosition() == 0 ? PlayBackControlWidget.StateType.STOP : PlayBackControlWidget.StateType.PAUSE;
        }, n -> {
            switch (n) {
                case PLAYING -> getScreen().insPlaying(false);
                case STOP, PAUSE -> getScreen().insPlaying(true);
            }
        }));

        this.playBackControlWidget.visible = isPlayBack();

        this.loopControlWidget = this.addRenderWidget(new LoopControlWidget(getStartX() + 189, getStartY() + 33, new BooleanValue() {
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

        this.playProgressWidget = this.addRenderWidget(new PlayProgressWidget(getStartX() + (isShortProgressBar() ? 55 : 12), getStartY() + 35, isShortProgressBar() ? 133 : 176, new FloatValue() {
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
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);

        if (!getCassetteTape().isEmpty() && IMPItemUtil.isCassetteTape(getCassetteTape())) {
            Music music = CassetteTapeItem.getMusic(getCassetteTape());
            if (music != null) {
                OERenderUtil.drawTexture(PLAYBACK_BG_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
                if (!music.getImage().isEmpty())
                    OERenderUtil.drawTexture(PLAYBACK_IMAGE_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
                int sx = 2;
                if (!music.getImage().isEmpty()) {
                    getPlayImageRenderer().draw(music.getImage(), poseStack, getStartX() + 1, getStartY() + 1, height - 2 - 13);
                    sx += height - 2 - 13;
                }
                drawSmartCenterText(poseStack, new TextComponent(OERenderUtil.getWidthString(music.getName(), width - sx - 2, "...")), getStartX() + sx + (width - sx - 2f) / 2f, getStartY() + 3);
                var ptx = LOADING_MUSIC_TEXT;
                if (!getScreen().isLoading())
                    ptx = new TextComponent(FNStringUtil.getTimeProgress(getScreen().getPosition(), music.getSource().getDuration()));
                drawSmartText(poseStack, ptx, getStartX() + 45 - (isShortProgressBar() ? 0 : 43), getStartY() + 15, 0XFF115D0E);
            } else {
                drawSmartCenterText(poseStack, NO_MUSIC_CASSETTE_TAPE_TEXT, getStartX() + width / 2f, getStartY() + (height - 10f) / 2f);
            }
        } else {
            drawSmartCenterText(poseStack, NO_CASSETTE_TAPE_TEXT, getStartX() + width / 2f, getStartY() + (height - 10f) / 2f);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.volumeWidget.visible = isPlayBack();
        this.playBackControlWidget.visible = isPlayBack();
        this.playBackControlWidget.x = getStartX() + (isShortProgressBar() ? 45 : 2);
        this.loopControlWidget.visible = isPlayBack();
        this.playProgressWidget.visible = isPlayBack();
        this.playProgressWidget.setWidth(isShortProgressBar() ? 133 : 176);
        this.playProgressWidget.x = getStartX() + (isShortProgressBar() ? 55 : 12);
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
