package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.value.BooleanValue;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TranslatableComponent;

public class LoopControlWidget extends AbstractWidget {
    private final BooleanValue loop;

    public LoopControlWidget(int x, int y, BooleanValue loop) {
        super(x, y, 8, 7, new TranslatableComponent("imp.widget.loopControl"));
        this.loop = loop;
    }

    @Override
    public void renderButton(PoseStack poseStack, int i, int j, float f) {
        boolean il = loop.get();
        int z = isHoveredOrFocused() ? 16 : 0;
        OERenderUtil.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, x, y, (il ? 8 : 0) + z, 164, 8, 7);
    }

    @Override
    public void onClick(double d, double e) {
        super.onClick(d, e);
        loop.accept(!loop.get());
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }
}
