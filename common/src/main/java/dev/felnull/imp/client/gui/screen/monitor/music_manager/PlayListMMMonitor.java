package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.components.MusicsFixedButtonsList;
import dev.felnull.imp.client.gui.components.MyPlayListFixedButtonsList;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.components.SortButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.client.music.MusicSyncManager;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.otyacraftengine.client.util.OEClientUtil;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class PlayListMMMonitor extends MusicManagerMonitor {
    private static final ResourceLocation PLAY_LIST_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/play_list.png");
    private static final Component ADD_PLAYLIST_TEXT = new TranslatableComponent("imp.button.addPlaylist");
    private static final Component ADD_MUSIC_TEXT = new TranslatableComponent("imp.button.addMusic");
    private static final Component SORT_TYPE_NAME_TEXT = new TranslatableComponent("imp.sortType." + SortButton.SortType.NAME.getName());
    private static final Component ORDER_TYPE_DESCENDING_TEXT = new TranslatableComponent("imp.orderType." + SortButton.OrderType.DESCENDING.getName());
    private static final Component DETAIL_TEXT = new TranslatableComponent("imp.button.detailPlaylist");
    private final List<MusicPlayList> musicPlayLists = new ArrayList<>();
    private final List<Music> musics = new ArrayList<>();
    private List<MusicPlayList> musicPlayListsCash;
    private List<Music> musicsCash;
    private MusicSyncManager.PlayListInfo lastPlayListInfo;
    private Component INFO_TEXT;
    private SortButton.SortTypeButton playlistSortButton;
    private SortButton.OrderTypeButton playlistOrderButton;
    private SortButton.SortTypeButton musicSortButton;
    private SortButton.OrderTypeButton musicOrderButton;
    private SmartButton addPlaylistButton;
    private SmartButton addMusic;
    private SmartButton detailButton;

    public PlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        addRenderWidget(new MyPlayListFixedButtonsList(getStartX() + 1, getStartY() + 20, musicPlayLists, (fixedButtonsList, playList, i, i1) -> {
            setSelectedPlayList(playList.getUuid());
        }, n -> n.getUuid().equals(getSelectedPlayList())));

        this.addPlaylistButton = addRenderWidget(new SmartButton(getStartX() + 1, getStartY() + 189, 72, 9, ADD_PLAYLIST_TEXT, n -> {
            insMonitor(MusicManagerBlockEntity.MonitorType.ADD_PLAY_LIST);
        }));
        this.addPlaylistButton.setIcon(WIDGETS_TEXTURE, 73, 14, 5, 5);

        this.addMusic = addRenderWidget(new SmartButton(getStartX() + 102, getStartY() + 189, 72, 9, ADD_MUSIC_TEXT, n -> {
            insMonitor(MusicManagerBlockEntity.MonitorType.ADD_MUSIC);
        }));
        this.addMusic.setIcon(WIDGETS_TEXTURE, 73, 14, 5, 5);
        this.addMusic.active = getSelectedMusicPlayList() != null && getSelectedMusicPlayList().getAuthority().getAuthorityType(IIMPSmartRender.mc.player.getGameProfile().getId()).isMoreMember();

        this.musicSortButton = addRenderWidget(new SortButton.SortTypeButton(getStartX() + 174, getStartY() + 189, n -> updateMusics(), true, getScreen()));
        this.musicOrderButton = addRenderWidget(new SortButton.OrderTypeButton(getStartX() + 271, getStartY() + 189, n -> updateMusics(), true, getScreen()));

        this.addRenderWidget(new MusicsFixedButtonsList(getStartX() + 102, getStartY() + 40, 267, 148, 4, new TranslatableComponent("imp.fixedList.musics"), musics, (fixedButtonsList, music, i, i1) -> {

        }));

        this.detailButton = this.addRenderWidget(new SmartButton(getStartX() + 336, getStartY() + 20, 33, 9, DETAIL_TEXT, n -> insMonitor(MusicManagerBlockEntity.MonitorType.DETAIL_PLAY_LIST)));
        this.detailButton.visible = getSelectedMusicPlayList() != null;

        this.playlistOrderButton = addRenderWidget(new SortButton.OrderTypeButton(getStartX() + 82, getStartY() + 189, n -> updateList(), false, getScreen()));
        this.playlistSortButton = addRenderWidget(new SortButton.SortTypeButton(getStartX() + 73, getStartY() + 189, n -> updateList(), false, getScreen()));
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(PLAY_LIST_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
        if (INFO_TEXT != null)
            drawSmartText(poseStack, INFO_TEXT, getStartX() + width - IIMPSmartRender.mc.font.width(INFO_TEXT) - 3, getStartY() + 11);

        var pl = getSelectedMusicPlayList();
        if (pl != null) {
            int plsty;
            var au = pl.getAuthority().getPlayersAuthority().entrySet().stream().sorted(Comparator.comparingInt(o -> o.getValue().getLevel())).map(Map.Entry::getKey).filter(n -> !n.equals(pl.getAuthority().getOwner())).toList();
            if (au.size() == 0) {
                OERenderUtil.drawPlayerFace(poseStack, pl.getAuthority().getOwner(), getStartX() + 328, getStartY() + 21, 7);
                plsty = 14;
            } else if (au.size() == 1) {
                OERenderUtil.drawPlayerFace(poseStack, au.get(0), getStartX() + 328, getStartY() + 21, 7);
                OERenderUtil.drawPlayerFace(poseStack, pl.getAuthority().getOwner(), getStartX() + 319, getStartY() + 21, 7);
                plsty = 20;
            } else {
                var tx = new TextComponent("+" + au.size());
                int txw = mc.font.width(tx);
                drawSmartText(poseStack, tx, getStartX() + 336 - txw - 2, getStartY() + 21);
                OERenderUtil.drawPlayerFace(poseStack, pl.getAuthority().getOwner(), getStartX() + 336 - txw - 2 - 8, getStartY() + 21, 7);
                plsty = 18 + txw;
            }

            drawSmartText(poseStack, new TextComponent(OERenderUtil.getWidthString(pl.getName(), 240 - plsty, "...")), getStartX() + 103, getStartY() + 21);
        }
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        OERenderUtil.renderTextureSprite(PLAY_LIST_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
        renderSmartButtonSprite(poseStack, multiBufferSource, 1, 189, OERenderUtil.MIN_BREADTH * 2, 72, 9, i, j, onPxW, onPxH, monitorHeight, ADD_PLAYLIST_TEXT, WIDGETS_TEXTURE, 73, 14, 5, 5, 256, 256);
        renderSmartButtonSprite(poseStack, multiBufferSource, 102, 189, OERenderUtil.MIN_BREADTH * 2, 72, 9, i, j, onPxW, onPxH, monitorHeight, ADD_MUSIC_TEXT, WIDGETS_TEXTURE, 73, 14, 5, 5, 256, 256, getSelectedPlayList(blockEntity) == null);

        renderSmartButtonSprite(poseStack, multiBufferSource, 73, 189, OERenderUtil.MIN_BREADTH * 2, 9, 9, i, j, onPxW, onPxH, monitorHeight, WIDGETS_TEXTURE, 73, 0, 7, 7, 256, 256);
        renderSmartButtonSprite(poseStack, multiBufferSource, 82, 189, OERenderUtil.MIN_BREADTH * 2, 9, 9, i, j, onPxW, onPxH, monitorHeight, WIDGETS_TEXTURE, 80, 7, 7, 7, 256, 256);

        renderSmartButtonSprite(poseStack, multiBufferSource, 174, 189, OERenderUtil.MIN_BREADTH * 2, 97, 9, i, j, onPxW, onPxH, monitorHeight, SORT_TYPE_NAME_TEXT, WIDGETS_TEXTURE, 73, 0, 7, 7, 256, 256);
        renderSmartButtonSprite(poseStack, multiBufferSource, 271, 189, OERenderUtil.MIN_BREADTH * 2, 88, 9, i, j, onPxW, onPxH, monitorHeight, ORDER_TYPE_DESCENDING_TEXT, WIDGETS_TEXTURE, 80, 7, 7, 7, 256, 256);

        var pls = getSyncManager().getMyPlayList();
        int plsc = 0;
        if (pls != null) {
            plsc = pls.size();
            for (int k = 0; k < Math.min(8, pls.size()); k++) {
                var playList = pls.get(k);
                renderSmartButtonBoxSprite(poseStack, multiBufferSource, 1, 20 + (k * 21), OERenderUtil.MIN_BREADTH * 2, 90, 21, i, j, onPxW, onPxH, monitorHeight, playList.getUuid().equals(getSelectedPlayList(blockEntity)));
                float sx = 1;
                if (!playList.getImage().isEmpty()) {
                    sx += 18 + 2;
                    PlayImageRenderer.getInstance().renderSprite(playList.getImage(), poseStack, multiBufferSource, 3 * onPxW, monitorHeight - (20 + (k * 21) + 2 + 17) * onPxH, OERenderUtil.MIN_BREADTH * 4, 17 * onPxH, i, j);
                }

                renderSmartTextSprite(poseStack, multiBufferSource, new TextComponent(OERenderUtil.getWidthString(playList.getName(), 80 - sx, "...")), sx + 3, 20 + (k * 21) + 3, OERenderUtil.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
                renderSmartTextSprite(poseStack, multiBufferSource, new TextComponent(MyPlayListFixedButtonsList.dateFormat.format(new Date(playList.getCreateDate()))), sx + 3, 20 + (k * 21) + 12, OERenderUtil.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, 0.7f, i);
            }
        }
        renderScrollbarSprite(poseStack, multiBufferSource, 92, 20, OERenderUtil.MIN_BREADTH * 2, 168, i, j, onPxW, onPxH, monitorHeight, plsc, 8);

        updateInfoText();
        if (INFO_TEXT != null)
            renderSmartTextSprite(poseStack, multiBufferSource, INFO_TEXT, width - IIMPSmartRender.mc.font.width(INFO_TEXT) - 3, 11, OERenderUtil.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, i);

        var ms = getSyncManager().getMusics(getSelectedPlayList(blockEntity));
        int msc = 0;
        if (ms != null) {
            msc = ms.size();
            for (int k = 0; k < Math.min(4, ms.size()); k++) {
                var music = ms.get(k);
                renderSmartButtonBoxSprite(poseStack, multiBufferSource, 102, 40 + (k * 37), OERenderUtil.MIN_BREADTH * 2, 267 - 10, 37, i, j, onPxW, onPxH, monitorHeight, false);
                float sx = 2;
                if (!music.getImage().isEmpty()) {
                    sx += 37 + 2;
                    PlayImageRenderer.getInstance().renderSprite(music.getImage(), poseStack, multiBufferSource, 103 * onPxW, monitorHeight - (40 + (k * 37) + 1 + 35) * onPxH, OERenderUtil.MIN_BREADTH * 4, 35 * onPxH, i, j);
                }
                var nt = new TextComponent(music.getName());
                renderSmartTextSprite(poseStack, multiBufferSource, nt, sx + 3 + 102, 40 + (k * 37) + 3, OERenderUtil.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, Math.min(1, (267f - 9f - sx - 15f) / (float) IIMPSmartRender.mc.font.width(nt)), i);

                renderSmartTextSprite(poseStack, multiBufferSource, new TextComponent(music.getAuthor()), sx + 3 + 102, 40 + (k * 37) + 14, OERenderUtil.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);

                var pname = OEClientUtil.getPlayerNameByUUID(music.getOwner()).map(n -> (Component) new TextComponent(n)).orElse(MusicsFixedButtonsList.UNKNOWN_PLAYER_TEXT);
                OERenderUtil.renderPlayerFaceSprite(poseStack, multiBufferSource, music.getOwner(), (sx + 102 + 2) * onPxW, monitorHeight - (40 + (k * 37) + 23 + 9) * onPxH, OERenderUtil.MIN_BREADTH * 4, 0, 0, 0, onPxH * 9, i, j);
                renderSmartTextSprite(poseStack, multiBufferSource, pname, sx + 3 + 102 + 12, 40 + (k * 37) + 26, OERenderUtil.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);

                renderSmartTextSprite(poseStack, multiBufferSource, new TextComponent(MyPlayListFixedButtonsList.dateFormat.format(new Date(music.getCreateDate()))), sx + 3 + 102 + 12 + 88, 40 + (k * 37) + 26, OERenderUtil.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
            }
        }
        renderScrollbarSprite(poseStack, multiBufferSource, 360, 40, OERenderUtil.MIN_BREADTH * 2, 148, i, j, onPxW, onPxH, monitorHeight, msc, 4);

        var pl = getSelectedMusicPlayList(blockEntity);
        if (pl != null) {
            renderSmartButtonSprite(poseStack, multiBufferSource, 336, 20, OERenderUtil.MIN_BREADTH * 2, 33, 9, i, j, onPxW, onPxH, monitorHeight, DETAIL_TEXT, true);
            int plsty;
            var au = pl.getAuthority().getPlayersAuthority().entrySet().stream().sorted(Comparator.comparingInt(o -> o.getValue().getLevel())).map(Map.Entry::getKey).filter(n -> !n.equals(pl.getAuthority().getOwner())).toList();
            if (au.size() == 0) {
                renderPlayerFaceSprite(poseStack, multiBufferSource, pl.getAuthority().getOwner(), 328, 20.5f, OERenderUtil.MIN_BREADTH * 2, 7, i, j, onPxW, onPxH, monitorHeight);
                plsty = 14;
            } else if (au.size() == 1) {
                renderPlayerFaceSprite(poseStack, multiBufferSource, au.get(0), 328, 20.5f, OERenderUtil.MIN_BREADTH * 2, 7, i, j, onPxW, onPxH, monitorHeight);
                renderPlayerFaceSprite(poseStack, multiBufferSource, pl.getAuthority().getOwner(), 319, 20.5f, OERenderUtil.MIN_BREADTH * 2, 7, i, j, onPxW, onPxH, monitorHeight);
                plsty = 20;
            } else {
                var tx = new TextComponent("+" + au.size());
                int txw = mc.font.width(tx);
                renderPlayerFaceSprite(poseStack, multiBufferSource, au.get(0), 336 - txw - 2 - 8, 20.5f, OERenderUtil.MIN_BREADTH * 2, 7, i, j, onPxW, onPxH, monitorHeight);
                renderSmartTextSprite(poseStack, multiBufferSource, tx, 336 - txw - 2, 22, OERenderUtil.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, i);
                plsty = 18 + txw;
            }
            renderSmartTextSprite(poseStack, multiBufferSource, new TextComponent(OERenderUtil.getWidthString(pl.getName(), 235 - plsty, "...")), 103, 22, OERenderUtil.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, i);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (musicPlayListsCash != getSyncManager().getMyPlayList()) {
            musicPlayListsCash = getSyncManager().getMyPlayList();
            updateList();
        }

        if (musicsCash != getSyncManager().getMusics(getSelectedPlayList())) {
            musicsCash = getSyncManager().getMusics(getSelectedPlayList());
            updateMusics();
        }

        updateInfoText();

        addMusic.active = getSelectedMusicPlayList() != null && getSelectedMusicPlayList().getAuthority().getAuthorityType(IIMPSmartRender.mc.player.getGameProfile().getId()).isMoreMember();

        detailButton.visible = getSelectedMusicPlayList() != null;
    }

    private void updateList() {
        musicPlayLists.clear();
        if (musicPlayListsCash != null)
            musicPlayLists.addAll(playlistSortButton.sort(musicPlayListsCash, playlistOrderButton));

        if (getSelectedMusicPlayList() == null)
            setSelectedPlayList(null);
    }

    private void updateMusics() {
        musics.clear();
        if (musicsCash != null)
            musics.addAll(musicSortButton.sort(musicsCash, musicOrderButton));
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

    public MusicPlayList getSelectedMusicPlayList(MusicManagerBlockEntity musicManagerBlockEntity) {
        var pls = getSyncManager().getMyPlayList();
        if (pls == null)
            return null;
        return getSyncManager().getMyPlayList().stream().filter(n -> n.getUuid().equals(getSelectedPlayList(musicManagerBlockEntity))).findFirst().orElse(null);
    }

    public MusicPlayList getSelectedMusicPlayList() {
        return musicPlayLists.stream().filter(n -> n.getUuid().equals(getSelectedPlayList())).findFirst().orElse(null);
    }

    public UUID getSelectedPlayList() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getSelectedPlayList(musicManagerBlockEntity);
        return null;
    }

    public UUID getSelectedPlayList(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getMySelectedPlayList();
    }

    private void setSelectedPlayList(UUID selectedPlayList) {
        getScreen().insSelectedPlayList(selectedPlayList);
    }

    @Override
    public void onUpdateSelectedPlayList(UUID playListId) {
        updateMusics();
    }
}
