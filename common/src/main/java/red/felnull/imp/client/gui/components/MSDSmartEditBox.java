package red.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class MSDSmartEditBox extends EditBox implements IMSDSmartRender {
    public MSDSmartEditBox(int x, int y, int size, Component component) {
        super(Minecraft.getInstance().font, x, y, size, 15, component);
    }

    @Override
    public void renderButton(PoseStack poseStack, int i, int j, float f) {
        super.renderButton(poseStack, i, j, f);
        if (this.isVisible()) {
            drawPrettyString(poseStack, getMessage().copy(), x, y - getFont().lineHeight , 0);
        }
    }

    protected void drawTextBox(PoseStack poseStack, int x, int y, int s) {
        fillXDarkGrayLine(poseStack, x, y, s);
        fillXDarkGrayLine(poseStack, x, y + 14, s);
        fillYDarkGrayLine(poseStack, x, y + 1, 13);
        fillYDarkGrayLine(poseStack, x + s - 1, y + 1, 13);
        fillBerryDarkGray(poseStack, x + 1, y + 1, s - 2, 13);
    }

}
