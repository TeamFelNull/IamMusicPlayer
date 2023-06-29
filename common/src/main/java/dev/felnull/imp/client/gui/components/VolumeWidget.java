package dev.felnull.imp.client.gui.components;

import dev.architectury.utils.value.IntValue;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.util.OEClientUtils;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.BooleanSupplier;

public class VolumeWidget extends AbstractWidget implements IIMPSmartRender {
    private final IntValue volume;
    private final BooleanSupplier mute;
    private final BooleanConsumer setMute;

    public VolumeWidget(int x, int y, IntValue volume, BooleanSupplier mute, BooleanConsumer setMute) {
        super(x, y, 30, 10, Component.translatable("imp.widget.volume"));
        this.volume = volume;
        this.mute = mute;
        this.setMute = setMute;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        int vol = volume.get();
        boolean imute = mute.getAsBoolean();
        int nv = imute ? 3 : vol / 100;
        int lfs = (nv * 2) + (vol <= 0 ? 0 : 2);
        int z = isHoveredOrFocused() ? 20 : 0;
        OERenderUtils.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, guiGraphics.pose(), getX(), getY(), z, 156, imute ? 4 : 4 + lfs, 8);
        if (imute)
            OERenderUtils.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, guiGraphics.pose(), getX() + 3, getY(), 12 + z, 156, 8, 8);
        drawSmartText(guiGraphics, Component.literal(String.valueOf(vol)), getX() + 5 + lfs, getY() + 0.5f, isHoveredOrFocused() ? 0XFF007F06 : 0XFF115D0E);
    }

    @Override
    public void onClick(double d, double e) {
        super.onClick(d, e);
        if (setMute != null)
            setMute.accept(!mute.getAsBoolean());
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f) {
        float mv = 1;
        if (OEClientUtils.isKeyInput(mc.options.keyShift))
            mv *= 10;
        if (OEClientUtils.isKeyInput(mc.options.keySprint))
            mv *= 10;
        int an = (int) (mv * f);
        volume.accept(Mth.clamp(volume.get() + an, 0, 300));
        return true;
    }
}
