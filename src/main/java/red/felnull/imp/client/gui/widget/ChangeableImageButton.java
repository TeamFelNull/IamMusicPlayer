package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ChangeableImageButton extends ImageButton {
    private ResourceLocation resourceLocation;
    private int xTexStart;
    private int yTexStart;
    private int yDiffText;
    private int textureWidth;
    private int textureHeight;

    public ChangeableImageButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, IPressable onPressIn) {
        this(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, 256, 256, onPressIn);
    }

    public ChangeableImageButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, int p_i51135_9_, int p_i51135_10_, Button.IPressable onPressIn) {
        this(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, p_i51135_9_, p_i51135_10_, onPressIn, StringTextComponent.field_240750_d_);
    }

    public ChangeableImageButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation p_i232261_8_, int p_i232261_9_, int p_i232261_10_, IPressable p_i232261_11_, ITextComponent p_i232261_12_) {
        super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, null, p_i232261_9_, p_i232261_10_, p_i232261_11_, p_i232261_12_);
        this.textureWidth = p_i232261_9_;
        this.textureHeight = p_i232261_10_;
        this.xTexStart = xTexStartIn;
        this.yTexStart = yTexStartIn;
        this.yDiffText = yDiffTextIn;
        this.resourceLocation = p_i232261_8_;
    }

    public void setTextuer(int xTexStartIn, int yTexStartIn, int yDiffTextIn, int texWidthIn, int texHeightIn) {
        this.textureWidth = texWidthIn;
        this.textureHeight = texHeightIn;
        this.xTexStart = xTexStartIn;
        this.yTexStart = yTexStartIn;
        this.yDiffText = yDiffTextIn;
    }


    public void setResourceLocation(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public void func_230431_b_(MatrixStack matrixStack, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(this.resourceLocation);
        int i = this.yTexStart;
        if (this.func_230449_g_()) {
            i += this.yDiffText;
        }
        RenderSystem.enableDepthTest();
        func_238463_a_(matrixStack, this.field_230690_l_, this.field_230691_m_, (float) this.xTexStart, (float) i, this.field_230688_j_, this.field_230689_k_, this.textureWidth, this.textureHeight);
    }
}
