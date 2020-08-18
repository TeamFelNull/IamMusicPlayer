package red.felnull.imp.client.gui.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.block.MusicSharingDeviceBlock;
import red.felnull.imp.client.gui.widget.ChangeableImageButton;
import red.felnull.imp.container.MusicSharingDeviceContainer;
import red.felnull.otyacraftengine.client.gui.screen.AbstractIkisugiContainerScreen;

public class MusicSharingDeviceScreen extends AbstractIkisugiContainerScreen<MusicSharingDeviceContainer> {
    public static final ResourceLocation MSD_GUI_TEXTURES = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device.png");

    private ChangeableImageButton powerButton;

    public MusicSharingDeviceScreen(MusicSharingDeviceContainer screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
        this.xSize = 215;
        this.ySize = 242;
    }

    @Override
    public void initByIKSG() {
        super.initByIKSG();
        int xs = (this.getWidthByIKSG() - this.xSize) / 2;
        int ys = (this.getHeightByIKSG() - this.ySize) / 2;

        this.field_238745_s_ = this.ySize - 94;

        this.powerButton = this.addWidgetByIKSG(new ChangeableImageButton(xs + 181, ys + 202, 20, 20, 215, 0, 20, MSD_GUI_TEXTURES, 256, 256, (p_213096_1_) -> {
        }, new TranslationTextComponent("narrator.button.power")));

        if (isStateOn()) {
            powerButton.setTextuer(235, 0, 20, 256, 256);
        } else {
            powerButton.setTextuer(215, 0, 20, 256, 256);
        }

    }

    @Override
    public void tickByIKSG() {
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
        return IamMusicPlayer.proxy.getMinecraft().world.getBlockState(this.container.getPos()).get(MusicSharingDeviceBlock.ON);
    }

}
