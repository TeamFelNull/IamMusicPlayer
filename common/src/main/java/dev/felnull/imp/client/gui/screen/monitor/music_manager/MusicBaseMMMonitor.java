package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.fnjl.util.FNStringUtil;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.PlaybackProgressBar;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.client.music.media.IMPMusicMedias;
import dev.felnull.imp.client.music.media.MusicMedia;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.otyacraftengine.client.util.OEClientUtils;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MusicBaseMMMonitor extends ImageNameBaseMMMonitor {
    private static final ResourceLocation ADD_MUSIC_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/add_music.png");
    private static final ResourceLocation SHOW_MUSIC_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/show_music.png");
    private static final Component PLAYBACK_CONTROL_TEXT = Component.translatable("imp.button.playbackControl");
    protected static final Component MUSIC_SOURCE_TEXT = Component.translatable("imp.text.musicSource");
    private static final Component PLAYBACK_NON_PROGRESS_TEXT = Component.translatable("--:--/--:--");
    private static final Component PLAYBACK_LOADING_PROGRESS_TEXT = Component.translatable("imp.text.playbackLoading");
    private SmartButton playControlButton;
    protected int playBackX;
    protected int playBackY;
    protected boolean isLoaderSelect;
    private Component AUTHOR_TEXT;

    public MusicBaseMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
        this.playBackX = 189;
        this.playBackY = 40;
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.playControlButton = this.addRenderWidget(new SmartButton(getStartX() + this.playBackX, getStartY() + this.playBackY, 20, 20, PLAYBACK_CONTROL_TEXT, n -> {
            if (getScreen().isMusicPlaying()) {
                getScreen().stopMusic();
            } else {
                if (!getMusicSource().isEmpty()) {
                    getScreen().playMusic(getMusicSource(), 0);
                }
            }
        }));
        this.playControlButton.setHideText(true);
        this.playControlButton.setIcon(WIDGETS_TEXTURE, 0, 123, 11, 11);
        this.playControlButton.active = !getMusicSource().isEmpty();

        this.addRenderWidget(new PlaybackProgressBar(getStartX() + this.playBackX + 22, getStartY() + this.playBackY + 14, Component.translatable("imp.progressBar.playbackControl"), () -> {
            if (getScreen().isMusicPlaying())
                return getScreen().getMusicPlayer().getCurrentPositionProgress();
            return 0f;
        }, n -> {
            if (getScreen().isMusicPlaying()) {
                var ms = getScreen().getMusicPlayer().getSource();
                getScreen().playMusic(ms, (long) ((float) ms.getDuration() * n));
            }
        }));

        if (!getMusicAuthor().isEmpty())
            setMusicAuthor(getMusicAuthor());
    }

    @Override
    public void depose() {
        super.depose();
        AUTHOR_TEXT = null;
    }

    @Override
    public void tick() {
        super.tick();
        this.playControlButton.active = !getMusicSource().isEmpty();

        if (getScreen().isMusicPlaying() || getScreen().isMusicLoading()) {
            this.playControlButton.setIcon(WIDGETS_TEXTURE, 0, 134, 11, 11);
        } else {
            this.playControlButton.setIcon(WIDGETS_TEXTURE, 0, 123, 11, 11);
        }
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtils.drawTexture(isLoaderSelect ? ADD_MUSIC_TEXTURE : SHOW_MUSIC_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
        drawSmartText(poseStack, MUSIC_SOURCE_TEXT, getStartX() + 189, getStartY() + 13);

        OERenderUtils.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, getStartX() + playBackX, getStartY() + playBackY, 68, 90, 177, 20);

        Component pt;
        if (getScreen().isMusicPlaying()) {
            var mp = getScreen().getMusicPlayer();
            pt = Component.literal(FNStringUtil.getTimeProgress(mp.getCurrentPosition(), mp.getSource().getDuration()));
        } else if (getScreen().isMusicLoading()) {
            pt = PLAYBACK_LOADING_PROGRESS_TEXT;
        } else if (!getMusicSource().isEmpty()) {
            pt = Component.literal(FNStringUtil.getTimeProgress(0, getMusicSource().getDuration()));
        } else {
            pt = PLAYBACK_NON_PROGRESS_TEXT;
        }
        drawSmartText(poseStack, pt, getStartX() + playBackX + 22, getStartY() + playBackY + 4);


        if (AUTHOR_TEXT != null)
            drawSmartText(poseStack, AUTHOR_TEXT, getStartX() + this.playBackX, getStartY() + this.playBackY + 25);

        if (!isLoaderSelect) {
            drawSmartButtonBox(poseStack, getStartX() + 189, getStartY() + 22, 176, 15, false);
            var lt = getRawMusicLoaderType();
            if (lt != null) {
                int sx = 0;
                var ic = lt.getIcon();
                if (ic != null) {
                    OERenderUtils.drawTexture(ic, poseStack, getStartX() + 190, getStartY() + 23, 0, 0, 13, 13, 13, 13);
                    sx += 13;
                }
                drawSmartText(poseStack, lt.getMediaName(), getStartX() + 190 + sx + 2, getStartY() + 21f + (15f - 6.5f) / 2f);
            }
        }
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;

        OERenderUtils.renderTextureSprite(isLoaderSelect ? ADD_MUSIC_TEXTURE : SHOW_MUSIC_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtils.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
        renderSmartTextSprite(poseStack, multiBufferSource, MUSIC_SOURCE_TEXT, 189, 13, OERenderUtils.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);

        renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, playBackX, playBackY, OERenderUtils.MIN_BREADTH * 3, 177, 20, 68, 90, 177, 20, 256, 256, i, j, onPxW, onPxH, monitorHeight);

        Component pt;
        if (!getMusicSource(blockEntity).isEmpty()) {
            pt = Component.literal(FNStringUtil.getTimeProgress(0, getMusicSource(blockEntity).getDuration()));
        } else {
            pt = PLAYBACK_NON_PROGRESS_TEXT;
        }
        renderSmartTextSprite(poseStack, multiBufferSource, pt, playBackX + 22, playBackY + 4, OERenderUtils.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);

        renderSmartButtonSprite(poseStack, multiBufferSource, playBackX, playBackY, OERenderUtils.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, WIDGETS_TEXTURE, 0, 123, 11, 11, 256, 256, getMusicSource(blockEntity).isEmpty());

        OERenderUtils.renderTextureSprite(WIDGETS_TEXTURE, poseStack, multiBufferSource, onPxW * (this.playBackX + 22), monitorHeight - onPxH * (this.playBackY + 14 + 3), OERenderUtils.MIN_BREADTH * 4, 0, 0, 0, onPxW * 153f, onPxH * 3f, 52, 54, 153, 3, 256, 256, i, j);

        if (!getMusicAuthor(blockEntity).isEmpty()) {
            int le = 176 - mc.font.width(Component.translatable("imp.text.musicAuthor", ""));
            var aut = OEClientUtils.getWidthOmitText(getMusicAuthor(blockEntity), le, "...");
            renderSmartTextSprite(poseStack, multiBufferSource, Component.translatable("imp.text.musicAuthor", aut), this.playBackX, this.playBackY + 25, OERenderUtils.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
        }

        if (!isLoaderSelect) {
            renderSmartButtonBoxSprite(poseStack, multiBufferSource, 189, 22, OERenderUtils.MIN_BREADTH * 3, 176, 15, i, j, onPxW, onPxH, monitorHeight);
            var lt = getRawMusicLoaderType(blockEntity);
            if (lt != null) {
                int sx = 0;
                var ic = lt.getIcon();
                if (ic != null) {
                    renderTextureSprite(ic, poseStack, multiBufferSource, 190, 23, OERenderUtils.MIN_BREADTH * 5, 13, 13, 0, 0, 13, 13, 13, 13, i, j, onPxW, onPxH, monitorHeight);
                    sx += 13;
                }
                renderSmartTextSprite(poseStack, multiBufferSource, lt.getMediaName(), 190 + sx + 2, 21f + (15f - 6.5f) / 2f + 2f, OERenderUtils.MIN_BREADTH * 5, onPxW, onPxH, monitorHeight, i);
            }
        }

    }

    protected void setMusicAuthor(String author) {
        if (author.isEmpty()) {
            AUTHOR_TEXT = null;
        } else {
            int le = 176 - mc.font.width(Component.translatable("imp.text.musicAuthor", ""));
            var aut = OEClientUtils.getWidthOmitText(author, le, "...");
            AUTHOR_TEXT = Component.translatable("imp.text.musicAuthor", aut);
        }
    }

    @NotNull
    protected String getMusicAuthor() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
            return getMusicAuthor(musicManagerBlock);
        return "";
    }

    @NotNull
    protected String getMusicAuthor(@NotNull MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getMusicAuthor(mc.player);
    }

    @NotNull
    protected String getMusicSourceName() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity blockEntity)
            return getMusicSourceName(blockEntity);
        return "";
    }

    @NotNull
    protected String getMusicSourceName(@NotNull MusicManagerBlockEntity blockEntity) {
        return blockEntity.getMusicSourceName(mc.player);
    }

    @NotNull
    protected MusicSource getMusicSource() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity blockEntity)
            return getMusicSource(blockEntity);
        return MusicSource.EMPTY;
    }

    @NotNull
    protected MusicSource getMusicSource(@NotNull MusicManagerBlockEntity blockEntity) {
        return blockEntity.getMusicSource(mc.player);
    }

    @Nullable
    protected MusicMedia getRawMusicLoaderType(@NotNull MusicManagerBlockEntity musicManagerBlockEntity) {
        return IMPMusicMedias.getAllMedia().get(getMusicLoaderType(musicManagerBlockEntity));
    }

    @Nullable
    protected MusicMedia getRawMusicLoaderType() {
        return IMPMusicMedias.getAllMedia().get(getMusicLoaderType());
    }

    @NotNull
    protected String getMusicLoaderType() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity blockEntity)
            return getMusicLoaderType(blockEntity);
        return "auto";
    }

    @NotNull
    protected String getMusicLoaderType(@NotNull MusicManagerBlockEntity blockEntity) {
        return blockEntity.getMusicLoaderType(mc.player).isEmpty() ? "auto" : blockEntity.getMusicLoaderType(mc.player);
    }
}
