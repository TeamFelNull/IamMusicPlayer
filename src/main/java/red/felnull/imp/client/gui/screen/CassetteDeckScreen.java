package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.container.CassetteDeckContainer;
import red.felnull.otyacraftengine.client.gui.screen.AbstractIkisugiContainerScreen;

public class CassetteDeckScreen extends AbstractIkisugiContainerScreen<CassetteDeckContainer> {
    public static final ResourceLocation CD_GUI_TEXTURES = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/cassette_deck.png");

    public CassetteDeckScreen(CassetteDeckContainer screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
        this.xSize = 214;
        this.ySize = 227;
        this.playerInventoryTitleY = this.ySize - 94;
    }

    @Override
    public ResourceLocation getBackGrandTextuer() {
        return CD_GUI_TEXTURES;
    }

    @Override
    public void initByIKSG() {
        super.initByIKSG();

    }

    @Override
    protected void drawGuiContainerBackgroundLayerByIKSG(MatrixStack matx, float partTick, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayerByIKSG(matx, partTick, mouseX, mouseY);
    }
    @Override
    public void tickByIKSG() {
        super.tickByIKSG();


    }
}
