package dev.felnull.imp.client.gui.screen.monitor.boombox;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.BoomboxScreen;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.client.music.loadertypes.IMPMusicLoaderTypes;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import dev.felnull.imp.data.BoomboxData;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.gui.components.EditBox;
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
            //insMonitor(CassetteDeckBlockEntity.MonitorType.WRITE_EXECUTION)
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
            drawSmartText(poseStack, RADIO_CHECKING_TEXT, getStartX() + 2, getStartY() + 2);
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
            drawSmartText(poseStack, ENTER_STREAM_TEXT, getStartX() + 2, getStartY() + 2);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.radioStreamStartButton.active = canRadioStreamStart();
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
            this.radioCheckLoader.interrupt();
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

    private class RadioCheckLoader extends Thread {
        private final String url;

        private RadioCheckLoader(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            try {
                var otrack = LavaPlayerUtil.loadTrack(IMPMusicLoaderTypes.allAudioPlayerManager, url);
                if (otrack.isPresent()) {
                    var track = otrack.get();
                    var info = track.getInfo();
                    if (!info.isStream) return;

                    if (track.getSourceManager() instanceof YoutubeAudioSourceManager)
                        mc.submit(() -> setRadioImage(new ImageInfo(ImageInfo.ImageType.YOUTUBE_THUMBNAIL, info.identifier)));

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
