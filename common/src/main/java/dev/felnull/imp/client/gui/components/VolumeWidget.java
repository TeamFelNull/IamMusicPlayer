package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.value.IntValue;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.BooleanSupplier;

public class VolumeWidget extends AbstractWidget implements IIMPSmartRender {
    private final IntValue volume;
    private final BooleanSupplier mute;

    public VolumeWidget(int x, int y, IntValue volume, BooleanSupplier mute) {
        super(x, y, 30, 10, new TranslatableComponent("imp.widget.volume"));
        this.volume = volume;
        this.mute = mute;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

    @Override
    public void renderButton(PoseStack poseStack, int i, int j, float f) {
        int vol = volume.get();
        int nv = vol / 100;
        int lfs = (nv * 2) + (vol <= 0 ? 0 : 2);
        OERenderUtil.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, x, y, 0, 156, 4 + lfs, 8);
        drawSmartText(poseStack, new TextComponent(String.valueOf(vol)), x + 5 + lfs, y + 0.5f, 0XFF115D0E);
    }
}
