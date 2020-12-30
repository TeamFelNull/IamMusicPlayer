package red.felnull.imp.client.gui.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.container.BoomboxContainer;

public class BoomboxScreen extends IMPAbstractEquipmentScreen<BoomboxContainer> {
    public static final ResourceLocation BOOMBOX_GUI_TEXTURES = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox.png");

    public BoomboxScreen(BoomboxContainer screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
        this.xSize = 214;
        this.ySize = 165;
        this.playerInventoryTitleY = this.ySize - 94;
    }

    @Override
    public ResourceLocation getBackGrandTextuer() {
        return BOOMBOX_GUI_TEXTURES;
    }

}
