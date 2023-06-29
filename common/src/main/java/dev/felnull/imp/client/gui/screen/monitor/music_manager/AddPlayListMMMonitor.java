package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.components.JoinPlayListFixedListWidget;
import dev.felnull.imp.client.gui.components.MyPlayListFixedListWidget;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.components.SortButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.client.music.MusicSyncManager;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddPlayListMMMonitor extends MusicManagerMonitor {
    private static final ResourceLocation ADD_PLAY_LIST_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/add_play_list.png");
    private static final Component CREATE_PLAYLIST_TEXT = Component.translatable("imp.button.createPlaylist");
    private static final Component ONLINE_PLAYLIST_TEXT = Component.translatable("imp.button.addOnlinePlaylist");
    private final List<MusicPlayList> musicPlayLists = new ArrayList<>();
    private final Component PUBLIC_TEXT = Component.translatable("imp.text.public");
    private MusicSyncManager.PlayListInfo lastPlayListInfo;
    private Component INFO_TEXT;
    private List<MusicPlayList> musicPlayListsCash;
    private SmartButton createPlayListButton;
    private SmartButton addOnlinePlayListButton;
    private SortButton.SortTypeButton playlistSortButton;
    private SortButton.OrderTypeButton playlistOrderButton;
    private JoinPlayListFixedListWidget joinPlayListFixedButtonsList;

    public AddPlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.joinPlayListFixedButtonsList = addRenderWidget(new JoinPlayListFixedListWidget(getStartX() + 1, getStartY() + 20, musicPlayLists, (widget, item) -> {
            getScreen().insAddPlayList(item.getUuid());
            insMonitor(MusicManagerBlockEntity.MonitorType.PLAY_LIST);
        }, this.joinPlayListFixedButtonsList));

        this.createPlayListButton = addRenderWidget(new SmartButton(getStartX() + 1, getStartY() + 189, 90, 9, CREATE_PLAYLIST_TEXT, n -> insMonitor(MusicManagerBlockEntity.MonitorType.CREATE_PLAY_LIST)));
        this.createPlayListButton.setIcon(WIDGETS_TEXTURE, 78, 14, 5, 5);

        this.addOnlinePlayListButton = addRenderWidget(new SmartButton(getStartX() + 91, getStartY() + 189, 122, 9, ONLINE_PLAYLIST_TEXT, n -> insMonitor(MusicManagerBlockEntity.MonitorType.ADD_ONLINE_PLAY_LIST)));
        this.addOnlinePlayListButton.setIcon(WIDGETS_TEXTURE, 83, 14, 5, 5);
        this.addOnlinePlayListButton.active = false;

        this.playlistSortButton = addRenderWidget(new SortButton.SortTypeButton(getStartX() + 213, getStartY() + 189, n -> updateList(), false, getScreen()));
        this.playlistOrderButton = addRenderWidget(new SortButton.OrderTypeButton(getStartX() + 222, getStartY() + 189, n -> updateList(), false, getScreen()));
    }

    @Override
    public void render(GuiGraphics guiGraphics, float f, int mouseX, int mouseY) {
        super.render(guiGraphics, f, mouseX, mouseY);
        OERenderUtils.drawTexture(ADD_PLAY_LIST_TEXTURE, guiGraphics.pose(), getStartX(), getStartY(), 0f, 0f, width, height, width, height);
        if (INFO_TEXT != null)
            drawSmartText(guiGraphics, INFO_TEXT, getStartX() + width - IIMPSmartRender.mc.font.width(INFO_TEXT) - 3, getStartY() + 11);
        drawSmartText(guiGraphics, PUBLIC_TEXT, getStartX() + 3, getStartY() + 11);
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        OERenderUtils.renderTextureSprite(ADD_PLAY_LIST_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtils.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);

        renderSmartButtonSprite(poseStack, multiBufferSource, 1, 189, OERenderUtils.MIN_BREADTH * 2, 90, 9, i, j, onPxW, onPxH, monitorHeight, CREATE_PLAYLIST_TEXT, WIDGETS_TEXTURE, 78, 14, 5, 5, 256, 256);
        renderSmartButtonSprite(poseStack, multiBufferSource, 91, 189, OERenderUtils.MIN_BREADTH * 2, 122, 9, i, j, onPxW, onPxH, monitorHeight, ONLINE_PLAYLIST_TEXT, WIDGETS_TEXTURE, 83, 14, 5, 5, 256, 256, true);

        renderSmartButtonSprite(poseStack, multiBufferSource, 213, 189, OERenderUtils.MIN_BREADTH * 2, 9, 9, i, j, onPxW, onPxH, monitorHeight, WIDGETS_TEXTURE, 73, 0, 7, 7, 256, 256);
        renderSmartButtonSprite(poseStack, multiBufferSource, 222, 189, OERenderUtils.MIN_BREADTH * 2, 9, 9, i, j, onPxW, onPxH, monitorHeight, WIDGETS_TEXTURE, 80, 7, 7, 7, 256, 256);

        var pls = getSyncManager().getCanJoinPlayList();
        int plsc = 0;
        if (pls != null) {
            plsc = pls.size();
            for (int k = 0; k < Math.min(6, pls.size()); k++) {
                renderSmartButtonBoxSprite(poseStack, multiBufferSource, 1, 20 + (k * 28), OERenderUtils.MIN_BREADTH * 2, 359, 28, i, j, onPxW, onPxH, monitorHeight);
                var playList = pls.get(k);
                float sx = 1;
                if (!playList.getImage().isEmpty()) {
                    sx += 28 + 2;
                    PlayImageRenderer.getInstance().renderSprite(playList.getImage(), poseStack, multiBufferSource, 3 * onPxW, monitorHeight - (20 + (k * 28) + 2 + 26) * onPxH, OERenderUtils.MIN_BREADTH * 4, 26 * onPxH, i, j);
                }

                renderSmartTextSprite(poseStack, multiBufferSource, Component.literal(playList.getName()), sx + 3, 20 + (k * 28) + 5, OERenderUtils.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
                renderSmartTextSprite(poseStack, multiBufferSource, Component.literal(MyPlayListFixedListWidget.dateFormat.format(new Date(playList.getCreateDate()))), sx + 3, 20 + (k * 28) + 18, OERenderUtils.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);

                poseStack.pushPose();
                poseStack.translate((sx + 101) * onPxW, monitorHeight - (20 + (k * 28) + 2 + 9) * onPxH, OERenderUtils.MIN_BREADTH * 4);
                OERenderUtils.renderPlayerFaceSprite(poseStack, multiBufferSource, playList.getAuthority().getOwnerName(), onPxH * 9, i, j);
                poseStack.popPose();

                renderSmartTextSprite(poseStack, multiBufferSource, Component.literal(playList.getAuthority().getOwnerName()), sx + 114, 20 + (k * 28) + 5, OERenderUtils.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
                renderSmartTextSprite(poseStack, multiBufferSource, Component.translatable("imp.text.musicCount", playList.getMusicList().size()), sx + 101, 20 + (k * 28) + 18, OERenderUtils.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
                renderSmartTextSprite(poseStack, multiBufferSource, Component.translatable("imp.text.playerCount", playList.getPlayerCount()), sx + 156, 20 + (k * 28) + 18, OERenderUtils.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);

                if (playList.getAuthority().getAuthorityType(IIMPSmartRender.mc.player.getGameProfile().getId()).isInvitation()) {
                    renderSmartTextSpriteColorSprite(poseStack, multiBufferSource, Component.translatable("imp.text.invitation"), sx + 208, 20 + (k * 28) + 5, OERenderUtils.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, 0xFF0000FF, i);
                }
            }
        }
        renderScrollbarSprite(poseStack, multiBufferSource, 360, 20, OERenderUtils.MIN_BREADTH * 2, 168, i, j, onPxW, onPxH, monitorHeight, plsc, 6);

        updateInfoText();
        if (INFO_TEXT != null)
            renderSmartTextSprite(poseStack, multiBufferSource, INFO_TEXT, width - IIMPSmartRender.mc.font.width(INFO_TEXT) - 3, 11, OERenderUtils.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, i);
        renderSmartTextSprite(poseStack, multiBufferSource, PUBLIC_TEXT, 3, 11, OERenderUtils.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, i);
    }

    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.PLAY_LIST;
    }

    @Override
    public void tick() {
        super.tick();
        if (musicPlayListsCash != getSyncManager().getCanJoinPlayList()) {
            musicPlayListsCash = getSyncManager().getCanJoinPlayList();
            updateList();
        }
        updateInfoText();
    }

    private void updateList() {
        musicPlayLists.clear();
        if (musicPlayListsCash != null)
            musicPlayLists.addAll(playlistSortButton.sort(musicPlayListsCash, playlistOrderButton));
    }

    private void updateInfoText() {
        var pls = getSyncManager();
        if (pls.getCanJoinPlayListInfo() != lastPlayListInfo) {
            lastPlayListInfo = pls.getCanJoinPlayListInfo();
            if (lastPlayListInfo != null) {
                INFO_TEXT = Component.translatable("imp.text.addPlaylistInfo", lastPlayListInfo.playListCount(), lastPlayListInfo.playerCount(), lastPlayListInfo.musicCount());
            } else {
                INFO_TEXT = null;
            }
        }
    }
}
