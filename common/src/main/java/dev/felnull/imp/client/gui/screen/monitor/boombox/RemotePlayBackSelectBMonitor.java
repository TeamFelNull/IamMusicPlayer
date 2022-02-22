package dev.felnull.imp.client.gui.screen.monitor.boombox;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.gui.components.RemoteMusicsFixedButtonsList;
import dev.felnull.imp.client.gui.components.RemotePlayListFixedButtonsList;
import dev.felnull.imp.client.gui.screen.BoomboxScreen;
import dev.felnull.imp.data.BoomboxData;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RemotePlayBackSelectBMonitor extends BoomboxMonitor {
    protected static final ResourceLocation REMOTE_PLAYBACK_SELECT_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox/monitor/remote_playback_select.png");
    private final List<MusicPlayList> musicPlayLists = new ArrayList<>();
    private final List<Music> musics = new ArrayList<>();
    private List<MusicPlayList> musicPlayListsCash;
    private List<Music> musicsCash;

    public RemotePlayBackSelectBMonitor(BoomboxData.MonitorType monitorType, BoomboxScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.addRenderWidget(new RemotePlayListFixedButtonsList(getStartX() + 1, getStartY() + 1, musicPlayLists, (fixedButtonsList, playList, i, i1) -> setSelectPlaylist(playList.getUuid()), n -> n.getUuid().equals(getSelectPlaylist())));

        this.addRenderWidget(new RemoteMusicsFixedButtonsList(getStartX() + 70, getStartY() + 1, musics, (fixedButtonsList, music, i, i1) -> {
            setMusic(music.getUuid());
            setMonitor(BoomboxData.MonitorType.REMOTE_PLAYBACK);
        }));
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(REMOTE_PLAYBACK_SELECT_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
    }

    @Override
    public void renderAppearance(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight, BoomboxData data) {
        super.renderAppearance(poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight, data);
        OERenderUtil.renderTextureSprite(REMOTE_PLAYBACK_SELECT_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;

        renderFixedListSprite(poseStack, multiBufferSource, 1, 1, OERenderUtil.MIN_BREADTH * 2f, 68, 35, i, j, onPxW, onPxH, monitorHeight, getSyncManager().getMyPlayList(), 5, (poseStack1, multiBufferSource1, x, y, z, w, h, i1, j1, entry) -> {
            renderSmartButtonBoxSprite(poseStack1, multiBufferSource1, x, y, z + OERenderUtil.MIN_BREADTH, w, h, i1, j1, onPxW, onPxH, monitorHeight, entry.getUuid().equals(getSelectPlaylist(data)));
            float sx = 1;
            var img = entry.getImage();
            if (!img.isEmpty()) {
                sx += h - 2 + 1;
                renderPlayListImage(poseStack1, multiBufferSource1, img, x + 1f, y + 1f, z + OERenderUtil.MIN_BREADTH * 3, h - 2, i1, j1, onPxW, onPxH, monitorHeight);
            }
            renderSmartTextSprite(poseStack1, multiBufferSource1, new TextComponent(OERenderUtil.getWidthString(entry.getName(), w - sx - 2 + 20, "...")), x + sx, y + 0.25f, z + OERenderUtil.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, getDefaultRenderTextScale() * 0.7f, i1);
        });

        renderFixedListSprite(poseStack, multiBufferSource, 70, 1, OERenderUtil.MIN_BREADTH * 2f, 129, 35, i, j, onPxW, onPxH, monitorHeight, getSyncManager().getMusics(getSelectPlaylist(data)), 5, (poseStack12, multiBufferSource12, x, y, z, w, h, i12, j12, entry) -> {
            renderSmartButtonBoxSprite(poseStack12, multiBufferSource12, x, y, z + OERenderUtil.MIN_BREADTH, w, h, i12, j12, onPxW, onPxH, monitorHeight, false);
            float sx = 1;
            var img = entry.getImage();
            if (!img.isEmpty()) {
                sx += h - 2 + 1;
                renderPlayListImage(poseStack12, multiBufferSource12, img, x + 1f, y + 1f, z + OERenderUtil.MIN_BREADTH * 3, h - 2, i12, j12, onPxW, onPxH, monitorHeight);
            }
            renderSmartTextSprite(poseStack12, multiBufferSource12, new TextComponent(OERenderUtil.getWidthString(entry.getName(), w - sx - 2 + 20, "...")), x + sx, y + 0.25f, z + OERenderUtil.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, getDefaultRenderTextScale() * 0.7f, i12);
        });
    }

    @Override
    public void tick() {
        super.tick();

        if (musicPlayListsCash != getSyncManager().getMyPlayList()) {
            musicPlayListsCash = getSyncManager().getMyPlayList();
            updateList();
        }

        if (getSelectPlaylist() != null && musicsCash != getSyncManager().getMusics(getSelectPlaylist())) {
            musicsCash = getSyncManager().getMusics(getSelectPlaylist());
            updateMusics();
        }
    }

    private void setMusic(UUID musicId) {
        getScreen().insSelectedMusic(musicId);
    }

    private Music getMusic() {
        return getScreen().getBoomBoxData().getSelectedMusic();
    }

    private UUID getSelectPlaylist(BoomboxData data) {
        return data.getSelectedPlayList(mc.player);
    }

    private UUID getSelectPlaylist() {
        return getScreen().getBoomBoxData().getSelectedPlayList(mc.player);
    }

    private void updateList() {
        musicPlayLists.clear();
        if (musicPlayListsCash != null)
            musicPlayLists.addAll(musicPlayListsCash);
    }

    private void updateMusics() {
        musics.clear();
        if (musicsCash != null)
            musics.addAll(musicsCash);
    }

    private void setSelectPlaylist(UUID playlist) {
        getScreen().insSelectedPlayList(playlist);
    }
}
