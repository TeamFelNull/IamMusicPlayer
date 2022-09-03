package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public abstract class DeleteBaseMMMonitor extends MusicManagerMonitor {
    private static final Component CANCEL_TEXT = Component.translatable("gui.cancel");
    private static final Component DELETE_TEXT = Component.translatable("imp.button.delete").withStyle(ChatFormatting.DARK_RED);
    private Component WARING;
    private String cashName;

    public DeleteBaseMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.WARING = Component.translatable("imp.text.deleteWarning", getWaringName());
        this.cashName = getWaringName();

        this.addRenderWidget(new SmartButton(getStartX() + 5, getStartY() + 180, 177, 15, DELETE_TEXT, n -> {
            onDelete();
            insMonitor(MusicManagerBlockEntity.MonitorType.PLAY_LIST);
        }));

        this.addRenderWidget(new SmartButton(getStartX() + width - 5 - 177, getStartY() + 180, 177, 15, CANCEL_TEXT, n -> {
            var pare = getParentType();
            if (pare == null)
                pare = MusicManagerBlockEntity.MonitorType.PLAY_LIST;
            insMonitor(pare);
        }));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.cashName.equals(getWaringName())) {
            this.cashName = getWaringName();
            this.WARING = Component.translatable("imp.text.deleteWarning", this.cashName);
        }
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        poseStack.pushPose();
        float sc = Math.min(1f, (width - 20f) / (float) mc.font.width(WARING));
        OERenderUtils.poseScaleAll(poseStack, sc);
        drawSmartCenterText(poseStack, WARING, (getStartX() + (float) width / 2f) / sc, (getStartY() + (float) height / 2f - 16.5f) / sc);
        poseStack.popPose();
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;

        var wrtx = Component.translatable("imp.text.deleteWarning", getWaringName(blockEntity));
        float sc = Math.min(1f, (width - 20f) / (float) mc.font.width(wrtx));
        renderSmartCenterTextSprite(poseStack, multiBufferSource, wrtx, (float) width / 2f, (float) height / 2f - 16.5f, OERenderUtils.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, sc, i);

        renderSmartButtonSprite(poseStack, multiBufferSource, 5, 180, OERenderUtils.MIN_BREADTH * 3, 177, 15, i, j, onPxW, onPxH, monitorHeight, DELETE_TEXT, true);
        renderSmartButtonSprite(poseStack, multiBufferSource, width - 5 - 177, 180, OERenderUtils.MIN_BREADTH * 3, 177, 15, i, j, onPxW, onPxH, monitorHeight, CANCEL_TEXT, true);
    }

    abstract public void onDelete();

    public String getWaringName() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getWaringName(musicManagerBlockEntity);
        return null;
    }

    @NotNull
    abstract public String getWaringName(MusicManagerBlockEntity musicManagerBlockEntity);
}
