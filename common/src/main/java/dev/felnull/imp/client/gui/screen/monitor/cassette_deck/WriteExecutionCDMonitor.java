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
        this.backButton = this.addRenderWidget(new SmartButton(getStartX() + 1, getStartY() + 44, 14, 11, new TranslatableComponent("imp.button.back"), n -> insMonitor(CassetteDeckBlockEntity.MonitorType.WRITE)));
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

    private float getWritePar() {
        if (getBlockEntity() instanceof CassetteDeckBlockEntity cassetteDeckBlockEntity)
            return (float) cassetteDeckBlockEntity.getCassetteWriteProgress() / (float) cassetteDeckBlockEntity.getCassetteWriteProgressAll();
        return 0;
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
