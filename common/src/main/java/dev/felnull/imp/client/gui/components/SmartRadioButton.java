package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.gui.components.RadioButton;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SmartRadioButton extends RadioButton implements IIMPSmartRender {
    public SmartRadioButton(int x, int y, int w, int h, Component title, boolean selected, boolean showLabel, Supplier<RadioButton[]> group, Consumer<RadioButton> onPress) {
        super(x, y, w, h, title, selected, showLabel, group, MusicManagerMonitor.WIDGETS_TEXTURE, 18, 65, 20, 20, 256, 256, onPress);
    }

    @Override
    public void drawRdoString(PoseStack poseStack, Component component, int i, int j, int k) {
        drawSmartText(poseStack, component, i, j);
    }
}
