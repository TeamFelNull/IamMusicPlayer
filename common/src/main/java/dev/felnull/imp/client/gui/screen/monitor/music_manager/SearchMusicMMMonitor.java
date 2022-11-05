package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.components.SearchMusicsFixedListWidget;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.client.music.media.IMPMusicMedias;
import dev.felnull.imp.client.music.media.MusicMedia;
import dev.felnull.imp.client.music.media.MusicMediaResult;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import dev.felnull.otyacraftengine.util.FlagThread;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class SearchMusicMMMonitor extends MusicManagerMonitor {
    private static final ResourceLocation SEARCH_MUSIC_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/search_music.png");
    private static final Component SEARCHING_TEXT = Component.translatable("imp.text.searching");
    private final List<MusicMediaResult> searchMusics = new ArrayList<>();
    private SearchMusicsFixedListWidget searchMusicsFixedButtonsList;
    private EditBox searchNameEditBox;
    private SearchThread searchThread;

    public SearchMusicMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.searchMusicsFixedButtonsList = this.addRenderWidget(new SearchMusicsFixedListWidget(getStartX() + 2, getStartY() + 25, 366, 172, Component.translatable("imp.fixedList.searchMusic"), 4, searchMusics, (widget, item) -> {
            setMusicSourceName(item.source().getIdentifier());
            getScreen().lastSearch = true;
            insMonitor(MusicManagerBlockEntity.MonitorType.ADD_MUSIC);
        }, this.searchMusicsFixedButtonsList));

        this.searchNameEditBox = new EditBox(IIMPSmartRender.mc.font, getStartX() + 2, getStartY() + 11, 367, 12, Component.translatable("imp.editBox.musicSearchName"));
        this.searchNameEditBox.setMaxLength(300);
        this.searchNameEditBox.setValue(getMusicSearchName());
        this.searchNameEditBox.setResponder(this::setMusicSearchName);
        addRenderWidget(this.searchNameEditBox);

        startMusicSearch(getMusicSearchName());
    }

    @Override
    public void depose() {
        super.depose();
        stopMusicSearch();
        searchMusics.clear();
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtils.drawTexture(SEARCH_MUSIC_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
        if (searchMusics.isEmpty() && !getMusicSearchName().isEmpty() && searchThread != null && searchThread.isAlive())
            drawSmartText(poseStack, SEARCHING_TEXT, getStartX() + 3, getStartY() + 27);
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        OERenderUtils.renderTextureSprite(SEARCH_MUSIC_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtils.MIN_BREADTH * 3, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
        renderSmartEditBoxSprite(poseStack, multiBufferSource, 2, 11, OERenderUtils.MIN_BREADTH * 3, 367, 12, i, j, onPxW, onPxH, monitorHeight, getMusicSearchName(blockEntity));

        renderScrollbarSprite(poseStack, multiBufferSource, 359, 25, OERenderUtils.MIN_BREADTH * 3, 172, i, j, onPxW, onPxH, monitorHeight, 0, 6);
    }

    private void startMusicSearch(String name) {
        this.stopMusicSearch();
        this.searchThread = new SearchThread(name);
        this.searchThread.start();
    }

    private void stopMusicSearch() {
        if (this.searchThread != null) {
            this.searchThread.stopped();
            this.searchThread = null;
        }
    }

    public String getMusicSearchName() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity blockEntity)
            return getMusicSearchName(blockEntity);
        return "";
    }

    public String getMusicSearchName(MusicManagerBlockEntity blockEntity) {
        return blockEntity.getMusicSearchName(mc.player);
    }

    private void setMusicSearchName(String name) {
        if (!getMusicSearchName().equals(name))
            startMusicSearch(name);
        getScreen().insMusicSearchName(name);
    }

    private void setMusicSourceName(String name) {
        getScreen().insMusicSourceName(name);
    }

    public MusicMedia getRawMusicLoaderType() {
        return IMPMusicMedias.getAllMedia().get(getMusicLoaderType());
    }

    public String getMusicLoaderType() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity blockEntity)
            return getMusicLoaderType(blockEntity);
        return "auto";
    }

    public String getMusicLoaderType(MusicManagerBlockEntity blockEntity) {
        return blockEntity.getMusicLoaderType(mc.player).isEmpty() ? "auto" : blockEntity.getMusicLoaderType(mc.player);
    }

    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.ADD_MUSIC;
    }

    private class SearchThread extends FlagThread {
        private final String name;

        public SearchThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            if (isStopped())
                return;
            var lt = getRawMusicLoaderType();
            if (lt == null || !lt.isSearchable())
                return;

            if (isStopped())
                return;

            List<MusicMediaResult> slst;
            slst = lt.search(name);

            if (isStopped())
                return;

            setSearchMusics(slst);
        }

        private synchronized void setSearchMusics(List<MusicMediaResult> musics) {
            if (musics != null) {
                searchMusics.clear();
                searchMusics.addAll(musics);
            }
        }
    }
}
