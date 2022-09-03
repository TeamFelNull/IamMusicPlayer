package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PlayBackControlWidget extends AbstractWidget implements IIMPSmartRender {
    private final Supplier<StateType> stateTypeSupplier;
    private final Consumer<StateType> press;

    public PlayBackControlWidget(int x, int y, Supplier<StateType> stateTypeSupplier, Consumer<StateType> press) {
        super(x, y, 10, 10, Component.translatable("imp.widget.playBackControl"));
        this.stateTypeSupplier = stateTypeSupplier;
        this.press = press;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

    @Override
    public void renderButton(PoseStack poseStack, int i, int j, float f) {
        var state = stateTypeSupplier.get();
        int z = isHoveredOrFocused() ? 30 : 0;
        OERenderUtils.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, x, y, state.ordinal() * 10 + z, 145, 10, 10);
    }

    @Override
    public void onClick(double d, double e) {
        super.onClick(d, e);
        press.accept(stateTypeSupplier.get());
    }

    public static enum StateType {
        PLAYING, STOP, PAUSE;
    }
}
