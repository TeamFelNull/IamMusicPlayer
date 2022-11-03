package dev.felnull.imp.client.gui.screen.monitor.boombox;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.BoomboxScreen;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.client.lava.LavaPlayerManager;
import dev.felnull.imp.data.BoomboxData;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.util.FlagThread;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class RadioSelectBMonitor extends BoomboxMonitor {
    private static final ResourceLocation RADIO_SELECT_BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox/monitor/radio_select.png");
    private static final ResourceLocation RADIO_SELECT_IMAGE_BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox/monitor/radio_select_image.png");
    private static final Component RADIO_CHECKING_TEXT = new TranslatableComponent("imp.text.radioChecking");
    private static final Component ENTER_STREAM_TEXT = new TranslatableComponent("imp.text.enterStream");
    private EditBox radioUrlEditBox;
    private SmartButton radioStreamStartButton;
    private RadioCheckLoader radioCheckLoader;
    private String lastRadioUrl;

    public RadioSelectBMonitor(BoomboxData.MonitorType monitorType, BoomboxScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);

        this.radioUrlEditBox = addRenderWidget(new EditBox(IIMPSmartRender.mc.font, getStartX() + 2, getStartY() + height - 2 - 12, width - 4 - 35, 12, new TranslatableComponent("imp.editBox.radioUrl")));
        this.radioUrlEditBox.setMaxLength(300);
        this.radioUrlEditBox.setValue(getRadioUrl());
        this.radioUrlEditBox.setResponder(this::setRadioUrl);

        this.radioStreamStartButton = this.addRenderWidget(new SmartButton(getStartX() + width - 34 - 1, getStartY() + height - 1 - 14, 34, 14, new TranslatableComponent("imp.button.radioStreamStart"), n -> {
            setMonitor(BoomboxData.MonitorType.RADIO);
        }));
        this.radioStreamStartButton.setHideText(true);
        this.radioStreamStartButton.setIcon(MusicManagerMonitor.WIDGETS_TEXTURE, 19, 123, 17, 8);
        this.radioStreamStartButton.active = canRadioStreamStart();

        startRadioCheckLoad(getRadioUrl());
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(RADIO_SELECT_BG_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
        if (isRadioChecking()) {
            drawSmartText(poseStack, RADIO_CHECKING_TEXT, getStartX() + 2, getStartY() + (height - 1f - 14f - 6.5f) / 2f);
        } else if (!getRadioSource().isEmpty()) {
            float st = 2;
            if (!getRadioImage().isEmpty()) {
                OERenderUtil.drawTexture(RADIO_SELECT_IMAGE_BG_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
                getPlayImageRenderer().draw(getRadioImage(), poseStack, getStartX() + 1, getStartY() + 1, 20);
                st += 21;
            }
            if (!getScreen().getBoomBoxData().getRadioName().isEmpty())
                drawSmartText(poseStack, new TextComponent(OERenderUtil.getWidthString(getScreen().getBoomBoxData().getRadioName(), width - 3 - st, "...")), getStartX() + st, getStartY() + 2);
            if (!getScreen().getBoomBoxData().getRadioAuthor().isEmpty()) {
                var tx = new TranslatableComponent("imp.text.musicAuthor", "");
                drawSmartText(poseStack, new TranslatableComponent("imp.text.musicAuthor", OERenderUtil.getWidthString(getScreen().getBoomBoxData().getRadioAuthor(), width - 3 - st - mc.font.width(tx), "...")), getStartX() + st, getStartY() + 12);
            }
        } else {
            drawSmartText(poseStack, ENTER_STREAM_TEXT, getStartX() + 2, getStartY() + (height - 1f - 14f - 6.5f) / 2f);
        }
    }

    @Override
    public void renderAppearance(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight, BoomboxData data) {
        super.renderAppearance(poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight, data);
        OERenderUtil.renderTextureSprite(RADIO_SELECT_BG_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        if (!getRadioSource(data).isEmpty()) {
            float st = 3;
            if (!data.getRadioImage().isEmpty()) {
                OERenderUtil.renderTextureSprite(RADIO_SELECT_IMAGE_BG_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 3, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
                renderPlayListImage(poseStack, multiBufferSource, data.getRadioImage(), 1, 1, OERenderUtil.MIN_BREADTH * 5, 20, i, j, onPxW, onPxH, monitorHeight);
                st += 21;
            }
            if (!data.getRadioName().isEmpty())
                renderSmartTextSprite(poseStack, multiBufferSource, new TextComponent(OERenderUtil.getWidthString(data.getRadioName(), width - 3 - st, "...")), st, 4, OERenderUtil.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
            if (!data.getRadioAuthor().isEmpty()) {
                var tx = new TranslatableComponent("imp.text.musicAuthor", "");
                renderSmartTextSprite(poseStack, multiBufferSource, new TranslatableComponent("imp.text.musicAuthor", OERenderUtil.getWidthString(data.getRadioAuthor(), width - 3 - st - mc.font.width(tx), "...")), st, 14, OERenderUtil.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
            }
        } else {
            renderSmartTextSprite(poseStack, multiBufferSource, ENTER_STREAM_TEXT, 2, (height - 1f - 14f - 6.5f) / 2f, OERenderUtil.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
        }

        renderSmartButtonSprite(poseStack, multiBufferSource, width - 34 - 1, height - 1 - 14, OERenderUtil.MIN_BREADTH * 4, 34, 14, i, j, onPxW, onPxH, monitorHeight, MusicManagerMonitor.WIDGETS_TEXTURE, 19, 123, 17, 8, 256, 256, !canRadioStreamStart(data));
        renderSmartEditBoxSprite(poseStack, multiBufferSource, 1, height - 2 - 12, OERenderUtil.MIN_BREADTH * 4, width - 2 - 35, 12, i, j, onPxW, onPxH, monitorHeight, getRadioUrl(data));
    }

    @Override
    public void tick() {
        super.tick();
        this.radioStreamStartButton.active = canRadioStreamStart();
    }

    private boolean canRadioStreamStart(BoomboxData data) {
        return !getRadioSource(data).isEmpty();
    }

    private boolean canRadioStreamStart() {
        return !getRadioSource().isEmpty();
    }

    public void setRadioUrl(String url) {
        getScreen().insRadioUrl(url);
        startRadioCheckLoad(url);
    }

    private boolean isRadioChecking() {
        return this.radioCheckLoader != null && this.radioCheckLoader.isAlive();
    }

    private void startRadioCheckLoad(String url) {
        if (url.equals(lastRadioUrl)) return;
        setRadioSource(MusicSource.EMPTY);
        setRadioImage(ImageInfo.EMPTY);
        this.lastRadioUrl = url;
        stopRadioCheckLoad();
        this.radioCheckLoader = new RadioCheckLoader(url);
        this.radioCheckLoader.start();
    }

    private void stopRadioCheckLoad() {
        if (this.radioCheckLoader != null) {
            this.radioCheckLoader.stopped();
            this.radioCheckLoader = null;
        }
    }

    private void setRadioSource(MusicSource source) {
        getScreen().insRadioSource(source);
    }

    private void setRadioImage(ImageInfo image) {
        getScreen().insRadioImage(image);
    }

    private void setRadioAuthor(String author) {
        getScreen().insRadioAuthor(author);
    }

    private void setRadioName(String name) {
        getScreen().insRadioName(name);
    }

    private class RadioCheckLoader extends FlagThread {
        private final String url;

        private RadioCheckLoader(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            if (isStopped()) return;
            try {
                var otrack = LavaPlayerManager.getInstance().loadTrack(url);
                if (isStopped()) return;
                if (otrack.isPresent()) {
                    var track = otrack.get();
                    var info = track.getInfo();
                    if (!info.isStream) return;
                    if (isStopped()) return;

                    if (track.getSourceManager() instanceof YoutubeAudioSourceManager)
                        mc.submit(() -> setRadioImage(new ImageInfo(ImageInfo.ImageType.YOUTUBE_THUMBNAIL, info.identifier)));

                    if (isStopped()) return;
                    mc.submit(() -> {
                        setRadioSource(new MusicSource("", info.identifier, -1));
                        setRadioName(info.title);
                        setRadioAuthor(info.author);
                    });
                }
            } catch (Exception ignored) {
            }
        }
    }
}
