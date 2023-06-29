package dev.felnull.imp.client.gui.components;

import dev.architectury.utils.value.BooleanValue;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class LoopControlWidget extends AbstractWidget {
    private final BooleanValue loop;

    public LoopControlWidget(int x, int y, BooleanValue loop) {
        super(x, y, 8, 7, Component.translatable("imp.widget.loopControl"));
        this.loop = loop;
    }

    /*@Override
    public void renderWidget(PoseStack poseStack, int i, int j, float f) {
        boolean il = loop.get();
        int z = isHoveredOrFocused() ? 16 : 0;
        OERenderUtils.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, getX(), getY(), (il ? 8 : 0) + z, 164, 8, 7);
    }*/

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        boolean il = loop.get();
        int z = isHoveredOrFocused() ? 16 : 0;
        OERenderUtils.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, guiGraphics.pose(), getX(), getY(), (il ? 8 : 0) + z, 164, 8, 7);
    }

    @Override
    public void onClick(double d, double e) {
        super.onClick(d, e);
        loop.accept(!loop.get());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }
}
