package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.block.IMPAbstractEquipmentBlock;
import red.felnull.imp.ffmpeg.FFmpegManeger;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.util.IKSGStyles;

public abstract class IMPAbstractEquipmentScreen<T extends Container> extends IMPAbstractScreen<T> {
    public static final Style smart_fontStyle = IKSGStyles.withFont(new ResourceLocation(IamMusicPlayer.MODID, "imp_fonts"));
    public static final ResourceLocation EQUIPMENT_WIDGETS_TEXTURES = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/equipment_widgets.png");

    public IMPAbstractEquipmentScreen(T screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
    }

    protected void insPower(boolean on) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("on", on);
        this.instruction("power", tag);
    }

    public boolean isStateOn() {
        return getTileEntity().getBlockState().get(IMPAbstractEquipmentBlock.ON);
    }

    @Override
    public void initByIKSG() {
        FFmpegManeger maneger = FFmpegManeger.instance();
        if (!maneger.canUseFFmpeg()) {
            maneger.cantFFmpegCaution(getMinecraft().player);
            closeScreen();
            return;
        }
        super.initByIKSG();
        this.addWidgetByIKSG(new PowerButton(getTexturStartX() + xSize - 34, getTexturStartY() + ySize - 40, this::isStateOn, n -> insPower(!this.isStateOn())));
    }

    protected void drawCenterFontString(MatrixStack matx, IFormattableTextComponent text, int x, int y) {
        IKSGRenderUtil.drawCenterString(this.font, matx, IKSGStyles.withStyle(text, smart_fontStyle), x, y, 0);
    }

    protected void drawFontString(MatrixStack matx, IFormattableTextComponent text, int x, int y) {
        IKSGRenderUtil.drawString(this.font, matx, IKSGStyles.withStyle(text, smart_fontStyle), x, y, 0);
    }

    protected void drawMiniFontString(MatrixStack matx, IFormattableTextComponent text, int x, int y) {
        IKSGRenderUtil.matrixPush(matx);
        IKSGRenderUtil.matrixScalf(matx, 0.5f);
        IKSGRenderUtil.drawString(this.font, matx, IKSGStyles.withStyle(text, smart_fontStyle), x * 2, y * 2, 0);
        IKSGRenderUtil.matrixPop(matx);
    }

    @Override
    public boolean mouseDragged(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_, double p_231045_8_) {
        boolean flag1 = super.mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
        boolean flag2 = this.getListener() != null && this.isDragging() && p_231045_5_ == 0 && this.getListener().mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
        return flag1 & flag2;
    }

    private static class PowerButton extends Button {
        private final PowerButton.IPowerIsOn powerIsOn;

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
}
