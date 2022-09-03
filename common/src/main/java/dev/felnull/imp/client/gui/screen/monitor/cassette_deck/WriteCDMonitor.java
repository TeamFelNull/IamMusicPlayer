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
import dev.felnull.otyacraftengine.client.util.OEClientUtils;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
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
        this.backButton = this.addRenderWidget(new SmartButton(getStartX() + 1, getStartY() + 44, 14, 11, Component.translatable("gui.back"), n -> insMonitor(CassetteDeckBlockEntity.MonitorType.MENU)));
        this.backButton.setHideText(true);
        this.backButton.setIcon(MusicManagerMonitor.WIDGETS_TEXTURE, 11, 123, 8, 8);

        this.writeButton = this.addRenderWidget(new SmartButton(getStartX() + 164, getStartY() + 44, 35, 11, Component.translatable("imp.button.writeStart"), n -> insMonitor(CassetteDeckBlockEntity.MonitorType.WRITE_EXECUTION)));
        this.writeButton.setHideText(true);
        this.writeButton.setIcon(MusicManagerMonitor.WIDGETS_TEXTURE, 11, 131, 20, 8);
        this.writeButton.active = canWriteStart();

        this.addRenderWidget(new WritePlayListFixedButtonsList(getStartX() + 1, getStartY() + 1, musicPlayLists, (fixedButtonsList, playList, i, i1) -> setSelectPlaylist(playList.getUuid()), n -> n.getUuid().equals(getSelectPlaylist())));

        this.addRenderWidget(new WriteMusicsFixedButtonsList(getStartX() + 70, getStartY() + 1, musics, (fixedButtonsList, music, i, i1) -> setMusic(music.getUuid()), n -> n.equals(getMusic())));
    }

    @Override
    public void renderAppearance(CassetteDeckBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        OERenderUtils.renderTextureSprite(WRITE_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtils.MIN_BREADTH, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);

        renderSmartButtonSprite(poseStack, multiBufferSource, 1, 44, OERenderUtils.MIN_BREADTH * 2f, 14, 11, i, j, onPxW, onPxH, monitorHeight, MusicManagerMonitor.WIDGETS_TEXTURE, 11, 123, 8, 8, 256, 256);
        renderSmartButtonSprite(poseStack, multiBufferSource, 164, 44, OERenderUtils.MIN_BREADTH * 2f, 35, 11, i, j, onPxW, onPxH, monitorHeight, MusicManagerMonitor.WIDGETS_TEXTURE, 11, 131, 20, 8, 256, 256, !canWriteStart(blockEntity));

        if (getMusic(blockEntity) != null) {
            float sx = 3;
            var img = getMusic(blockEntity).getImage();
            if (!img.isEmpty()) {
                sx += 11 - 2 + 1;
                renderPlayListImage(poseStack, multiBufferSource, img, 16, 44, OERenderUtils.MIN_BREADTH * 2f, 11, i, j, onPxW, onPxH, monitorHeight);
            }
            renderSmartTextSprite(poseStack, multiBufferSource, Component.literal(OEClientUtils.getWidthOmitText(getMusic(blockEntity).getName(), 147 - sx - 2, "...")), 16 + sx, 44f + (11f - 6.5f) / 2f, OERenderUtils.MIN_BREADTH * 2f, onPxW, onPxH, monitorHeight, i);
        }

        renderFixedListSprite(poseStack, multiBufferSource, 1, 1, OERenderUtils.MIN_BREADTH * 2f, 68, 42, i, j, onPxW, onPxH, monitorHeight, getSyncManager().getMyPlayList(), 6, (poseStack1, multiBufferSource1, x, y, z, w, h, i1, j1, entry) -> {
            renderSmartButtonBoxSprite(poseStack1, multiBufferSource1, x, y, z + OERenderUtils.MIN_BREADTH, w, h, i1, j1, onPxW, onPxH, monitorHeight, entry.getUuid().equals(getSelectPlaylist(blockEntity)));
            float sx = 1;
            var img = entry.getImage();
            if (!img.isEmpty()) {
                sx += h - 2 + 1;
                renderPlayListImage(poseStack1, multiBufferSource1, img, x + 1f, y + 1f, z + OERenderUtils.MIN_BREADTH * 3, h - 2, i1, j1, onPxW, onPxH, monitorHeight);
            }
            renderSmartTextSprite(poseStack1, multiBufferSource1, Component.literal(OEClientUtils.getWidthOmitText(entry.getName(), w - sx - 2 + 20, "...")), x + sx, y + 0.25f, z + OERenderUtils.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, 0.9f, i1);
        });

        renderFixedListSprite(poseStack, multiBufferSource, 70, 1, OERenderUtils.MIN_BREADTH * 2f, 129, 42, i, j, onPxW, onPxH, monitorHeight, getSyncManager().getMusics(getSelectPlaylist(blockEntity)), 6, (poseStack12, multiBufferSource12, x, y, z, w, h, i12, j12, entry) -> {
            renderSmartButtonBoxSprite(poseStack12, multiBufferSource12, x, y, z + OERenderUtils.MIN_BREADTH, w, h, i12, j12, onPxW, onPxH, monitorHeight, getMusic(blockEntity) != null && entry.getUuid().equals(getMusic(blockEntity).getUuid()));
            float sx = 1;
            var img = entry.getImage();
            if (!img.isEmpty()) {
                sx += h - 2 + 1;
                renderPlayListImage(poseStack12, multiBufferSource12, img, x + 1f, y + 1f, z + OERenderUtils.MIN_BREADTH * 3, h - 2, i12, j12, onPxW, onPxH, monitorHeight);
            }
            renderSmartTextSprite(poseStack12, multiBufferSource12, Component.literal(OEClientUtils.getWidthOmitText(entry.getName(), w - sx - 2 + 20, "...")), x + sx, y + 0.25f, z + OERenderUtils.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, 0.9f, i12);
        });

    }

    private MusicPlayList getRawSelectPlaylist() {
        if (getBlockEntity() instanceof CassetteDeckBlockEntity cassetteDeckBlockEntity)
            return getRawSelectPlaylist(cassetteDeckBlockEntity);
        return null;
    }

    private MusicPlayList getRawSelectPlaylist(CassetteDeckBlockEntity cassetteDeckBlockEntity) {
        var pls = getSelectPlaylist(cassetteDeckBlockEntity);
        if (pls != null)
            return getSyncManager().getMyPlayList().stream().filter(n -> n.getUuid().equals(pls)).findFirst().orElse(null);
        return null;
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

    private boolean canWriteStart(CassetteDeckBlockEntity cassetteDeckBlockEntity) {
        var tape = cassetteDeckBlockEntity.getCassetteTape();
        return !tape.isEmpty() && IMPItemUtil.isCassetteTape(tape) && getMusic(cassetteDeckBlockEntity) != null;
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
        OERenderUtils.drawTexture(WRITE_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
        if (getMusic() != null) {
            float sx = 3;
            var img = getMusic().getImage();
            if (!img.isEmpty()) {
                sx += 11 - 2 + 1;
                PlayImageRenderer.getInstance().draw(img, poseStack, getStartX() + 16, getStartY() + 44, 11);
            }
            drawSmartText(poseStack, Component.literal(OEClientUtils.getWidthOmitText(getMusic().getName(), 147 - sx - 2, "...")), getStartX() + 16 + sx, getStartY() + 44f + (11f - 6.5f) / 2f);
        }
    }


}
