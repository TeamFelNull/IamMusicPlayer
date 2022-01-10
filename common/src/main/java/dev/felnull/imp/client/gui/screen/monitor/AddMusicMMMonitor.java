package dev.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
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
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddMusicMMMonitor extends CreateBaseMMMonitor {
    private static final ResourceLocation ADD_MUSIC_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/add_music.png");
    private static final Component PLAYBACK_CONTROL_TEXT = new TranslatableComponent("imp.button.playbackControl");
    private static final Component PLAYBACK_TEXT = new TranslatableComponent("imp.text.playback");
    private static final Component PLAYBACK_NON_PROGRESS_TEXT = new TextComponent("--:--/--:--");
    private static final Component PLAYBACK_LOADING_PROGRESS_TEXT = new TranslatableComponent("imp.text.playbackLoading");
    private static final Component MUSIC_SOURCE_TEXT = new TranslatableComponent("imp.text.musicSource");
    private static final Component MUSIC_CHECKING_TEXT = new TranslatableComponent("imp.text.musicChecking");
    private final List<String> musicLoaderTypes = new ArrayList<>();
    private final List<Component> musicLoadInfos = new ArrayList<>();
    private SmartButton playControlButton;
    private EditBox musicSourceNameEditBox;
    private MusicLoadThread musicLoadThread;

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

        this.addRenderWidget(new MusicLoaderTypesFixedButtonsList(getStartX() + 189, getStartY() + 23, 175, 65, 5, new TranslatableComponent("imp.fixedList.musicLoaderTypes"), musicLoaderTypes, (fixedButtonsList, s, i, i1) -> setMusicLoaderType(s), n -> getMusicLoaderType().equals(n)));

        this.musicSourceNameEditBox = new EditBox(IIMPSmartRender.mc.font, getStartX() + 189, getStartY() + 112, isMSNShortWidth() ? 141 : 177, 12, new TranslatableComponent("imp.editBox.musicSourceName"));
        this.musicSourceNameEditBox.setMaxLength(300);
        this.musicSourceNameEditBox.visible = isMSNVisible();
        this.musicSourceNameEditBox.setValue(getMusicSourceName());
        this.musicSourceNameEditBox.setResponder(this::setMusicSourceName);
        addRenderWidget(this.musicSourceNameEditBox);

        startMusicLoad(getMusicSourceName(), false);
    }

    @Override
    public void depose() {
        super.depose();
        stopMusicLoad();
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

        if (getRawMusicLoaderType() != null) {
            if (musicLoadThread != null)
                drawSmartText(poseStack, MUSIC_CHECKING_TEXT, getStartX() + 189, getStartY() + 128);
        }

    }

    @Override
    public void create(ImageInfo imageInfo, String name) {
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

        if ((getScreen().isMusicPlaying() && !getMusicSource().equals(getScreen().getMusicPlayer().getMusicSource())) || (getScreen().isMusicLoading() && !getMusicSource().equals(getScreen().getLoadingMusic().getSource())))
            getScreen().stopMusic();
    }

    private boolean isMSNVisible() {
        return !getMusicLoaderType().equals("upload");
    }

    private boolean isMSNShortWidth() {
        var rtp = getRawMusicLoaderType();
        return rtp != null && rtp.isSearchable();
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

    private void setMusicSource(MusicSource source) {
        getScreen().insMusicSource(source);
    }

    private void setLoadResult(MusicLoadResult result, boolean autoIn) {
        musicLoadInfos.clear();
        if (result != null) {
            setMusicSource(result.source());
            if (autoIn) {
                if (result.imageInfo() != null)
                    setImage(result.imageInfo());
                if (result.name() != null)
                    this.nameEditBox.setValue(result.name());
            }
            musicLoadInfos.addAll(result.info());
        } else {
            setMusicSource(MusicSource.EMPTY);
        }
    }

    private void startMusicLoad(String name, boolean autoIn) {
        stopMusicLoad();
        if (getRawMusicLoaderType() != null || "auto".equals(getMusicLoaderType())) {
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
                    setLoadResult(loader.load(name), autoIn);
                } else if ("auto".equals(loaderType)) {
                    var ar = loadAuto(name);
                    if (ar != null) {
                        setMusicLoaderType(ar.getKey());
                        IIMPSmartRender.mc.submit(() -> {
                            musicSourceNameEditBox.setValue(ar.getValue().source().getIdentifier());
                            startMusicLoad(ar.getValue().source().getIdentifier(), autoIn);
                        });
                    }
                }
            } catch (InterruptedException ignored) {
            }
            musicLoadThread = null;
        }
    }

    private static Pair<String, MusicLoadResult> loadAuto(String sourceName) throws InterruptedException {
        for (Map.Entry<String, IMusicLoaderType> entry : IMPMusicLoaderTypes.getMusicLoaderTypes().entrySet()) {
            var src = entry.getValue().load(sourceName);
            if (src != null)
                return Pair.of(entry.getKey(), src);
        }
        return null;
    }
}
