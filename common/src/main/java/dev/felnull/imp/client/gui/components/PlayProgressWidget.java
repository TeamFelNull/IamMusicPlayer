package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.value.FloatValue;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;

public class PlayProgressWidget extends AbstractWidget {
    private final FloatValue progress;

    public PlayProgressWidget(int x, int y, int w, FloatValue progress) {
        super(x, y, w, 3, new TranslatableComponent("imp.widget.playProgressControl"));
        this.progress = progress;
    }

    @Override
    public void renderButton(PoseStack poseStack, int i, int j, float f) {
        int z = isHoveredOrFocused() ? 6 : 0;

        OERenderUtil.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, x, y, 58, 81 + z, width, 3);
        OERenderUtil.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, x, y, 58, 78 + z, (float) width * progress.get(), 3);
    }

    @Override
    public void onClick(double d, double e) {
        super.onClick(d, e);
        progress.accept(Mth.clamp((float) (d - x) / (float) width, 0, 1f));
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }
}
