package red.felnull.imp.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class StringImageButton extends ImageButton {
    private int lethi;
    public boolean dredclos;
    private static Minecraft mc = Minecraft.getInstance();
    private int cloer;

    public StringImageButton(int p_i51134_1_, int p_i51134_2_, int p_i51134_3_, int p_i51134_4_, int p_i51134_5_, int p_i51134_6_, int p_i51134_7_, ResourceLocation p_i51134_8_, IPressable p_i51134_9_, int leth, boolean redclos) {
        this(p_i51134_1_, p_i51134_2_, p_i51134_3_, p_i51134_4_, p_i51134_5_, p_i51134_6_, p_i51134_7_, p_i51134_8_,
                256, 256, p_i51134_9_, leth, redclos);
    }

    public StringImageButton(int p_i51135_1_, int p_i51135_2_, int p_i51135_3_, int p_i51135_4_, int p_i51135_5_, int p_i51135_6_, int p_i51135_7_, ResourceLocation p_i51135_8_, int p_i51135_9_, int p_i51135_10_, IPressable p_i51135_11_, int leth, boolean redclos) {
        this(p_i51135_1_, p_i51135_2_, p_i51135_3_, p_i51135_4_, p_i51135_5_, p_i51135_6_, p_i51135_7_, p_i51135_8_,
                p_i51135_9_, p_i51135_10_, p_i51135_11_, new StringTextComponent(""), leth, redclos);
    }

    public StringImageButton(int p_i51136_1_, int p_i51136_2_, int p_i51136_3_, int p_i51136_4_, int p_i51136_5_, int p_i51136_6_, int p_i51136_7_, ResourceLocation p_i51136_8_, int p_i51136_9_, int p_i51136_10_, IPressable p_i51136_11_, ITextComponent p_i51136_12_, int leth, boolean redclos) {
        super(p_i51136_1_, p_i51136_2_, p_i51136_3_, p_i51136_4_, p_i51136_5_, p_i51136_6_, p_i51136_7_, p_i51136_8_, p_i51136_9_, p_i51136_10_, p_i51136_11_, p_i51136_12_);
        this.lethi = leth;
        this.dredclos = redclos;
        this.cloer = 0;

    }

    @Override
    public void func_230431_b_(MatrixStack matx, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        super.func_230431_b_(matx, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
        stringRenderButton(matx, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);

    }

    public void setStringColoer(int coloer) {
        this.cloer = coloer;
    }

    public void stringRenderButton(MatrixStack matx, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        fontrenderer.func_238422_b_(matx, this.func_230458_i_(), this.field_230690_l_ + this.field_230688_j_ / 2 + lethi, this.field_230691_m_ + (this.field_230689_k_ - 8) / 2, cloer);

        if (dredclos) {
            mc.getTextureManager().bindTexture(SoundFileUploaderScreen.SFU_GUI_TEXTURE2);
            func_238474_b_(matx, this.field_230690_l_, this.field_230691_m_, 24, 149, 12, 12);
        }
    }

}
