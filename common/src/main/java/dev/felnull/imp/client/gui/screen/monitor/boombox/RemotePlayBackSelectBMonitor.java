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
