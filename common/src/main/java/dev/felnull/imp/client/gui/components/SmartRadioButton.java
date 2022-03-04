package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.gui.TextureSpecifyLocation;
import dev.felnull.otyacraftengine.client.gui.components.RadioButtonV2;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SmartRadioButton extends RadioButtonV2 implements IIMPSmartRender {
    private static final TextureSpecifyLocation SRDIO_TEXTURE = new TextureSpecifyLocation(MusicManagerMonitor.WIDGETS_TEXTURE, 18, 65, 20, 20);

    public SmartRadioButton(int x, int y, @NotNull Component title, @Nullable Consumer<RadioButtonV2> onPress, @NotNull Supplier<Set<RadioButtonV2>> group) {
        super(x, y, 20, 20, title, onPress, group, true, SRDIO_TEXTURE);
    }

    @Override
    public void drawTextBase(PoseStack poseStack, Component text, int x, int y, int color) {
        drawSmartText(poseStack, text, x, y);
    }
}
