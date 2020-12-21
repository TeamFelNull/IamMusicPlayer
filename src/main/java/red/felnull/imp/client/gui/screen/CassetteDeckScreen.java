package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.container.CassetteDeckContainer;
import red.felnull.imp.tileentity.CassetteDeckTileEntity;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class CassetteDeckScreen extends IMPAbstractEquipmentScreen<CassetteDeckContainer> {
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

    protected int getMonitorStartX() {
        return getTexturStartX() + 7;
    }

    protected int getMonitorStartY() {
        return getTexturStartY() + 19;
    }

    @Override
    protected void drawGuiContainerBackgroundLayerByIKSG(MatrixStack matx, float partTick, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayerByIKSG(matx, partTick, mouseX, mouseY);
        IKSGRenderUtil.matrixPush(matx);
        IKSGRenderUtil.guiBindAndBlit(getCurrentScreen().getTexLocation(), matx, getMonitorStartX(), getMonitorStartY(), 0, 0, 200, 80, 200, 80);
        IKSGRenderUtil.matrixPop(matx);
        switch (getCurrentScreen()) {
            case SELECTION:
                drawSelectionScreen(matx, partTick, mouseX, mouseY);
                break;
        }
    }

    private void drawSelectionScreen(MatrixStack matrx, float partTick, int mouseX, int mouseY) {
        drawFontString(matrx, new TranslationTextComponent("cd.operationselection"), getMonitorStartX() + 2, getMonitorStartY() + 2);
    }

    @Override
    public void tickByIKSG() {
        super.tickByIKSG();

    }

    public CassetteDeckTileEntity.Screen getCurrentScreen() {
        return ((CassetteDeckTileEntity) getTileEntity()).getScreen();
    }
}
