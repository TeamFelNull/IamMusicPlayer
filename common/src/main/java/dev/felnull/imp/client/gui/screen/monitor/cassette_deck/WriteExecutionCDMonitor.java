package dev.felnull.imp.client.gui.screen.monitor.cassette_deck;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.fnjl.util.FNStringUtil;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.CassetteDeckBlockEntity;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.CassetteDeckScreen;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class WriteExecutionCDMonitor extends CassetteDeckMonitor {
    private static final ResourceLocation WRITE_EXECUTION_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/cassette_deck/monitor/write_execution.png");
    private static final ResourceLocation WRITE_EXECUTION_NO_IMAGE_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/cassette_deck/monitor/write_execution_no_image.png");
    private static final MutableComponent WRITING_TEXT = new TranslatableComponent("imp.text.writing");
    private SmartButton backButton;

    public WriteExecutionCDMonitor(CassetteDeckBlockEntity.MonitorType monitorType, CassetteDeckScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.backButton = this.addRenderWidget(new SmartButton(getStartX() + 1, getStartY() + 44, 14, 11, new TranslatableComponent("gui.back"), n -> insMonitor(CassetteDeckBlockEntity.MonitorType.WRITE)));
        this.backButton.setHideText(true);
        this.backButton.setIcon(MusicManagerMonitor.WIDGETS_TEXTURE, 11, 123, 8, 8);
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        if (getMusic() == null) return;
        var img = getMusic().getImage();
        OERenderUtil.drawTexture(img.isEmpty() ? WRITE_EXECUTION_NO_IMAGE_TEXTURE : WRITE_EXECUTION_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
        float sx = 22;
        if (!img.isEmpty()) {
            getPlayImageRenderer().draw(img, poseStack, getStartX() + 23, getStartY() + 6, 30);
            sx = 55;
        }
        drawSmartText(poseStack, WRITING_TEXT, getStartX() + sx, getStartY() + 5);
        drawSmartText(poseStack, new TextComponent(OERenderUtil.getWidthString(getMusic().getName(), width - sx - 3, "...")), getStartX() + sx, getStartY() + 15);

        OERenderUtil.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, getStartX() + 22, getStartY() + 41, 58, 60, 156, 9);
        float par = getWritePar();
        OERenderUtil.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, getStartX() + 22, getStartY() + 41, 58, 60 + 9, 156f * par, 9);

        drawSmartCenterText(poseStack, new TextComponent(FNStringUtil.getPercentage(par)), getStartX() + (width / 2f), getStartY() + 42);
    }

    @Override
    public void renderAppearance(CassetteDeckBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        renderSmartButtonSprite(poseStack, multiBufferSource, 1, 44, OERenderUtil.MIN_BREADTH * 2f, 14, 11, i, j, onPxW, onPxH, monitorHeight, MusicManagerMonitor.WIDGETS_TEXTURE, 11, 123, 8, 8, 256, 256);

        if (getMusic(blockEntity) == null) return;
        var img = getMusic(blockEntity).getImage();
        OERenderUtil.renderTextureSprite(img.isEmpty() ? WRITE_EXECUTION_NO_IMAGE_TEXTURE : WRITE_EXECUTION_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
        float sx = 22;
        if (!img.isEmpty()) {
            renderPlayListImage(poseStack, multiBufferSource, img, 23, 6, OERenderUtil.MIN_BREADTH * 3, 30, i, j, onPxW, onPxH, monitorHeight);
            sx = 55;
        }
        renderSmartTextSprite(poseStack, multiBufferSource, WRITING_TEXT, sx, 5, OERenderUtil.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, i);
        renderSmartTextSprite(poseStack, multiBufferSource, new TextComponent(OERenderUtil.getWidthString(getMusic(blockEntity).getName(), width - sx - 3, "...")), sx, 15, OERenderUtil.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, i);

        renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, 22, 41, OERenderUtil.MIN_BREADTH * 3, 156, 9, 58, 60, 156, 9, 256, 256, i, j, onPxW, onPxH, monitorHeight);
        float par = getWritePar(blockEntity);
        renderTextureSprite(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, multiBufferSource, 22, 41, OERenderUtil.MIN_BREADTH * 4, 156f * par, 9, 58, 60 + 9, 156f * par, 9, 256, 256, i, j, onPxW, onPxH, monitorHeight);

        renderSmartTextSprite(poseStack, multiBufferSource, new TextComponent(FNStringUtil.getPercentage(par)), (width / 2f), 43, OERenderUtil.MIN_BREADTH * 5, onPxW, onPxH, monitorHeight, i);
    }

    private float getWritePar() {
        if (getBlockEntity() instanceof CassetteDeckBlockEntity cassetteDeckBlockEntity)
            return getWritePar(cassetteDeckBlockEntity);
        return 0;
    }

    private float getWritePar(CassetteDeckBlockEntity cassetteDeckBlockEntity) {
        return (float) cassetteDeckBlockEntity.getCassetteWriteProgress() / (float) cassetteDeckBlockEntity.getCassetteWriteProgressAll();
    }

    private Music getMusic() {
        if (getBlockEntity() instanceof CassetteDeckBlockEntity cassetteDeckBlockEntity)
            return getMusic(cassetteDeckBlockEntity);
        return null;
    }

    private Music getMusic(CassetteDeckBlockEntity cassetteDeckBlockEntity) {
        return cassetteDeckBlockEntity.getMusic();
    }
}
