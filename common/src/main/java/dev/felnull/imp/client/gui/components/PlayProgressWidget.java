package dev.felnull.imp.client.gui.components;

import dev.architectury.utils.value.FloatValue;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class PlayProgressWidget extends AbstractWidget {
    private final FloatValue progress;

    public PlayProgressWidget(int x, int y, int w, FloatValue progress) {
        super(x, y, w, 3, Component.translatable("imp.widget.playProgressControl"));
        this.progress = progress;
    }

  /*  @Override
    public void renderWidget(PoseStack poseStack, int i, int j, float f) {
        int z = isHoveredOrFocused() ? 6 : 0;

        OERenderUtils.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, getX(), getY(), 58, 81 + z, width, 3);
        OERenderUtils.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, getX(), getY(), 58, 78 + z, (float) width * progress.get(), 3);
    }*/

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        int z = isHoveredOrFocused() ? 6 : 0;

        OERenderUtils.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, guiGraphics.pose(), getX(), getY(), 58, 81 + z, width, 3);
        OERenderUtils.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, guiGraphics.pose(), getX(), getY(), 58, 78 + z, (float) width * progress.get(), 3);
    }

    @Override
    public void onClick(double d, double e) {
        super.onClick(d, e);
        progress.accept(Mth.clamp((float) (d - getX()) / (float) width, 0, 1f));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }
}
