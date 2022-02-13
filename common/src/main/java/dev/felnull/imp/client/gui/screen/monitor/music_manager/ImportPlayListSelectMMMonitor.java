package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.Nullable;

public class ImportPlayListSelectMMMonitor extends MusicManagerMonitor {
    private static final Component IMPORT_YOUTUBE_PLAYLIST_TEXT = new TranslatableComponent("imp.button.importYoutubePlayList");

    public ImportPlayListSelectMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        addRenderWidget(new SmartButton(getStartX() + (width - 270) / 2, getStartY() + (height - 15) / 2, 270, 15, IMPORT_YOUTUBE_PLAYLIST_TEXT, n -> insMonitor(MusicManagerBlockEntity.MonitorType.IMPORT_YOUTUBE_PLAY_LIST)));
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        renderSmartButtonSprite(poseStack, multiBufferSource, (width - 270f) / 2f, (height - 15f) / 2f, OERenderUtil.MIN_BREADTH * 2, 270, 15, i, j, onPxW, onPxH, monitorHeight, IMPORT_YOUTUBE_PLAYLIST_TEXT, true);
    }

    @Override
    protected @Nullable MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.CREATE_PLAY_LIST;
    }
}
