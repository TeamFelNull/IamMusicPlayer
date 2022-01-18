package dev.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.networking.NetworkManager;
import dev.felnull.fnjl.util.FNStringUtil;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.components.MusicLoaderTypesFixedButtonsList;
import dev.felnull.imp.client.gui.components.PlaybackProgressBar;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.client.music.loadertypes.IMPMusicLoaderTypes;
import dev.felnull.imp.client.music.loadertypes.IMusicLoaderType;
import dev.felnull.imp.client.music.loadertypes.MusicLoadResult;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.networking.IMPPackets;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class AddMusicMMMonitor extends ImageNameBaseMMMonitor {
    private static final ResourceLocation ADD_MUSIC_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/add_music.png");
    private static final Component PLAYBACK_CONTROL_TEXT = new TranslatableComponent("imp.button.playbackControl");
    private static final Component PLAYBACK_TEXT = new TranslatableComponent("imp.text.playback");
    private static final Component PLAYBACK_NON_PROGRESS_TEXT = new TextComponent("--:--/--:--");
    private static final Component PLAYBACK_LOADING_PROGRESS_TEXT = new TranslatableComponent("imp.text.playbackLoading");
    private static final Component MUSIC_SOURCE_TEXT = new TranslatableComponent("imp.text.musicSource");
    private static final Component MUSIC_CHECKING_TEXT = new TranslatableComponent("imp.text.musicChecking");
    private static final Component MUSIC_GUESSING_TEXT = new TranslatableComponent("imp.text.musicGuessing");
    private static final Component MUSIC_LOAD_FAILURE_TEXT = new TranslatableComponent("imp.text.loadFailure");
    private static final Component AUTO_ENTER_TEXT = new TranslatableComponent("imp.text.enterText.auto");
    private static final Component AUTO_INFO_TEXT = new TranslatableComponent("imp.text.loaderTypeInfo.auto");
    private static final Component AUTO_FAILURE_TEXT = new TranslatableComponent("imp.text.loadFailure.auto");
    private static final Component UPLOAD_MUSIC_TEXT = new TranslatableComponent("imp.button.uploadMusic");
    private final List<String> musicLoaderTypes = new ArrayList<>();
    private SmartButton playControlButton;
    private EditBox musicSourceNameEditBox;
    private SmartButton searchButton;
    private SmartButton uploadButton;
    private MusicLoadThread musicLoadThread;
    private boolean loadFailure;
    private Component AUTHOR_TEXT;

    public AddMusicMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.musicLoaderTypes.clear();
        this.musicLoaderTypes.add("auto");
        this.musicLoaderTypes.add("upload");
        this.musicLoaderTypes.addAll(IMPMusicLoaderTypes.getMusicLoaderTypes().keySet());

        this.playControlButton = this.addRenderWidget(new SmartButton(getStartX() + 5, getStartY() + 140, 20, 20, PLAYBACK_CONTROL_TEXT, n -> {
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

        this.addRenderWidget(new MusicLoaderTypesFixedButtonsList(getStartX() + 189, getStartY() + 23, 175, 65, 5, new TranslatableComponent("imp.fixedList.musicLoaderTypes"), musicLoaderTypes, (fixedButtonsList, s, i, i1) -> {
            setMusicLoaderType(s);
            loadFailure = false;
        }, n -> getMusicLoaderType().equals(n)));

        this.musicSourceNameEditBox = new EditBox(IIMPSmartRender.mc.font, getStartX() + 189, getStartY() + 112, isMSNShortWidth() ? 141 : 177, 12, new TranslatableComponent("imp.editBox.musicSourceName"));
        this.musicSourceNameEditBox.setMaxLength(300);
        this.musicSourceNameEditBox.visible = isMSNVisible();
        this.musicSourceNameEditBox.setValue(getMusicSourceName());
        this.musicSourceNameEditBox.setResponder(this::setMusicSourceName);
        addRenderWidget(this.musicSourceNameEditBox);

        startMusicLoad(getMusicSourceName(), getScreen().lastSearch);
        getScreen().lastSearch = false;

        this.searchButton = this.addRenderWidget(new SmartButton(getStartX() + 331, getStartY() + 111, 35, 14, new TranslatableComponent("imp.button.search"), n -> {
            insMonitor(MusicManagerBlockEntity.MonitorType.SEARCH_MUSIC);
        }));
        this.searchButton.setHideText(true);
        this.searchButton.setIcon(WIDGETS_TEXTURE, 24, 107, 12, 12);
        this.searchButton.visible = isMSNVisible() && isMSNShortWidth();

        this.uploadButton = this.addRenderWidget(new SmartButton(getStartX() + 189, getStartY() + 111, 177, 14, UPLOAD_MUSIC_TEXT, n -> {
            insMonitor(MusicManagerBlockEntity.MonitorType.UPLOAD_MUSIC);
        }));
        this.uploadButton.setIcon(WIDGETS_TEXTURE, 36, 107, 12, 12);
        this.uploadButton.visible = "upload".equals(getMusicLoaderType());

        if (!getMusicAuthor().isEmpty())
            AUTHOR_TEXT = new TranslatableComponent("imp.text.musicAuthor", getMusicAuthor());
    }

    @Override
    public void depose() {
        super.depose();
        stopMusicLoad();
        AUTHOR_TEXT = null;
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
        } else if (!getMusicSource().isEmpty()) {
            pt = new TextComponent(FNStringUtil.getTimeProgress(0, getMusicSource().getDuration()));
        } else {
            pt = PLAYBACK_NON_PROGRESS_TEXT;
        }
        drawSmartText(poseStack, pt, getStartX() + 27, getStartY() + 144);

        drawSmartText(poseStack, MUSIC_SOURCE_TEXT, getStartX() + 189, getStartY() + 13);

        if (musicLoadThread != null)
            drawSmartText(poseStack, "auto".equals(getMusicLoaderType()) ? MUSIC_GUESSING_TEXT : MUSIC_CHECKING_TEXT, getStartX() + 189, getStartY() + 128);

        if (getRawMusicLoaderType() != null) {
            drawSmartText(poseStack, getRawMusicLoaderType().getEnterText(), getStartX() + 189, getStartY() + 102);
        } else if ("auto".equals(getMusicLoaderType())) {
            drawSmartText(poseStack, AUTO_ENTER_TEXT, getStartX() + 189, getStartY() + 102);
            drawSmartText(poseStack, AUTO_INFO_TEXT, getStartX() + 189, getStartY() + 90);
        }

        if (loadFailure && !getMusicSourceName().isEmpty())
            drawSmartText(poseStack, "auto".equals(getMusicLoaderType()) ? AUTO_FAILURE_TEXT : MUSIC_LOAD_FAILURE_TEXT, getStartX() + 189, getStartY() + 128);

        if (AUTHOR_TEXT != null)
            drawSmartText(poseStack, AUTHOR_TEXT, getStartX() + 5, getStartY() + 162);
    }

    @Override
    public void create(ImageInfo imageInfo, String name) {
        var ms = getMusicSource();
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
            NetworkManager.sendToServer(IMPPackets.MUSIC_ADD, new IMPPackets.MusicAddMessage(musicManagerBlock.getMySelectedPlayList(), name, getMusicAuthor(), imageInfo, ms).toFBB());
    }

    @Override
    public boolean canCreate(MusicManagerBlockEntity blockEntity) {
        return super.canCreate(blockEntity) && !getMusicSource(blockEntity).isEmpty();
    }

    @Override
    public List<Component> getNotEntered(List<Component> names, MusicManagerBlockEntity blockEntity) {
        if (getMusicSource(blockEntity).isEmpty())
            names.add(MUSIC_SOURCE_TEXT);
        return super.getNotEntered(names, blockEntity);
    }

    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.PLAY_LIST;
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

        this.musicSourceNameEditBox.visible = isMSNVisible();
        this.musicSourceNameEditBox.setWidth(isMSNShortWidth() ? 141 : 177);

        this.searchButton.visible = isMSNVisible() && isMSNShortWidth();
        this.uploadButton.visible = "upload".equals(getMusicLoaderType());

        if ((getScreen().isMusicPlaying() && !getMusicSource().equals(getScreen().getMusicPlayer().getMusicSource())) || (getScreen().isMusicLoading() && !getMusicSource().equals(getScreen().getLoadingMusic().getSource())))
            getScreen().stopMusic();
    }

    private boolean isMSNVisible() {
        return !getMusicLoaderType().equals("upload");
    }

    @Override
    protected DoneType getDoneType() {
        return DoneType.ADD;
    }

    private boolean isMSNShortWidth() {
        var rtp = getRawMusicLoaderType();
        return rtp != null && rtp.isSearchable();
    }

    public String getMusicAuthor() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
            return getMusicAuthor(musicManagerBlock);
        return "";
    }

    public String getMusicAuthor(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getMyMusicAuthor();
    }

    private void setMusicAuthor(String author) {
        AUTHOR_TEXT = author.isEmpty() ? null : new TranslatableComponent("imp.text.musicAuthor", author);
        getScreen().insMusicAuthor(author);
    }

    public MusicSource getMusicSource() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity blockEntity)
            return getMusicSource(blockEntity);
        return MusicSource.EMPTY;
    }

    public IMusicLoaderType getRawMusicLoaderType() {
        return IMPMusicLoaderTypes.getMusicLoaderTypes().get(getMusicLoaderType());
    }

    public String getMusicLoaderType() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity blockEntity)
            return getMusicLoaderType(blockEntity);
        return "auto";
    }

    public String getMusicLoaderType(MusicManagerBlockEntity blockEntity) {
        return blockEntity.getMyMusicLoaderType().isEmpty() ? "auto" : blockEntity.getMyMusicLoaderType();
    }

    private void setMusicLoaderType(String name) {
        musicSourceNameEditBox.setValue("");
        getScreen().insMusicLoaderType(name);
    }

    public String getMusicSourceName() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity blockEntity)
            return getMusicSourceName(blockEntity);
        return "";
    }

    public String getMusicSourceName(MusicManagerBlockEntity blockEntity) {
        return blockEntity.getMyMusicSourceName();
    }

    private void setMusicSourceName(String name) {
        if (!getMusicSourceName().equals(name))
            startMusicLoad(name, true);
        getScreen().insMusicSourceName(name);
    }

    public MusicSource getMusicSource(MusicManagerBlockEntity blockEntity) {
        return blockEntity.getMyMusicSource();
    }

    private void setMusicSource(MusicSource source, String author) {
        getScreen().insMusicSource(source);
        setMusicAuthor(author);
    }

    private void setLoadResult(MusicLoadResult result, boolean autoIn) {
        if (result != null) {
            setMusicSource(result.source(), result.author());
            if (autoIn) {
                if (result.imageInfo() != null)
                    setImage(result.imageInfo());
                if (result.name() != null)
                    this.nameEditBox.setValue(result.name());
            }
        } else {
            setMusicSource(MusicSource.EMPTY, "");
        }
    }

    private void startMusicLoad(String name, boolean autoIn) {
        stopMusicLoad();
        if (getRawMusicLoaderType() != null || "auto".equals(getMusicLoaderType())) {
            this.loadFailure = false;
            this.musicLoadThread = new MusicLoadThread(getMusicLoaderType(), name, autoIn);
            this.musicLoadThread.start();
        }
    }

    private void stopMusicLoad() {
        if (this.musicLoadThread != null) {
            this.musicLoadThread.interrupt();
            this.musicLoadThread = null;
        }
    }

    private class MusicLoadThread extends Thread {
        private final String name;
        private final String loaderType;
        private final boolean autoIn;

        private MusicLoadThread(String loaderType, String name, boolean autoIn) {
            setName("Music Load Thread");
            this.loaderType = loaderType;
            this.name = name;
            this.autoIn = autoIn;
        }

        @Override
        public void run() {
            try {
                var loader = IMPMusicLoaderTypes.getMusicLoaderTypes().get(loaderType);
                if (loader != null) {
                    var r = loader.load(name);
                    setLoadResult(r, autoIn);
                    if (r == null)
                        loadFailure = true;
                } else if ("auto".equals(loaderType)) {
                    var ar = IMPMusicLoaderTypes.loadAuto(name);
                    if (ar != null) {
                        setMusicLoaderType(ar.getKey());
                        IIMPSmartRender.mc.submit(() -> {
                            musicSourceNameEditBox.setValue(ar.getValue().source().getIdentifier());
                            startMusicLoad(ar.getValue().source().getIdentifier(), autoIn);
                        });
                    } else {
                        loadFailure = true;
                    }
                }
            } catch (InterruptedException ignored) {
            }
            musicLoadThread = null;
        }
    }
}
