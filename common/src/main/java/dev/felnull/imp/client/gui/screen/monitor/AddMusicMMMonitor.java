package dev.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.fnjl.util.FNStringUtil;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.PlaybackProgressBar;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class AddMusicMMMonitor extends CreateBaseMMMonitor {
    private static final ResourceLocation ADD_MUSIC_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/add_music.png");
    private static final Component PLAYBACK_CONTROL_TEXT = new TranslatableComponent("imp.button.playbackControl");
    private static final Component PLAYBACK_TEXT = new TranslatableComponent("imp.text.playback");
    private static final Component PLAYBACK_NON_PROGRESS_TEXT = new TextComponent("--:--/--:--");
    private static final Component PLAYBACK_LOADING_PROGRESS_TEXT = new TranslatableComponent("imp.text.playbackLoading");
    private SmartButton playControlButton;

    public AddMusicMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);

        this.playControlButton = this.addRenderWidget(new SmartButton(getStartX() + 5, getStartY() + 140, 20, 20, PLAYBACK_CONTROL_TEXT, n -> {
            if (getScreen().isMusicPlaying()) {
                getScreen().stopMusic();
            } else {
                if (getMusicSource() != null) {
                    getScreen().playMusic(getMusicSource(), 0);
                }
            }
        }));
        this.playControlButton.setHideText(true);
        this.playControlButton.setIcon(WIDGETS_TEXTURE, 0, 123, 11, 11);

        this.addRenderWidget(new PlaybackProgressBar(getStartX() + 27, getStartY() + 154, new TranslatableComponent("imp.progressBar.playbackControl"), () -> {
            if (getScreen().isMusicPlaying())
                return getScreen().getMusicPlayer().getPositionProgress();
            return 0f;
        }, n -> {
            if (getScreen().isMusicPlaying()) {
                var ms = getScreen().getMusicPlayer().getMusicSource();
                getScreen().playMusic(ms, (long) ((float) ms.getDuration() * n));
            }
        }));
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(ADD_MUSIC_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);

        drawSmartText(poseStack, PLAYBACK_TEXT, getStartX() + 5, getStartY() + 131);

        Component pt;
        if (getScreen().isMusicPlaying()) {
            var mp = getScreen().getMusicPlayer();
            pt = new TextComponent(FNStringUtil.getTimeProgress(mp.getPosition(), mp.getMusicSource().getDuration()));
        } else if (getScreen().isMusicLoading()) {
            pt = PLAYBACK_LOADING_PROGRESS_TEXT;
        } else if (getMusicSource() != null) {
            pt = new TextComponent(FNStringUtil.getTimeProgress(0, getMusicSource().getDuration()));
        } else {
            pt = PLAYBACK_NON_PROGRESS_TEXT;
        }
        drawSmartText(poseStack, pt, getStartX() + 27, getStartY() + 144);
    }

    @Override
    public void create(ImageInfo imageInfo, String name) {
    }


    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.PLAY_LIST;
    }

    @Override
    public void tick() {
        super.tick();
        this.playControlButton.active = getMusicSource() != null;

        if (getScreen().isMusicPlaying() || getScreen().isMusicLoading()) {
            this.playControlButton.setIcon(WIDGETS_TEXTURE, 0, 134, 11, 11);
        } else {
            this.playControlButton.setIcon(WIDGETS_TEXTURE, 0, 123, 11, 11);
        }
    }

    public MusicSource getMusicSource() {
        return new MusicSource("youtube", "9aHQnDTd1y4", 178000);
    }
}
