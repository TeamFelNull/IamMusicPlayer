package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class UploadLocationSelectButton extends ImageButton {
    private final ITextComponent text;
    private final boolean isComingSoon;

    public UploadLocationSelectButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, IPressable onPressIn, ITextComponent text, boolean isComingSoon) {
        super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, onPressIn);
        this.text = text;
        this.isComingSoon = isComingSoon;
    }

    public void func_230431_b_(MatrixStack matrix, int mouseX, int mouseY, float parTic) {
        super.func_230431_b_(matrix, mouseX, mouseY, parTic);
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        int drsize = isComingSoon ? field_230688_j_ - 2 : field_230688_j_ - 15;
        int stsize = fontrenderer.func_238414_a_(text);
        float size = drsize >= stsize ? 1 : (float) drsize / (float) stsize;
        int x = isComingSoon ? this.field_230690_l_ + this.field_230688_j_ / 2 + 1 : this.field_230690_l_ + 13;
        int y = (int) (this.field_230691_m_ + (this.field_230689_k_ - fontrenderer.FONT_HEIGHT) / 2);
        IKSGRenderUtil.matrixPush(matrix);
        IKSGRenderUtil.matrixScalf(matrix, size);
        if (isComingSoon)
            IKSGRenderUtil.drawCenterString(fontrenderer, matrix, text, (int) ((float) x / size), (int) ((float) y / size), 0, false);
        else
            IKSGRenderUtil.drawString(fontrenderer, matrix, text, (int) ((float) x / size), (int) ((float) y / size), 0, false);
        IKSGRenderUtil.matrixPop(matrix);
    }
}
