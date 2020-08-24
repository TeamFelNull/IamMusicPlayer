package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class IkisugiImageButton extends ImageButton {
    private final ResourceLocation resourceLocation;
    private boolean showString;
    private boolean shadwString;
    private boolean sizeAdjustment;
    protected int strColor;

    public IkisugiImageButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, Button.IPressable onPressIn, ITextComponent p_i232261_12_) {
        this(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, 256, 256, onPressIn, p_i232261_12_);
    }

    public IkisugiImageButton(int p_i232261_1_, int p_i232261_2_, int p_i232261_3_, int p_i232261_4_, int p_i232261_5_, int p_i232261_6_, int p_i232261_7_, ResourceLocation p_i232261_8_, int p_i232261_9_, int p_i232261_10_, Button.IPressable p_i232261_11_, ITextComponent p_i232261_12_) {
        super(p_i232261_1_, p_i232261_2_, p_i232261_3_, p_i232261_4_, p_i232261_5_, p_i232261_6_, p_i232261_7_, p_i232261_8_, p_i232261_9_, p_i232261_10_, p_i232261_11_, p_i232261_12_);
        this.resourceLocation = p_i232261_8_;
        this.showString = false;
        this.shadwString = true;
        this.sizeAdjustment = false;
        this.strColor = -1;
    }

    public void setShowString(boolean showString) {
        this.showString = showString;
    }

    public void setSizeAdjustment(boolean sizeAdjustment) {
        this.sizeAdjustment = sizeAdjustment;
    }

    public void setShadwString(boolean shadwString) {
        this.shadwString = shadwString;
    }

    public void setStringColor(int strColor) {
        this.strColor = strColor;
    }

    public void func_230431_b_(MatrixStack matrix, int mouseX, int mouseY, float parTic) {
        super.func_230431_b_(matrix, mouseX, mouseY, parTic);
        if (showString) {
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontrenderer = minecraft.fontRenderer;
            int j = strColor == -1 ? getFGColor() | MathHelper.ceil(this.field_230695_q_ * 255.0F) << 24 : strColor;
            IKSGRenderUtil.matrixPush(matrix);
            int x = this.field_230690_l_ + this.field_230688_j_ / 2;
            int y = this.field_230691_m_ + (this.field_230689_k_ - 8) / 2;
            if (shadwString) {
                func_238472_a_(matrix, fontrenderer, this.func_230458_i_(), x, y, j);
            } else {
                IKSGRenderUtil.drawCenterString(fontrenderer, matrix, this.func_230458_i_(), x, y, j);
            }
            IKSGRenderUtil.matrixPop(matrix);
        }
    }

}
