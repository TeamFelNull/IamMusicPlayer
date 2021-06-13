package red.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import red.felnull.imp.client.gui.screen.monitor.MSDBaseMonitor;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class MSDSmartCheckbox extends AbstractButton implements IMSDSmartRender {
    private boolean checked;

    public MSDSmartCheckbox(int x, int y, Component component) {
        super(x, y, 15, 15, component);
    }

    @Override
    public void onPress() {
        this.checked = !checked;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void renderButton(PoseStack poseStack, int i, int j, float f) {
        int tx = 48;
        int ty = 0;

        if (isHovered())
            tx += 15;

        if (checked)
            ty += 15;


        IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x, y, tx, ty, 15, 15);

        drawPrettyString(poseStack, (MutableComponent) this.getMessage(), this.x + 18, this.y + (this.height - 8) / 2, 0);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }
}
