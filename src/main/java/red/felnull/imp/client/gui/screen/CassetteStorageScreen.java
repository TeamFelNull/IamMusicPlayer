package red.felnull.imp.client.gui.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.container.CassetteStorageContainer;

public class CassetteStorageScreen extends IMPAbstractScreen<CassetteStorageContainer> {
    public static final ResourceLocation CS_GUI_TEXTURES = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/cassette_storage.png");

    public CassetteStorageScreen(CassetteStorageContainer screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
        this.xSize = 234;
        this.ySize = 239;
        this.playerInventoryTitleY = this.ySize - 75;
    }


    @Override
    public ResourceLocation getBackGrandTextuer() {
        return CS_GUI_TEXTURES;
    }
}
