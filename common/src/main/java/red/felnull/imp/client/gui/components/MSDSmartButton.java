package red.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class MSDSmartButton extends Button implements IMSDSmartRender {
    public MSDSmartButton(int i, int j, int k, int l, Component component, OnPress onPress, OnTooltip onTooltip) {
        super(i, j, k, l, component, onPress, onTooltip);
    }

    public MSDSmartButton(int i, int j, int k, int l, Component component, OnPress onPress) {
        super(i, j, k, l, component, onPress);
    }

    @Override
    public void renderButton(PoseStack poseStack, int mx, int my, float parTick) {
        int k = this.getYImage(this.isHovered());
        drawSmartButtonBox(poseStack, x, y, width, height, k);
        this.renderBg(poseStack, getMinecraft(), mx, my);
        drawPrettyCenteredString(poseStack, (MutableComponent) this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, 0);
    }

}
