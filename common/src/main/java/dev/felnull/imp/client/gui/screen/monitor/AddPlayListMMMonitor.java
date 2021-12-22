package dev.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.components.SortButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class AddPlayListMMMonitor extends MusicManagerMonitor {
    private static final ResourceLocation ADD_PLAY_LIST_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/add_play_list.png");
    private SmartButton createPlayListButton;
    private SmartButton addUploadedPlayListButton;
    private SortButton.SortTypeButton playlistSortButton;
    private SortButton.OrderTypeButton playlistOrderButton;

    public AddPlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.createPlayListButton = addRenderWidget(new SmartButton(getStartX() + 1, getStartY() + 189, 90, 9, new TranslatableComponent("imp.button.createPlaylist"), n -> {

        }));
        this.createPlayListButton.setIcon(WIDGETS_TEXTURE, 78, 14, 5, 5);

        this.addUploadedPlayListButton = addRenderWidget(new SmartButton(getStartX() + 91, getStartY() + 189, 122, 9, new TranslatableComponent("imp.button.addUploadedPlaylist"), n -> {

        }));
        this.addUploadedPlayListButton.setIcon(WIDGETS_TEXTURE, 83, 14, 5, 5);

        this.playlistSortButton = addRenderWidget(new SortButton.SortTypeButton(getStartX() + 213, getStartY() + 189, n -> updateList(), false, getScreen()));
        this.playlistOrderButton = addRenderWidget(new SortButton.OrderTypeButton(getStartX() + 222, getStartY() + 189, n -> updateList(), false, getScreen()));
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(ADD_PLAY_LIST_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        OERenderUtil.renderTextureSprite(ADD_PLAY_LIST_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);

        renderSmartButtonSprite(poseStack, multiBufferSource, 1, 189, OERenderUtil.MIN_BREADTH * 2, 90, 9, i, j, onPxW, onPxH, monitorHeight, new TranslatableComponent("imp.button.createPlaylist"), WIDGETS_TEXTURE, 78, 14, 5, 5, 256, 256);
        renderSmartButtonSprite(poseStack, multiBufferSource, 91, 189, OERenderUtil.MIN_BREADTH * 2, 122, 9, i, j, onPxW, onPxH, monitorHeight, new TranslatableComponent("imp.button.addUploadedPlaylist"), WIDGETS_TEXTURE, 83, 14, 5, 5, 256, 256);

        renderSmartButtonSprite(poseStack, multiBufferSource, 213, 189, OERenderUtil.MIN_BREADTH * 2, 9, 9, i, j, onPxW, onPxH, monitorHeight, WIDGETS_TEXTURE, 73, 0, 7, 7, 256, 256);
        renderSmartButtonSprite(poseStack, multiBufferSource, 222, 189, OERenderUtil.MIN_BREADTH * 2, 9, 9, i, j, onPxW, onPxH, monitorHeight, WIDGETS_TEXTURE, 80, 7, 7, 7, 256, 256);
    }

    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.PLAY_LIST;
    }

    private void updateList() {

    }
}
