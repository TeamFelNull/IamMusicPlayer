package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PlaybackProgressBar extends AbstractButton {
    private final Supplier<Float> progressGetter;
    private final Consumer<Float> playbackProgressControl;

    public PlaybackProgressBar(int x, int y, Component component, Supplier<Float> progressGetter, Consumer<Float> playbackProgressControl) {
        super(x, y, 153, 3, component);
        this.progressGetter = progressGetter;
        this.playbackProgressControl = playbackProgressControl;
    }

    @Override
    public void onClick(double d, double e) {
        super.onClick(d, e);
        if (isHovered()) {
            if (e >= y && e <= (y + getHeight()) && d >= x && d <= (x + getWidth()))
                playbackProgressControl.accept((float) ((d - x) / getWidth()));
        }
    }

    @Override
    public void onPress() {
    }

    @Override
    public void renderButton(PoseStack poseStack, int i, int j, float f) {
        OERenderUtil.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, x, y, 52, 54 + (isHovered() ? 3 : 0), getWidth(), 3);
        OERenderUtil.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, x, y, 52, 48 + (isHovered() ? 3 : 0), (float) getWidth() * progressGetter.get(), 3);
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, this.createNarrationMessage());
    }
}
