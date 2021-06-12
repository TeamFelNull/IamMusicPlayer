package red.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.MutableComponent;
import red.felnull.imp.client.gui.IMPFonts;
import red.felnull.otyacraftengine.client.gui.components.IIkisugibleWidget;
import red.felnull.otyacraftengine.util.IKSGColorUtil;

public interface IMSDSmartRender extends IIkisugibleWidget {
    default void fillXGrayLine(PoseStack ps, int x, int y, int s) {
        GuiComponent.fill(ps, x, y, x + s, y + 1, IKSGColorUtil.toSRGB(14474460));
    }

    default void fillYGrayLine(PoseStack ps, int x, int y, int s) {
        GuiComponent.fill(ps, x, y, x + 1, y + s, IKSGColorUtil.toSRGB(14474460));
    }

    default void fillLightGray(PoseStack poseStack, int x, int y, int w, int h) {
        GuiComponent.fill(poseStack, x, y, x + w, y + h, IKSGColorUtil.toSRGB(16119543));
    }

    default void fillBerryDarkGray(PoseStack poseStack, int x, int y, int w, int h) {
        GuiComponent.fill(poseStack, x, y, x + w, y + h, IKSGColorUtil.toSRGB(0x343434));
    }

    default void fillXDarkGrayLine(PoseStack ps, int x, int y, int s) {
        GuiComponent.fill(ps, x, y, x + s, y + 1, IKSGColorUtil.toSRGB(0x585858));
    }

    default void fillYDarkGrayLine(PoseStack ps, int x, int y, int s) {
        GuiComponent.fill(ps, x, y, x + 1, y + s, IKSGColorUtil.toSRGB(0x585858));
    }

    default void drawPrettyCenteredString(PoseStack poseStack, MutableComponent component, int i, int j, int k) {
        component = component.withStyle(IMPFonts.FLOPDE_SIGN_FONT);
        getFont().draw(poseStack, component, (float) (i - getFont().width(component) / 2), j, k);
    }

    default void drawPrettyString(PoseStack poseStack, MutableComponent component, float i, float j, int k) {
        component = component.withStyle(IMPFonts.FLOPDE_SIGN_FONT);
        getFont().draw(poseStack, component, i, j, k);
    }
}
