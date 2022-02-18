package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.value.IntValue;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.util.OEClientUtil;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;

import java.util.function.BooleanSupplier;

public class VolumeWidget extends AbstractWidget implements IIMPSmartRender {
    private final IntValue volume;
    private final BooleanSupplier mute;
    private final BooleanConsumer setMute;

    public VolumeWidget(int x, int y, IntValue volume, BooleanSupplier mute, BooleanConsumer setMute) {
        super(x, y, 30, 10, new TranslatableComponent("imp.widget.volume"));
        this.volume = volume;
        this.mute = mute;
        this.setMute = setMute;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

    @Override
    public void renderButton(PoseStack poseStack, int i, int j, float f) {
        int vol = volume.get();
        boolean imute = mute.getAsBoolean();
        int nv = imute ? 3 : vol / 100;
        int lfs = (nv * 2) + (vol <= 0 ? 0 : 2);
        int z = isHovered() ? 20 : 0;
        OERenderUtil.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, x, y, z, 156, imute ? 4 : 4 + lfs, 8);
        if (imute)
            OERenderUtil.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, x + 3, y, 12 + z, 156, 8, 8);
        drawSmartText(poseStack, new TextComponent(String.valueOf(vol)), x + 5 + lfs, y + 0.5f, isHovered() ? 0XFF007F06 : 0XFF115D0E);
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
        if (OEClientUtil.isKeyInput(mc.options.keyShift))
            mv *= 10;
        if (OEClientUtil.isKeyInput(mc.options.keySprint))
            mv *= 10;
        int an = (int) (mv * f);
        volume.accept(Mth.clamp(volume.get() + an, 0, 300));
        return true;
    }
}
