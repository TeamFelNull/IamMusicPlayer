package red.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

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
/*
        IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x, y, 0, 62 + 15 * k, 3, 3);

        IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x, y + height - 3, 0, 74 + 15 * k, 3, 3);

        IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + width - 3, y, 45, 62 + 15 * k, 3, 3);

        IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + width - 3, y + height - 3, 45, 74 + 15 * k, 3, 3);

        int w = width - 6;
        int wc = w / 42;
        for (int i = 0; i < wc; i++) {
            IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + 3 + 42 * i, y, 3, 62 + 15 * k, 42, 3);
            IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + 3 + 42 * i, y + height - 3, 3, 74 + 15 * k, 42, 3);
        }
        IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + 3 + 42 * wc, y, 3, 62 + 15 * k, w % 42, 3);
        IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + 3 + 42 * wc, y + height - 3, 3, 74 + 15 * k, w % 42, 3);


        int h = height - 6;
        int hc = h / 9;
        for (int i = 0; i < hc; i++) {
            IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x, y + 3 + 9 * i, 0, 65 + 15 * k, 3, 9);
            IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + width - 3, y + 3 + 9 * i, 45, 65 + 15 * k, 3, 9);
        }
        IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x, y + 3 + 9 * hc, 0, 65 + 15 * k, 3, h % 9);
        IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + width - 3, y + 3 + 9 * hc, 45, 65 + 15 * k, 3, h % 9);

        for (int i = 0; i < wc; i++) {
            for (int j = 0; j < hc; j++) {
                IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + 3 + 42 * i, y + 3 + 9 * j, 3, 65 + 15 * k, 42, 9);
            }
            IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + 3 + 42 * i, y + 3 + 9 * hc, 3, 65 + 15 * k, 42, h % 9);
        }
        for (int j = 0; j < hc; j++) {
            IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + 3 + 42 * wc, y + 3 + 9 * j, 3, 65 + 15 * k, w % 42, 9);
        }
        IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + 3 + 42 * wc, y + 3 + 9 * hc, 3, 65 + 15 * k, w % 42, h % 9);
*/
        this.renderBg(poseStack, getMinecraft(), mx, my);
        drawPrettyCenteredString(poseStack, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, 0);
    }

    public void drawPrettyCenteredString(PoseStack poseStack, Component component, int i, int j, int k) {
        getFont().draw(poseStack, component, (float) (i - getFont().width(component) / 2), j, k);
    }
}
