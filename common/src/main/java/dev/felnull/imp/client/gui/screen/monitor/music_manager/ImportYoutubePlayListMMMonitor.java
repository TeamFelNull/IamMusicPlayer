package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.components.YoutubePlayListMusicsFixedButtonsList;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.client.music.loadertypes.IMPMusicLoaderTypes;
import dev.felnull.imp.client.music.loadertypes.YoutubeMusicLoaderType;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.otyacraftengine.client.gui.components.FixedButtonsList;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ImportYoutubePlayListMMMonitor extends MusicManagerMonitor {
    private static final ResourceLocation IMPORT_YOUTUBE_PLAY_LIST_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/import_youtube_play_list.png");
    private static final Component BACK_TEXT = new TranslatableComponent("imp.button.back");
    private static final Component LOADING_TEXT = new TranslatableComponent("imp.text.playlistLoading");
    private final List<YoutubePlayListEntry> youtubePlayListEntries = new ArrayList<>();
    private SmartButton importButton;
    private PlayListLoadThread playListLoader;
    private EditBox playlistIdentifierEditBox;

    public ImportYoutubePlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);

        addRenderWidget(new SmartButton(getStartX() + 5, getStartY() + 180, 87, 15, BACK_TEXT, n -> insMonitor(MusicManagerBlockEntity.MonitorType.IMPORT_PLAY_LIST_SELECT)));

        this.importButton = addRenderWidget(new SmartButton(getStartX() + 95, getStartY() + 180, 87, 15, CreatePlayListMMMonitor.IMPORT_TEXT, n -> {

        }));
        this.importButton.active = canImport();

        this.playlistIdentifierEditBox = addRenderWidget(new EditBox(mc.font, getStartX() + 6, getStartY() + 164, 175, 12, new TranslatableComponent("imp.editBox.youtubePlaylistIdentifier")));
        this.playlistIdentifierEditBox.setMaxLength(300);
        this.playlistIdentifierEditBox.setResponder(this::startPlayListLoad);
        this.playlistIdentifierEditBox.setValue(getImportPlayList());

        addRenderWidget(new YoutubePlayListMusicsFixedButtonsList(getStartX() + 1, getStartY() + 10, 368, 148, 4, new TranslatableComponent("imp.fixedList.youtubePlayListMusics"), youtubePlayListEntries, new FixedButtonsList.PressEntry<YoutubePlayListEntry>() {
            @Override
            public void onPressEntry(FixedButtonsList<YoutubePlayListEntry> fixedButtonsList, YoutubePlayListEntry youtubePlayListEntry, int i, int i1) {

            }
        }));

        startPlayListLoad(getImportPlayList());
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(IMPORT_YOUTUBE_PLAY_LIST_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
        if (isPlayListLoading()) {
            drawSmartText(poseStack, LOADING_TEXT, getStartX() + 2, getStartY() + 11);
        }
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        OERenderUtil.renderTextureSprite(IMPORT_YOUTUBE_PLAY_LIST_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);

        renderSmartButtonSprite(poseStack, multiBufferSource, 5, 180, OERenderUtil.MIN_BREADTH * 4, 87, 15, i, j, onPxW, onPxH, monitorHeight, BACK_TEXT, true);

        renderSmartButtonSprite(poseStack, multiBufferSource, 95, 180, OERenderUtil.MIN_BREADTH * 4, 87, 15, i, j, onPxW, onPxH, monitorHeight, CreatePlayListMMMonitor.IMPORT_TEXT, true, !canImport(blockEntity));
    }

    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.IMPORT_PLAY_LIST_SELECT;
    }

    @Override
    public void tick() {
        super.tick();
        this.importButton.active = canImport();
    }

    private boolean canImport(MusicManagerBlockEntity blockEntity) {
        return !getImportPlayList(blockEntity).isEmpty();
    }

    private boolean canImport() {
        return !getImportPlayList().isEmpty();
    }

    private void setImportPlayList(String id) {
        getScreen().insImportIdentifier(id);
    }

    private String getImportPlayList() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getImportPlayList(musicManagerBlockEntity);
        return "";
    }

    private String getImportPlayList(MusicManagerBlockEntity blockEntity) {
        return blockEntity.getMyImportIdentifier();
    }

    private boolean isPlayListLoading() {
        return playListLoader != null && playListLoader.isAlive();
    }

    private void startPlayListLoad(String id) {
        stopPlayListLoad();
        youtubePlayListEntries.clear();
        playListLoader = new PlayListLoadThread(id);
        playListLoader.start();
    }

    private void stopPlayListLoad() {
        if (playListLoader != null) {
            playListLoader.interrupt();
            playListLoader = null;
        }
    }

    private class PlayListLoadThread extends Thread {
        private final String id;

        public PlayListLoadThread(String id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                var pl = LavaPlayerUtil.loadTracks(getManager(), id);
                for (AudioTrack track : pl.getRight()) {
                    var ret = getYoutubeLoaderType().createResult(track);
                    var en = new YoutubePlayListEntry(ret.name(), ret.author(), ret.source(), ret.imageInfo());
                    youtubePlayListEntries.add(en);
                    setImportPlayList(id);
                }
            } catch (Exception ignored) {
            }
        }

        private AudioPlayerManager getManager() {
            return getYoutubeLoaderType().getAudioPlayerManager();
        }
    }

    private YoutubeMusicLoaderType getYoutubeLoaderType() {
        return ((YoutubeMusicLoaderType) IMPMusicLoaderTypes.getLoaderType(IMPMusicLoaderTypes.YOUTUBE));
    }

    public static record YoutubePlayListEntry(String name, String artist, MusicSource source, ImageInfo imageInfo) {
    }
}