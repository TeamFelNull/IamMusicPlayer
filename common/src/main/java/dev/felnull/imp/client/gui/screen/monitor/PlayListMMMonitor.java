package dev.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.music.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.components.MyPlayListFixedButtonsList;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.components.SortButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.client.music.MusicSyncManager;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayListMMMonitor extends MusicManagerMonitor {
    private static final ResourceLocation PLAY_LIST_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/play_list.png");
    private final List<MusicPlayList> musicPlayLists = new ArrayList<>();
    private List<MusicPlayList> musicPlayListsCash;
    private MusicSyncManager.PlayListInfo lastPlayListInfo;
    private Component INFO_TEXT;
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
        addRenderWidget(new MyPlayListFixedButtonsList(getStartX() + 1, getStartY() + 20, musicPlayLists, (fixedButtonsList, playList, i, i1) -> {
            selectedMusicPlayList = playList;
        }, n -> n.equals(selectedMusicPlayList)));

        this.addPlaylistButton = addRenderWidget(new SmartButton(getStartX() + 1, getStartY() + 189, 72, 9, new TranslatableComponent("imp.button.addPlaylist"), n -> {
            insMonitor(MusicManagerBlockEntity.MonitorType.ADD_PLAY_LIST);
        }));
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
        if (INFO_TEXT != null)
            drawSmartText(poseStack, INFO_TEXT, getStartX() + width - IIMPSmartRender.mc.font.width(INFO_TEXT) - 3, getStartY() + 11);
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        OERenderUtil.renderTextureSprite(PLAY_LIST_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
        renderSmartButtonSprite(poseStack, multiBufferSource, 1, 189, OERenderUtil.MIN_BREADTH * 2, 72, 9, i, j, onPxW, onPxH, monitorHeight, new TranslatableComponent("imp.button.addPlaylist"), WIDGETS_TEXTURE, 73, 14, 5, 5, 256, 256);
        renderSmartButtonSprite(poseStack, multiBufferSource, 102, 189, OERenderUtil.MIN_BREADTH * 2, 72, 9, i, j, onPxW, onPxH, monitorHeight, new TranslatableComponent("imp.button.addMusic"), WIDGETS_TEXTURE, 73, 14, 5, 5, 256, 256);

        renderSmartButtonSprite(poseStack, multiBufferSource, 73, 189, OERenderUtil.MIN_BREADTH * 2, 9, 9, i, j, onPxW, onPxH, monitorHeight, WIDGETS_TEXTURE, 73, 0, 7, 7, 256, 256);
        renderSmartButtonSprite(poseStack, multiBufferSource, 82, 189, OERenderUtil.MIN_BREADTH * 2, 9, 9, i, j, onPxW, onPxH, monitorHeight, WIDGETS_TEXTURE, 80, 7, 7, 7, 256, 256);

        renderSmartButtonSprite(poseStack, multiBufferSource, 174, 189, OERenderUtil.MIN_BREADTH * 2, 97, 9, i, j, onPxW, onPxH, monitorHeight, new TranslatableComponent("imp.sortType." + SortButton.SortType.NAME.getName()), WIDGETS_TEXTURE, 73, 0, 7, 7, 256, 256);
        renderSmartButtonSprite(poseStack, multiBufferSource, 271, 189, OERenderUtil.MIN_BREADTH * 2, 88, 9, i, j, onPxW, onPxH, monitorHeight, new TranslatableComponent("imp.orderType." + SortButton.OrderType.DESCENDING.getName()), WIDGETS_TEXTURE, 80, 7, 7, 7, 256, 256);

        var pls = getSyncManager().getMyPlayList();
        int plsc = 0;
        if (pls != null) {
            plsc = pls.size();
            for (int k = 0; k < Math.min(8, pls.size()); k++) {
                renderSmartButtonBoxSprite(poseStack, multiBufferSource, 1, 20 + (k * 21), OERenderUtil.MIN_BREADTH * 2, 90, 21, i, j, onPxW, onPxH, monitorHeight);
                var playList = pls.get(k);
                float sx = 1;
                if (!playList.getImage().isEmpty()) {
                    sx += 18 + 2;
                    PlayImageRenderer.getInstance().renderSprite(playList.getImage(), poseStack, multiBufferSource, 3 * onPxW, monitorHeight - (20 + (k * 21) + 2 + 17) * onPxH, OERenderUtil.MIN_BREADTH * 4, 17 * onPxH, i, j);
                }

                renderSmartTextSprite(poseStack, multiBufferSource, new TextComponent(playList.getName()), sx + 3, 20 + (k * 21) + 3, OERenderUtil.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
                renderSmartTextSprite(poseStack, multiBufferSource, new TextComponent(MyPlayListFixedButtonsList.dateFormat.format(new Date(playList.getCreateDate()))), sx + 3, 20 + (k * 21) + 12, OERenderUtil.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, 0.7f, i);
            }
        }
        renderScrollbarSprite(poseStack, multiBufferSource, 92, 20, OERenderUtil.MIN_BREADTH * 2, 168, i, j, onPxW, onPxH, monitorHeight, plsc, 8);

        updateInfoText();
        if (INFO_TEXT != null)
            renderSmartTextSprite(poseStack, multiBufferSource, INFO_TEXT, width - IIMPSmartRender.mc.font.width(INFO_TEXT) - 3, 11, OERenderUtil.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, i);
    }

    @Override
    public void tick() {
        super.tick();
        if (musicPlayListsCash != getSyncManager().getMyPlayList()) {
            musicPlayListsCash = getSyncManager().getMyPlayList();
            updateList();
        }
        updateInfoText();
    }

    private void updateList() {
        musicPlayLists.clear();
        if (musicPlayListsCash != null)
            musicPlayLists.addAll(playlistSortButton.sort(musicPlayListsCash, playlistOrderButton));

        if (!musicPlayLists.contains(selectedMusicPlayList))
            selectedMusicPlayList = null;
    }

    private void updateInfoText() {
        var pls = getSyncManager();
        if (pls.getMyPlayListInfo() != lastPlayListInfo) {
            lastPlayListInfo = pls.getMyPlayListInfo();
            if (lastPlayListInfo != null) {
                INFO_TEXT = new TranslatableComponent("imp.text.playlistInfo", lastPlayListInfo.playListCount(), lastPlayListInfo.musicCount());
            } else {
                INFO_TEXT = null;
            }
        }
    }
}
