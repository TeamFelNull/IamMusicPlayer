package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.block.propertie.BoomboxMode;
import red.felnull.imp.container.BoomboxContainer;
import red.felnull.imp.tileentity.BoomboxTileEntity;
import red.felnull.imp.util.StringUtils;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class BoomboxScreen extends IMPAbstractEquipmentScreen<BoomboxContainer> {
    public static final ResourceLocation BOOMBOX_GUI_TEXTURES = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox.png");

    private ImageButton pauseButton;
    private ImageButton stopButton;
    private ImageButton playButton;

    public BoomboxScreen(BoomboxContainer screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
        this.xSize = 214;
        this.ySize = 165;
        this.playerInventoryTitleY = this.ySize - 94;
    }

    @Override
    public void initByIKSG() {
        super.initByIKSG();
        addBoomboxButton(70, 17, BoomboxMode.PAUSE);
        addBoomboxButton(92, 17, BoomboxMode.STOP);
        addBoomboxButton(114, 17, BoomboxMode.PLAY);

    }

    private void addBoomboxButton(int x, int y, BoomboxMode mode) {
        this.addWidgetByIKSG(new BoomboxButton(getTexturStartX() + x, getTexturStartY() + y, mode, n -> insMode(mode), this::getMode));
    }

    private BoomboxMode getMode() {
        return ((BoomboxTileEntity) getTileEntity()).getMode();
    }

    @Override
    public ResourceLocation getBackGrandTextuer() {
        return BOOMBOX_GUI_TEXTURES;
    }

    @Override
    protected void drawGuiContainerBackgroundLayerByIKSG(MatrixStack matx, float partTick, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayerByIKSG(matx, partTick, mouseX, mouseY);
        BoomboxTileEntity boomboxTile = (BoomboxTileEntity) getTileEntity();
        if (boomboxTile.getMusic() != null && boomboxTile.isOn()) {
            if (boomboxTile.isPlayWaiting()) {
                IKSGRenderUtil.drawCenterString(this.font, matx, new TranslationTextComponent("boombox.loading"), getTexturStartX() + 72, getTexturStartY() + 48, 2722312);
            } else {
                IKSGRenderUtil.drawCenterString(this.font, matx, new StringTextComponent(StringUtils.getTimeNotationPercentage(boomboxTile.getCurrentMusicPlayPosition(), boomboxTile.getMusicDuration())), getTexturStartX() + 72, getTexturStartY() + 48, 2722312);
            }

        }
    }

    public void insMode(BoomboxMode mode) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("name", mode.getName());
        this.instruction("Mode", tag);
    }

    private static class BoomboxButton extends Button {
        private final BoomboxMode nummode;
        private final IMode mode;

        public BoomboxButton(int x, int y, BoomboxMode nummode, IPressable pressedAction, IMode mode) {
            super(x, y, 22, 17, new TranslationTextComponent("narrator.button.boombox"), pressedAction);
            this.nummode = nummode;
            this.mode = mode;
        }

        public void renderButton(MatrixStack matx, int mouseX, int mouseY, float partialTicks) {
            boolean flag = nummode == mode.getMode();
            int zure = nummode == BoomboxMode.PAUSE ? 0 : nummode == BoomboxMode.STOP ? 1 : nummode == BoomboxMode.PLAY ? 2 : 3;
            IKSGRenderUtil.guiBindAndBlit(BOOMBOX_GUI_TEXTURES, matx, this.x, this.y, zure * 22, (this.isHovered() ? 17 : 0) + (flag ? 34 : 0) + 165, 22, 17, 256, 256);
        }

        @OnlyIn(Dist.CLIENT)
        public interface IMode {
            BoomboxMode getMode();
        }
    }
}
