package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.block.MusicSharingDeviceBlock;
import red.felnull.imp.client.gui.widget.IkisugiImageButton;
import red.felnull.imp.client.gui.widget.MSDScrollBarSlider;
import red.felnull.imp.container.MusicSharingDeviceContainer;
import red.felnull.imp.item.IMPItems;
import red.felnull.imp.tileentity.MusicSharingDeviceTileEntity;
import red.felnull.otyacraftengine.client.gui.screen.AbstractIkisugiContainerScreen;
import red.felnull.otyacraftengine.client.gui.widget.ChangeableImageButton;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGScreenUtil;
import red.felnull.otyacraftengine.util.IKSGStyles;

import java.util.Arrays;

public class MusicSharingDeviceScreen extends AbstractIkisugiContainerScreen<MusicSharingDeviceContainer> {


    public static final ResourceLocation MSD_GUI_TEXTURES = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device.png");
    //   private static final ResourceLocation fontLocation = new ResourceLocation(IamMusicPlayer.MODID, "msd");
    private static final ResourceLocation fontLocation = new ResourceLocation("minecraft", "default");
    private static final Style fontStyle = IKSGStyles.withFont(fontLocation);

    private ChangeableImageButton powerButton;
    private IkisugiImageButton allbutton;
    private ImageButton addGuildButton;
    private ScrollBarSlider guildlistbar;
    private ScrollBarSlider playlistbar;

    private String listname;

    private Monitors Monitorsa;

    public MusicSharingDeviceScreen(MusicSharingDeviceContainer screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
        this.xSize = 215;
        this.ySize = 242;
        setMonitorsa();
        setListName();
    }

    protected int getMonitorStartX() {
        return getTexturStartX() + 8;
    }

    protected int getMonitorStartY() {
        return getTexturStartY() + 20;
    }


    @Override
    public void initByIKSG() {
        super.initByIKSG();
        instruction("opengui", new CompoundNBT());
        this.field_238745_s_ = this.ySize - 94;
        this.powerButton = this.addWidgetByIKSG(new ChangeableImageButton(getTexturStartX() + 181, getTexturStartY() + 202, 20, 20, 215, 0, 20, MSD_GUI_TEXTURES, 256, 256, n -> {
            insPower(!this.isStateOn());
        }));
        if (isStateOn()) {
            powerButton.setTextuer(235, 0, 20, 256, 256);
        } else {
            powerButton.setTextuer(215, 0, 20, 256, 256);
        }
        this.allbutton = this.addWidgetByIKSG(new IkisugiImageButton(getMonitorStartX() + 1, getMonitorStartY() + 1, 18, 18, 215, 60, 18, MSD_GUI_TEXTURES, n -> {

        }, IKSGStyles.withStyle(new TranslationTextComponent("msd.all"), fontStyle)));
        this.allbutton.setShowString(true);
        this.allbutton.setSizeAdjustment(true);
        this.allbutton.setShadwString(false);
        this.allbutton.setStringColor(0);
        IKSGScreenUtil.setVisible(this.allbutton, false);

        this.addGuildButton = this.addWidgetByIKSG(new ImageButton(getMonitorStartX() + 20, getMonitorStartY() + 1, 9, 18, 235, 40, 18, MSD_GUI_TEXTURES, n -> {

        }));
        IKSGScreenUtil.setVisible(this.addGuildButton, false);

        this.guildlistbar = this.addWidgetByIKSG(new MSDScrollBarSlider(getMonitorStartX() + 20, getMonitorStartY() + 20, 101, 100, 0, -20));
        IKSGScreenUtil.setVisible(this.guildlistbar, false);

        this.playlistbar = this.addWidgetByIKSG(new MSDScrollBarSlider(getMonitorStartX() + 190, getMonitorStartY() + 20, 101, 100, 0, -160));
        IKSGScreenUtil.setVisible(this.playlistbar, false);

    }

    @Override
    public boolean func_231045_a_(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_, double p_231045_8_) {
        super.func_231045_a_(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
        return this.func_241217_q_() != null && this.func_231041_ay__() && p_231045_5_ == 0 ? this.func_241217_q_().func_231045_a_(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_) : false;
    }


    @Override
    protected void drawGuiContainerBackgroundLayerByIKSG(MatrixStack matx, float partTick, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayerByIKSG(matx, partTick, mouseX, mouseY);
        IKSGRenderUtil.matrixPush(matx);
        IKSGRenderUtil.guiBindAndBlit(Monitorsa.getTextuer(), matx, getMonitorStartX(), getMonitorStartY(), 0, 0, 199, 122, 199, 122);
        IKSGRenderUtil.matrixPop(matx);
        switch (Monitorsa) {
            case NOANTENNA:
                drawNoAntenna(matx);
                break;
            case PLAYLIST:
                drawPlayList(matx);
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
        IKSGScreenUtil.setVisible(this.allbutton, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.addGuildButton, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.guildlistbar, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.playlistbar, isMonitor(Monitors.PLAYLIST));
    }

    private boolean isMonitor(Monitors... mo) {
        return Arrays.asList(mo).contains(Monitorsa);
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

    private void setListName() {
        listname = I18n.format("msd.all");

    }

    protected void drawPlayList(MatrixStack matrx) {
        drawFontString(matrx, new StringTextComponent(listname), getMonitorStartX() + 31, getMonitorStartY() + 2);
    }

    protected void drawNoAntenna(MatrixStack matrx) {
        drawCenterFontString(matrx, new TranslationTextComponent("msd.noantenna"), getTexturStartX() + getXSize() / 2, getTexturStartY() + 70);
        ItemRenderer ir = getMinecraft().getItemRenderer();
        ir.zLevel = 100.0F;
        ir.renderItemAndEffectIntoGUI(new ItemStack(IMPItems.PARABOLIC_ANTENNA), getTexturStartX() + getXSize() / 2 - 8, getTexturStartY() + 85);
        ir.zLevel = 0.0F;
        IKSGRenderUtil.matrixPush(matrx);
        IKSGRenderUtil.matrixTranslatef(matrx, 0, 0, 500);
        IKSGRenderUtil.guiBindAndBlit(MSD_GUI_TEXTURES, matrx, getTexturStartX() + getXSize() / 2 - 10, getTexturStartY() + 83, 215, 40, 20, 20, 256, 256);
        IKSGRenderUtil.matrixPop(matrx);
    }

    protected void drawCenterFontString(MatrixStack matx, IFormattableTextComponent text, int x, int y) {
        IKSGRenderUtil.drawCenterString(this.field_230712_o_, matx, IKSGStyles.withStyle(text, fontStyle), x, y, 0);
    }

    protected void drawFontString(MatrixStack matx, IFormattableTextComponent text, int x, int y) {
        IKSGRenderUtil.drawString(this.field_230712_o_, matx, IKSGStyles.withStyle(text, fontStyle), x, y, 0);
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
