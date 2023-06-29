package dev.felnull.imp.client.gui.components;

import dev.architectury.utils.value.Value;
import dev.felnull.imp.block.BoomboxData;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class ContinuousWidget extends AbstractWidget implements IIMPSmartRender {
    private final Value<BoomboxData.ContinuousType> continuousTypeValue;
    private static final Component CONTINUOUSCONTROL = Component.translatable("imp.widget.continuousControl");

    public ContinuousWidget(int x, int y, Value<BoomboxData.ContinuousType> continuousTypeValue) {
        super(x, y, 40, 10, CONTINUOUSCONTROL);
        this.continuousTypeValue = continuousTypeValue;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

    /*@Override
    public void renderWidget(PoseStack poseStack, int i, int j, float f) {
        drawSmartCenterText(poseStack, continuousTypeValue.get().getComponent(), getX() + width / 2f, getY() + 2, isHoveredOrFocused() ? 0XFF007F06 : 0XFF115D0E);
    }*/

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        drawSmartCenterText(guiGraphics, continuousTypeValue.get().getComponent(), getX() + width / 2f, getY() + 2, isHoveredOrFocused() ? 0XFF007F06 : 0XFF115D0E);
    }

    @Override
    public void onClick(double d, double e) {
        super.onClick(d, e);
        continuousTypeValue.accept(BoomboxData.ContinuousType.values()[(continuousTypeValue.get().ordinal() + 1) % BoomboxData.ContinuousType.values().length]);
    }
}
