package dev.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.PlayListFixedButtonsList;
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
    private SortButton.SortTypeButton playlistSortButton;
    private SortButton.OrderTypeButton playlistOrderButton;

    public PlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen, int leftPos, int topPos) {
        super(type, screen, leftPos, topPos);
    }

    @Override
    public void init() {
        super.init();
        addRenderWidget(new PlayListFixedButtonsList(getStartX() + 1, getStartY() + 20, 100, 168, 8, new TranslatableComponent("imp.fixedList.myPlaylist"), musicPlayLists, (fixedButtonsList, playList, i, i1) -> {
            System.out.println("test");
        }));

        this.playlistSortButton = addRenderWidget(new SortButton.SortTypeButton(getStartX() + 73, getStartY() + 189, n -> {
            updateList();
        }, false, getScreen()));

        this.playlistOrderButton = addRenderWidget(new SortButton.OrderTypeButton(getStartX() + 82, getStartY() + 189, n -> {
            updateList();
        }, false, getScreen()));
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
            musicPlayLists.addAll(musicPlayListsCash.stream().sorted(playlistSortButton.getSortType().getComparator(playlistOrderButton.getOrderType())).toList());
    }

}
