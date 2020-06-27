package red.felnull.imp.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import red.felnull.imp.util.RenderHelper;

public class FaseStringImageButton extends StringImageButton {
    private static Minecraft mc = Minecraft.getInstance();

    public boolean isDrawPlayerFase;
    public boolean isStringWidth;
    private boolean fileselectbuton = false;
    public boolean isReddish;

    private String faseplayername;
    public boolean isOtherPlayerFase;

    public FaseStringImageButton(int p_i51136_1_, int p_i51136_2_, int p_i51136_3_, int p_i51136_4_, int p_i51136_5_,
                                 int p_i51136_6_, int p_i51136_7_, ResourceLocation p_i51136_8_, int p_i51136_9_, int p_i51136_10_,
                                 IPressable p_i51136_11_, String p_i51136_12_, int leth, boolean redclos) {
        super(p_i51136_1_, p_i51136_2_, p_i51136_3_, p_i51136_4_, p_i51136_5_, p_i51136_6_, p_i51136_7_, p_i51136_8_,
                p_i51136_9_, p_i51136_10_, p_i51136_11_, new StringTextComponent(p_i51136_12_), leth, redclos);

    }

    public FaseStringImageButton(int p_i51136_1_, int p_i51136_2_, int p_i51136_3_, int p_i51136_4_,
                                 int p_i51136_5_, int p_i51136_6_, int p_i51136_7_, ResourceLocation p_i51136_8_, int p_i51136_9_,
                                 int p_i51136_10_, IPressable p_i51136_11_, String p_i51136_12_, boolean draplayer, boolean stw) {
        this(p_i51136_1_, p_i51136_2_, p_i51136_3_, p_i51136_4_, p_i51136_5_, p_i51136_6_, p_i51136_7_, p_i51136_8_,
                p_i51136_9_, p_i51136_10_, p_i51136_11_, new StringTextComponent(p_i51136_12_), draplayer);
    }

    public FaseStringImageButton(int p_i51136_1_, int p_i51136_2_, int p_i51136_3_, int p_i51136_4_,
                                 int p_i51136_5_, int p_i51136_6_, int p_i51136_7_, ResourceLocation p_i51136_8_, int p_i51136_9_,
                                 int p_i51136_10_, IPressable p_i51136_11_, ITextComponent p_i51136_12_, boolean draplayer) {
        super(p_i51136_1_, p_i51136_2_, p_i51136_3_, p_i51136_4_, p_i51136_5_, p_i51136_6_, p_i51136_7_, p_i51136_8_,
                p_i51136_9_, p_i51136_10_, p_i51136_11_, p_i51136_12_, -23, false);
        this.isDrawPlayerFase = draplayer;

    }

    public void setFileSelectButton(boolean isfileselect) {
        fileselectbuton = isfileselect;

    }

    public void setFasResourceLocation(String name) {
        isOtherPlayerFase = true;

        faseplayername = name;

    }

    @Override
    public void func_230431_b_(MatrixStack matx, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        super.func_230431_b_(matx, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
        if (isDrawPlayerFase) {
            if (!isOtherPlayerFase) {
                RenderSystem.pushMatrix();

                if (isReddish)
                    RenderSystem.color4f(2.55f, 0.69f, 0.01f, 1.0F);
                else
                    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

                mc.getTextureManager().bindTexture(mc.player.getLocationSkin());
                AbstractGui.func_238463_a_(matx, this.field_230690_l_ + (fileselectbuton ? 0 : 2), this.field_230691_m_ + (this.field_230689_k_ - 8) / 2, 8, 8, 8, 8, 64,
                        64);
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.popMatrix();
            } else {
                if (!faseplayername.isEmpty()) {

                    RenderSystem.pushMatrix();

                    if (isReddish)
                        RenderSystem.color4f(2.55f, 0.69f, 0.01f, 1.0F);
                    else
                        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

                    RenderHelper.drawPlayerFase(matx,
                            faseplayername, this.field_230690_l_ + (fileselectbuton ? 0 : 2), this.field_230691_m_ + (this.field_230691_m_ - 8) / 2);

                    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.popMatrix();
                }
            }
        }
    }
}
