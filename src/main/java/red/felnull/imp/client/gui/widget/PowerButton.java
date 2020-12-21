package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import red.felnull.imp.client.gui.screen.IMPAbstractEquipmentScreen;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class PowerButton extends Button {

    private final IPowerIsOn powerIsOn;

    public PowerButton(int x, int y, IPowerIsOn ison, IPressable pressedAction) {
        super(x, y, 20, 20, new TranslationTextComponent("narrator.button.power"), pressedAction);
        this.powerIsOn = ison;
    }

    public void renderButton(MatrixStack matx, int mouseX, int mouseY, float partialTicks) {
        IKSGRenderUtil.guiBindAndBlit(IMPAbstractEquipmentScreen.EQUIPMENT_WIDGETS_TEXTURES, matx, this.x, this.y, this.powerIsOn.isOn() ? 20 : 0, this.isHovered() ? 20 : 0, 20, 20, 256, 256);
    }

    @OnlyIn(Dist.CLIENT)
    public interface IPowerIsOn {
        boolean isOn();
    }
}
