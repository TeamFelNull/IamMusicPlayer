package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.block.MusicSharingDeviceBlock;
import red.felnull.imp.container.MusicSharingDeviceContainer;
import red.felnull.imp.tileentity.MusicSharingDeviceTileEntity;
import red.felnull.otyacraftengine.client.gui.screen.AbstractIkisugiContainerScreen;
import red.felnull.otyacraftengine.client.gui.widget.ChangeableImageButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.util.IKSGDokataUtil;
import red.felnull.otyacraftengine.util.IKSGStyles;

public class MusicSharingDeviceScreen extends AbstractIkisugiContainerScreen<MusicSharingDeviceContainer> {
    public static final ResourceLocation MSD_GUI_TEXTURES = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device.png");
    private static final ResourceLocation fontLocation = new ResourceLocation(IamMusicPlayer.MODID, "msd");
    private static final Style fontStyle = IKSGStyles.withFont(fontLocation);

    private ChangeableImageButton powerButton;
    private Monitors Monitorsa;

    public MusicSharingDeviceScreen(MusicSharingDeviceContainer screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
        this.xSize = 215;
        this.ySize = 242;
        setMonitorsa();
    }

    protected int getTexturStartX() {
        return (this.getWidthByIKSG() - this.xSize) / 2;
    }

    protected int getTexturStartY() {
        return (this.getHeightByIKSG() - this.ySize) / 2;
    }

    @Override
    public void initByIKSG() {
        super.initByIKSG();
        instruction("opengui", new CompoundNBT());
        this.field_238745_s_ = this.ySize - 94;
        this.powerButton = this.addWidgetByIKSG(new ChangeableImageButton(getTexturStartX() + 181, getTexturStartY() + 202, 20, 20, 215, 0, 20, MSD_GUI_TEXTURES, 256, 256, (p_213096_1_) -> {
            insPower(!this.isStateOn());
        }, new TranslationTextComponent("narrator.button.power")));
        if (isStateOn()) {
            powerButton.setTextuer(235, 0, 20, 256, 256);
        } else {
            powerButton.setTextuer(215, 0, 20, 256, 256);
        }
    }


    @Override
    protected void drawGuiContainerBackgroundLayerByIKSG(MatrixStack matx, float partTick, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayerByIKSG(matx, partTick, mouseX, mouseY);
        IKSGRenderUtil.matrixPush(matx);
        IKSGRenderUtil.guiBindAndBlit(Monitorsa.getTextuer(), matx, getTexturStartX() + 8, getTexturStartY() + 20, 0, 0, 199, 122, 199, 122);
        IKSGRenderUtil.matrixPop(matx);
        switch (Monitorsa) {
            case NOANTENNA:
                drawNoAntenna(matx);
                break;
        }
    }

    @Override
    public void tickByIKSG() {
        setMonitorsa();
        if (isStateOn()) {
            powerButton.setTextuer(235, 0, 20, 256, 256);
        } else {
            powerButton.setTextuer(215, 0, 20, 256, 256);
        }
    }

    @Override
    public ResourceLocation getBackGrandTextuer() {
        return MSD_GUI_TEXTURES;
    }

    public boolean isStateOn() {
        return getTileEntity().getBlockState().get(MusicSharingDeviceBlock.ON);
    }

    protected String getMode() {
        return ((MusicSharingDeviceTileEntity) getTileEntity()).getMode(IamMusicPlayer.proxy.getMinecraft().player);
    }

    public void insPower(boolean on) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("on", on);
        this.instruction("power", tag);
    }

    private void setMonitorsa() {
        if (!isStateOn()) {
            Monitorsa = Monitors.OFF;
        } else if (getMode() == null) {
            Monitorsa = Monitors.OFF;
        } else if (getMode().equals("noantenna")) {
            Monitorsa = Monitors.NOANTENNA;
        } else if (getMode().equals("playlist")) {
            Monitorsa = Monitors.PLAYLIST;
        } else {
            Monitorsa = Monitors.ON;
        }
    }

    protected void drawNoAntenna(MatrixStack matrx) {
        drawFontString(matrx, new StringTextComponent(IKSGDokataUtil.getYattaze()), getTexturStartX() + getXSize() / 2, getTexturStartY() + getYSize() / 2);
    }

    protected void drawFontString(MatrixStack matx, IFormattableTextComponent text, int x, int y) {
        drawCenterString(this.field_230712_o_, matx, IKSGStyles.withStyle(text, fontStyle), x, y, 0);
    }

    public static void drawCenterString(FontRenderer fr, MatrixStack matrix, ITextComponent text, int x, int y, int color) {
        int size = fr.func_238414_a_(text);
        drawString(fr, matrix, text, x - size / 2, y, color);
    }

    public static void drawString(FontRenderer fr, MatrixStack matrix, ITextComponent text, int x, int y, int color) {
        fr.func_243248_b(matrix, text, x, y, color);
    }

    private enum Monitors {
        OFF(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_off.png")),
        ON(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_on.png")),
        PLAYLIST(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_list.png")),
        NOANTENNA(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_noantenna.png"));
        private final ResourceLocation location;

        Monitors(ResourceLocation location) {
            this.location = location;
        }

        public ResourceLocation getTextuer() {
            return this.location;
        }
    }
}
