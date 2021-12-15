package dev.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.MyPlayListFixedButtonsList;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.components.SortButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class PlayListMMMonitor extends MusicManagerMonitor {
    private static final ResourceLocation PLAY_LIST_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/play_list.png");
    private final List<MusicPlayList> musicPlayLists = new ArrayList<>();
    private List<MusicPlayList> musicPlayListsCash = new ArrayList<>();
    private MusicPlayList selectedMusicPlayList;
    private SortButton.SortTypeButton playlistSortButton;
    private SortButton.OrderTypeButton playlistOrderButton;
    private SortButton.SortTypeButton musicSortButton;
    private SortButton.OrderTypeButton musicOrderButton;
    private SmartButton addPlaylistButton;
    private SmartButton addMusic;

    public PlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        addRenderWidget(new MyPlayListFixedButtonsList(getStartX() + 1, getStartY() + 20, new TranslatableComponent("imp.fixedList.myPlaylist"), musicPlayLists, (fixedButtonsList, playList, i, i1) -> {
            selectedMusicPlayList = playList;
        }, n -> n.equals(selectedMusicPlayList)));

        this.addPlaylistButton = addRenderWidget(new SmartButton(getStartX() + 1, getStartY() + 189, 72, 9, new TranslatableComponent("imp.button.addPlaylist"), n -> updateList()));
        this.addPlaylistButton.setIcon(WIDGETS_TEXTURE, 73, 14, 5, 5);

        this.addMusic = addRenderWidget(new SmartButton(getStartX() + 102, getStartY() + 189, 72, 9, new TranslatableComponent("imp.button.addMusic"), n -> updateList()));
        this.addMusic.setIcon(WIDGETS_TEXTURE, 73, 14, 5, 5);

        this.playlistSortButton = addRenderWidget(new SortButton.SortTypeButton(getStartX() + 73, getStartY() + 189, n -> updateList(), false, getScreen()));
        this.playlistOrderButton = addRenderWidget(new SortButton.OrderTypeButton(getStartX() + 82, getStartY() + 189, n -> updateList(), false, getScreen()));

        this.musicSortButton = addRenderWidget(new SortButton.SortTypeButton(getStartX() + 174, getStartY() + 189, n -> updateList(), true, getScreen()));
        this.musicOrderButton = addRenderWidget(new SortButton.OrderTypeButton(getStartX() + 271, getStartY() + 189, n -> updateList(), true, getScreen()));
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(PLAY_LIST_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        OERenderUtil.renderTextureSprite(PLAY_LIST_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
        renderSmartButtonSprite(poseStack, multiBufferSource, 1, 189, OERenderUtil.MIN_BREADTH * 2, 72, 9, i, j, onPxW, onPxH, monitorHeight, new TranslatableComponent("imp.button.addPlaylist"), WIDGETS_TEXTURE, 73, 14, 5, 5, 256, 256);
        renderSmartButtonSprite(poseStack, multiBufferSource, 102, 189, OERenderUtil.MIN_BREADTH * 2, 72, 9, i, j, onPxW, onPxH, monitorHeight, new TranslatableComponent("imp.button.addMusic"), WIDGETS_TEXTURE, 73, 14, 5, 5, 256, 256);
    }

    @Override
    public void tick() {
        super.tick();
        if (musicPlayListsCash != getSyncManager().getMyPlayList()) {
            musicPlayListsCash = getSyncManager().getMyPlayList();
            updateList();
        }
    }

    private void updateList() {
        musicPlayLists.clear();
        if (musicPlayListsCash != null)
            musicPlayLists.addAll(playlistSortButton.sort(musicPlayListsCash, playlistOrderButton));

        if (!musicPlayLists.contains(selectedMusicPlayList))
            selectedMusicPlayList = null;
    }

}
