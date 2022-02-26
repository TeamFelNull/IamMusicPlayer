package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.value.Value;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.data.BoomboxData;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TranslatableComponent;

public class ContinuousWidget extends AbstractWidget implements IIMPSmartRender {
    private final Value<BoomboxData.ContinuousType> continuousTypeValue;

    public ContinuousWidget(int x, int y, Value<BoomboxData.ContinuousType> continuousTypeValue) {
        super(x, y, 40, 10, new TranslatableComponent("imp.widget.continuousControl"));
        this.continuousTypeValue = continuousTypeValue;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

    @Override
    public void onClick(double d, double e) {
        super.onClick(d, e);
        continuousTypeValue.accept(BoomboxData.ContinuousType.values()[(continuousTypeValue.get().ordinal() + 1) % BoomboxData.ContinuousType.values().length]);
    }

    @Override
    public void renderButton(PoseStack poseStack, int i, int j, float f) {
        drawSmartCenterText(poseStack, continuousTypeValue.get().getComponent(), x + width / 2f, y + 2, isHoveredOrFocused() ? 0XFF007F06 : 0XFF115D0E);
    }
}
