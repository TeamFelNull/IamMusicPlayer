package dev.felnull.imp.client.gui.screen.monitor.cassette_deck;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.CassetteDeckBlockEntity;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.components.WriteMusicsFixedButtonsList;
import dev.felnull.imp.client.gui.components.WritePlayListFixedButtonsList;
import dev.felnull.imp.client.gui.screen.CassetteDeckScreen;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.util.IMPItemUtil;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WriteCDMonitor extends CassetteDeckMonitor {
    protected static final ResourceLocation WRITE_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/cassette_deck/monitor/write.png");
    private SmartButton backButton;
    private SmartButton writeButton;
    private final List<MusicPlayList> musicPlayLists = new ArrayList<>();
    private final List<Music> musics = new ArrayList<>();
    private List<MusicPlayList> musicPlayListsCash;
    private List<Music> musicsCash;

    public WriteCDMonitor(CassetteDeckBlockEntity.MonitorType monitorType, CassetteDeckScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.backButton = this.addRenderWidget(new SmartButton(getStartX() + 1, getStartY() + 44, 14, 11, new TranslatableComponent("imp.button.back"), n -> insMonitor(CassetteDeckBlockEntity.MonitorType.MENU)));
        this.backButton.setHideText(true);
        this.backButton.setIcon(MusicManagerMonitor.WIDGETS_TEXTURE, 11, 123, 8, 8);

        this.writeButton = this.addRenderWidget(new SmartButton(getStartX() + 164, getStartY() + 44, 35, 11, new TranslatableComponent("imp.button.writeStart"), n -> insMonitor(CassetteDeckBlockEntity.MonitorType.WRITE_EXECUTION)));
        this.writeButton.setHideText(true);
        this.writeButton.setIcon(MusicManagerMonitor.WIDGETS_TEXTURE, 11, 131, 20, 8);
        this.writeButton.active = canWriteStart();

        this.addRenderWidget(new WritePlayListFixedButtonsList(getStartX() + 1, getStartY() + 1, musicPlayLists, (fixedButtonsList, playList, i, i1) -> setSelectPlaylist(playList.getUuid()), n -> n.getUuid().equals(getSelectPlaylist())));

        this.addRenderWidget(new WriteMusicsFixedButtonsList(getStartX() + 70, getStartY() + 1, musics, (fixedButtonsList, music, i, i1) -> setMusic(music.getUuid()), n -> n.equals(getMusic())));
    }

    private UUID getSelectPlaylist() {
        if (getBlockEntity() instanceof CassetteDeckBlockEntity cassetteDeckBlockEntity)
            return getSelectPlaylist(cassetteDeckBlockEntity);
        return null;
    }

    private UUID getSelectPlaylist(CassetteDeckBlockEntity cassetteDeckBlockEntity) {
        return cassetteDeckBlockEntity.getMyPlayerSelectPlaylist();
    }

    private void setSelectPlaylist(UUID uuid) {
        getScreen().insSelectPlaylist(uuid);
    }

    private boolean canWriteStart() {
        var tape = getScreen().getCassetteTape();
        return !tape.isEmpty() && IMPItemUtil.isCassetteTape(tape) && getMusic() != null;
    }

    private void setMusic(UUID uuid) {
        getScreen().insMusic(uuid);
    }

    private Music getMusic() {
        if (getBlockEntity() instanceof CassetteDeckBlockEntity cassetteDeckBlockEntity)
            return getMusic(cassetteDeckBlockEntity);
        return null;
    }

    private Music getMusic(CassetteDeckBlockEntity cassetteDeckBlockEntity) {
        return cassetteDeckBlockEntity.getMusic();
    }

    @Override
    public void tick() {
        super.tick();
        this.writeButton.active = canWriteStart();

        if (musicPlayListsCash != getSyncManager().getMyPlayList()) {
            musicPlayListsCash = getSyncManager().getMyPlayList();
            updateList();
        }

        if (getSelectPlaylist() != null && musicsCash != getSyncManager().getMusics(getSelectPlaylist())) {
            musicsCash = getSyncManager().getMusics(getSelectPlaylist());
            updateMusics();
        }
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

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(WRITE_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
        if (getMusic() != null) {
            float sx = 3;
            var img = getMusic().getImage();
            if (!img.isEmpty()) {
                sx += 11 - 2 + 1;
                PlayImageRenderer.getInstance().draw(img, poseStack, getStartX() + 16, getStartY() + 44, 11);
            }
            drawSmartText(poseStack, new TextComponent(OERenderUtil.getWidthString(getMusic().getName(), 147 - sx - 2, "...")), getStartX() + 16 + sx, getStartY() + 44f + (11f - 6.5f) / 2f);
        }
    }
}
