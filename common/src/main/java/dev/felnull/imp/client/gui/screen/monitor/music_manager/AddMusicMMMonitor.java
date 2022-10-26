package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.components.MusicLoaderTypesFixedButtonsList;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.client.lava.LavaPlayerManager;
import dev.felnull.imp.client.music.media.IMPMusicMedias;
import dev.felnull.imp.client.music.media.MusicMediaResult;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.networking.IMPPackets;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import dev.felnull.otyacraftengine.networking.existence.BlockEntityExistence;
import dev.felnull.otyacraftengine.util.FlagThread;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class AddMusicMMMonitor extends SavedMusicBaseMMMonitor {
    private static final Component MUSIC_CHECKING_TEXT = Component.translatable("imp.text.musicChecking");
    private static final Component MUSIC_GUESSING_TEXT = Component.translatable("imp.text.musicGuessing");
    private static final Component MUSIC_LOAD_FAILURE_TEXT = Component.translatable("imp.text.loadFailure");
    private static final Component AUTO_ENTER_TEXT = Component.translatable("imp.text.enterText.auto");
    private static final Component AUTO_INFO_TEXT = Component.translatable("imp.text.loaderTypeInfo.auto");
    private static final Component AUTO_FAILURE_TEXT = Component.translatable("imp.text.loadFailure.auto");
    private static final Component UPLOAD_MUSIC_TEXT = Component.translatable("imp.button.uploadMusic");
    protected static final Component IMPORT_TEXT = Component.translatable("imp.button.import");
    private final List<String> musicLoaderTypes = new ArrayList<>();
    private EditBox musicSourceNameEditBox;
    private SmartButton searchButton;
    private SmartButton uploadButton;
    private MusicLoadThread musicLoadThread;
    private boolean loadFailure;

    public AddMusicMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
        this.playBackX = 189;
        this.playBackY = 140;
        this.isLoaderSelect = true;
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.musicLoaderTypes.clear();
        this.musicLoaderTypes.add("auto");
        this.musicLoaderTypes.add("upload");
        this.musicLoaderTypes.addAll(IMPMusicMedias.getAllMedia().keySet());

        this.addRenderWidget(new MusicLoaderTypesFixedButtonsList(getStartX() + 189, getStartY() + 23, 175, 65, 5, Component.translatable("imp.fixedList.musicLoaderTypes"), musicLoaderTypes, (fixedButtonsList, s, i, i1) -> {
            setMusicLoaderType(s);
            loadFailure = false;
        }, n -> getMusicLoaderType().equals(n)));

        this.musicSourceNameEditBox = new EditBox(IIMPSmartRender.mc.font, getStartX() + 189, getStartY() + 112, isMSNShortWidth() ? 141 : 177, 12, Component.translatable("imp.editBox.musicSourceName"));
        this.musicSourceNameEditBox.setMaxLength(300);
        this.musicSourceNameEditBox.visible = isMSNVisible();
        this.musicSourceNameEditBox.setValue(getMusicSourceName());
        this.musicSourceNameEditBox.setResponder(this::setMusicSourceName);
        addRenderWidget(this.musicSourceNameEditBox);

        startMusicLoad(getMusicSourceName(), getScreen().lastSearch);
        getScreen().lastSearch = false;

        this.searchButton = this.addRenderWidget(new SmartButton(getStartX() + 331, getStartY() + 111, 35, 14, Component.translatable("imp.button.search"), n -> {
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

        addRenderWidget(new SmartButton(getStartX() + width - 95 - 87, getStartY() + 180, 87, 15, IMPORT_TEXT, n -> insMonitor(MusicManagerBlockEntity.MonitorType.IMPORT_MUSICS_SELECT)));
    }

    @Override
    public void depose() {
        super.depose();
        stopMusicLoad();
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);

        if (musicLoadThread != null && !getMusicSourceName().isEmpty())
            drawSmartText(poseStack, "auto".equals(getMusicLoaderType()) ? MUSIC_GUESSING_TEXT : MUSIC_CHECKING_TEXT, getStartX() + 189, getStartY() + 128);

        if (getRawMusicLoaderType() != null) {
            drawSmartText(poseStack, getRawMusicLoaderType().getEnterText(), getStartX() + 189, getStartY() + 102);
        } else if ("auto".equals(getMusicLoaderType())) {
            drawSmartText(poseStack, AUTO_ENTER_TEXT, getStartX() + 189, getStartY() + 102);
            drawSmartText(poseStack, AUTO_INFO_TEXT, getStartX() + 189, getStartY() + 90);
        }

        if (loadFailure && !getMusicSourceName().isEmpty())
            drawSmartText(poseStack, "auto".equals(getMusicLoaderType()) ? AUTO_FAILURE_TEXT : MUSIC_LOAD_FAILURE_TEXT, getStartX() + 189, getStartY() + 128);
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);

        this.musicLoaderTypes.clear();
        this.musicLoaderTypes.add("auto");
        this.musicLoaderTypes.add("upload");
        this.musicLoaderTypes.addAll(IMPMusicMedias.getAllMedia().keySet());

        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;


        var ml = getRawMusicLoaderType(blockEntity);

        if (ml != null) {
            renderSmartTextSprite(poseStack, multiBufferSource, ml.getEnterText(), 189, 102, OERenderUtils.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
        } else if ("auto".equals(getMusicLoaderType(blockEntity))) {
            renderSmartTextSprite(poseStack, multiBufferSource, AUTO_ENTER_TEXT, 189, 102, OERenderUtils.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
            renderSmartTextSprite(poseStack, multiBufferSource, AUTO_INFO_TEXT, 189, 91, OERenderUtils.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
        }

        if (!"upload".equals(getMusicLoaderType(blockEntity))) {
            var rtp = getRawMusicLoaderType(blockEntity);
            renderSmartEditBoxSprite(poseStack, multiBufferSource, 189, 112, OERenderUtils.MIN_BREADTH * 4, (rtp != null && rtp.isSearchable()) ? 141 : 177, 12, i, j, onPxW, onPxH, monitorHeight, getName(blockEntity));
            if (rtp != null && rtp.isSearchable())
                renderSmartButtonSprite(poseStack, multiBufferSource, 333, 111, OERenderUtils.MIN_BREADTH * 4, 33, 14, i, j, onPxW, onPxH, monitorHeight, MusicManagerMonitor.WIDGETS_TEXTURE, 24, 107, 11, 11, 256, 256);
        } else {
            renderSmartButtonSprite(poseStack, multiBufferSource, 189, 111, OERenderUtils.MIN_BREADTH * 4, 177, 14, i, j, onPxW, onPxH, monitorHeight, UPLOAD_MUSIC_TEXT, WIDGETS_TEXTURE, 36, 107, 12, 12, 256, 256);
        }

        renderScrollbarSprite(poseStack, multiBufferSource, 355, 23, OERenderUtils.MIN_BREADTH * 3, 65, i, j, onPxW, onPxH, monitorHeight, musicLoaderTypes.size(), 5);

        for (int k = 0; k < Math.min(5, musicLoaderTypes.size()); k++) {
            var lt = musicLoaderTypes.get(k);
            renderSmartButtonBoxSprite(poseStack, multiBufferSource, 189, 23 + (k * 13), OERenderUtils.MIN_BREADTH * 3, 165, 13, i, j, onPxW, onPxH, monitorHeight, lt.equals(getMusicLoaderType(blockEntity)));
            var type = IMPMusicMedias.getAllMedia().get(lt);
            int tx = 189 + 2;
            if ((type != null && type.getIcon() != null) || "upload".equals(lt)) {
                var icon = type != null ? type.getIcon() : MusicLoaderTypesFixedButtonsList.UPLOAD_ICON;
                OERenderUtils.renderTextureSprite(icon, poseStack, multiBufferSource, (189f + 1f) * onPxW, monitorHeight - (23f + ((float) k * 13f) + 1f + 11f) * onPxH, OERenderUtils.MIN_BREADTH * 5, 0, 0, 0, 11f * onPxW, 11f * onPxH, 0, 0, 11f * onPxW, 11f * onPxH, 11f * onPxW, 11f * onPxH, i, j);
                tx += 13 - 1;
            }
            renderSmartTextSprite(poseStack, multiBufferSource, type == null ? Component.translatable("imp.loaderType." + lt) : type.getMediaName(), tx, 23 + (k * 13) + (13f - 6.5f) / 2f + 1f, OERenderUtils.MIN_BREADTH * 5, onPxW, onPxH, monitorHeight, i);
        }

        renderSmartButtonSprite(poseStack, multiBufferSource, width - 95 - 87, 180, OERenderUtils.MIN_BREADTH * 4, 87, 15, i, j, onPxW, onPxH, monitorHeight, IMPORT_TEXT, true);
    }

    @Override
    public boolean done(ImageInfo imageInfo, String name) {
        var ms = getMusicSource();
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock && musicManagerBlock.getSelectedPlayList(mc.player) != null)
            NetworkManager.sendToServer(IMPPackets.MUSIC_ADD, new IMPPackets.MusicMessage(musicManagerBlock.getSelectedPlayList(mc.player), name, getMusicAuthor(), imageInfo, ms, BlockEntityExistence.getByBlockEntity(getScreen().getBlockEntity())).toFBB());
        return true;
    }

    @Override
    public boolean canDone(MusicManagerBlockEntity blockEntity) {
        return super.canDone(blockEntity) && !getMusicSource(blockEntity).isEmpty();
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
        this.musicSourceNameEditBox.visible = isMSNVisible();
        this.musicSourceNameEditBox.setWidth(isMSNShortWidth() ? 141 : 177);

        this.searchButton.visible = isMSNVisible() && isMSNShortWidth();
        this.uploadButton.visible = "upload".equals(getMusicLoaderType());

        if ((getScreen().isMusicPlaying() && !getMusicSource().equals(getScreen().getMusicPlayer().getSource())) || (getScreen().isMusicLoading() && !getMusicSource().equals(getScreen().getMusicPlayer().getSource())))
            getScreen().stopMusic();
    }

    private boolean isMSNVisible() {
        return !"upload".equals(getMusicLoaderType());
    }

    @Override
    protected DoneType getDoneType() {
        return DoneType.ADD;
    }

    private boolean isMSNShortWidth() {
        var rtp = getRawMusicLoaderType();
        return rtp != null && rtp.isSearchable();
    }


    private void setMusicLoaderType(String name) {
        if (!"auto".equals(getMusicLoaderType()))
            musicSourceNameEditBox.setValue("");
        getScreen().insMusicLoaderType(name);
    }

    private void setMusicSourceName(String name) {
        if (!getMusicSourceName().equals(name))
            startMusicLoad(name, true);
        getScreen().insMusicSourceName(name);
    }


    private void setLoadResult(MusicMediaResult result, boolean autoIn) {
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
        if (getRawMusicLoaderType() != null || "auto".equals(getMusicLoaderType()) || "upload".equals(getMusicLoaderType())) {
            this.loadFailure = false;
            this.musicLoadThread = new MusicLoadThread(getMusicLoaderType(), name, autoIn);
            this.musicLoadThread.start();
        }
    }

    private void stopMusicLoad() {
        if (this.musicLoadThread != null) {
            this.musicLoadThread.stopped();
            this.musicLoadThread = null;
        }
    }

    private class MusicLoadThread extends FlagThread {
        private final String name;
        private final String loaderType;
        private boolean autoIn;

        private MusicLoadThread(String loaderType, String name, boolean autoIn) {
            setName("Music Load Thread");
            this.loaderType = loaderType;
            this.name = name;
            this.autoIn = autoIn;
        }

        @Override
        public void run() {
            try {
                if (isStopped()) return;
                var loader = IMPMusicMedias.getAllMedia().get(loaderType);
                if ("upload".equals(loaderType))
                    loader = IMPMusicMedias.HTTP;
                if (isStopped()) return;

                if (loader != null) {
                    if (isStopped()) return;
                    var r = loader.load(name);
                    if (isStopped()) return;
                    setLoadResult(r, autoIn);
                    if (r == null)
                        loadFailure = true;
                    if (isStopped()) return;
                } else if ("auto".equals(loaderType)) {
                    if (isStopped()) return;
                    var ar = LavaPlayerManager.getInstance().autoLoad(name);
                    if (isStopped()) return;
                    if (ar != null) {
                        setMusicLoaderType(ar.getKey().getName());
                        if (isStopped()) return;
                        IIMPSmartRender.mc.submit(() -> {
                            musicSourceNameEditBox.setValue(ar.getValue().source().getIdentifier());
                            startMusicLoad(ar.getValue().source().getIdentifier(), autoIn);
                        });
                        if (isStopped()) return;
                    } else {
                        loadFailure = true;
                    }
                    if (isStopped()) return;
                }
            } catch (Exception ignored) {
            }
            musicLoadThread = null;
        }
    }
}
