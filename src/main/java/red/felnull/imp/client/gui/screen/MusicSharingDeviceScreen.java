package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.block.MusicSharingDeviceBlock;
import red.felnull.imp.container.MusicSharingDeviceContainer;
import red.felnull.imp.tileentity.MusicSharingDeviceTileEntity;
import red.felnull.otyacraftengine.client.gui.screen.AbstractIkisugiContainerScreen;
import red.felnull.otyacraftengine.client.gui.widget.ChangeableImageButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class MusicSharingDeviceScreen extends AbstractIkisugiContainerScreen<MusicSharingDeviceContainer> {
    public static final ResourceLocation MSD_GUI_TEXTURES = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device.png");

    private ChangeableImageButton powerButton;
    private MonitorTextures MonitorTexture;

    public MusicSharingDeviceScreen(MusicSharingDeviceContainer screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
        this.xSize = 215;
        this.ySize = 242;
        setMonitorTexture();
    }

    @Override
    public void initByIKSG() {
        super.initByIKSG();
        instruction("opengui", new CompoundNBT());
        int xs = (this.getWidthByIKSG() - this.xSize) / 2;
        int ys = (this.getHeightByIKSG() - this.ySize) / 2;
        this.field_238745_s_ = this.ySize - 94;
        this.powerButton = this.addWidgetByIKSG(new ChangeableImageButton(xs + 181, ys + 202, 20, 20, 215, 0, 20, MSD_GUI_TEXTURES, 256, 256, (p_213096_1_) -> {
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
        int xs = (this.getWidthByIKSG() - this.xSize) / 2;
        int ys = (this.getHeightByIKSG() - this.ySize) / 2;
        IKSGRenderUtil.guiBindAndBlit(MonitorTexture.getTextuer(), matx, xs + 8, ys + 20, 0, 0, 199, 122, 199, 122);
        IKSGRenderUtil.matrixPop(matx);
    }

    @Override
    public void tickByIKSG() {
        setMonitorTexture();
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

    private void setMonitorTexture() {
        if (!isStateOn()) {
            MonitorTexture = MonitorTextures.OFF;
        } else if (getMode() == null) {
            MonitorTexture = MonitorTextures.OFF;
        } else if (getMode().equals("noantenna")) {
            MonitorTexture = MonitorTextures.NOANTENNA;
        } else if (getMode().equals("playlist")) {
            MonitorTexture = MonitorTextures.PLAYLIST;
        } else {
            MonitorTexture = MonitorTextures.ON;
        }
    }

    private enum MonitorTextures {
        OFF(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_off.png")),
        ON(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_on.png")),
        PLAYLIST(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_list.png")),
        NOANTENNA(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_noantenna.png"));
        private final ResourceLocation location;

        MonitorTextures(ResourceLocation location) {
            this.location = location;
        }

        public ResourceLocation getTextuer() {
            return this.location;
        }
    }
}
