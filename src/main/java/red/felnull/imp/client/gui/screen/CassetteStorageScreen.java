package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.util.RenderUtil;
import red.felnull.imp.container.CassetteStorageContainer;
import red.felnull.imp.music.resource.PlayMusic;
import red.felnull.imp.tileentity.CassetteStorageTileEntity;
import red.felnull.imp.util.ItemHelper;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.stream.IntStream;

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


    @Override
    protected void drawGuiContainerBackgroundLayerByIKSG(MatrixStack matx, float partTick, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayerByIKSG(matx, partTick, mouseX, mouseY);
        CassetteStorageTileEntity cst = (CassetteStorageTileEntity) getTileEntity();
        if (!cst.isEmpty()) {
            IntStream.range(0, 8).forEach(n -> {
                ItemStack stack = cst.getStackInSlot(n);
                if (!stack.isEmpty() && ItemHelper.isWrittenCassetteTape(stack)) {
                    PlayMusic pm = ItemHelper.getPlayMusicByItem(stack);
                    IKSGRenderUtil.guiBindAndBlit(CS_GUI_TEXTURES, matx, getTexturStartX() + 7, getTexturStartY() + 19 + 18 * n, 0, 239, 86, 14, 256, 256);
                    IKSGRenderUtil.drawHorizontalMovementString(matx, getMinecraft().fontRenderer, pm.getName(), pm.getUUID() + "cs" + n, 30, getTexturStartX() + 22, getTexturStartY() + 21 + 18 * n, 69, 30);
                    RenderUtil.drwPlayImage(matx, pm.getImage(), getTexturStartX() + 8, getTexturStartY() + 20 + 18 * n, 12);
                }
            });
            IntStream.range(0, 8).forEach(n -> {
                ItemStack stack = cst.getStackInSlot(n + 8);
                if (!stack.isEmpty() && ItemHelper.isWrittenCassetteTape(stack)) {
                    PlayMusic pm = ItemHelper.getPlayMusicByItem(stack);
                    IKSGRenderUtil.guiBindAndBlit(CS_GUI_TEXTURES, matx, getTexturStartX() + 120, getTexturStartY() + 19 + 18 * n, 0, 239, 86, 14, 256, 256);
                    IKSGRenderUtil.drawHorizontalMovementString(matx, getMinecraft().fontRenderer, pm.getName(), pm.getUUID() + "cs" + (n + 8), 30, getTexturStartX() + 135, getTexturStartY() + 21 + 18 * n, 69, 30);
                    RenderUtil.drwPlayImage(matx, pm.getImage(), getTexturStartX() + 121, getTexturStartY() + 20 + 18 * n, 12);
                }
            });
        }
    }

}
