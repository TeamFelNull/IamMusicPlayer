package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
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

    private BoomboxButton pauseButton;
    private BoomboxButton stopButton;
    private BoomboxButton playButton;
    private BoomboxButton loopButton;
    private BoomboxButton volDownButton;
    private BoomboxButton volUpButton;
    private BoomboxButton volMuteButton;

    public BoomboxScreen(BoomboxContainer screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
        this.xSize = 214;
        this.ySize = 165;
        this.playerInventoryTitleY = this.ySize - 94;
    }

    @Override
    public void initByIKSG() {
        super.initByIKSG();
        this.pauseButton = addBoomboxButton(25, 17, 0, n -> {
            if (!getCassetteTape().isEmpty() && getMode() == BoomboxMode.PLAY)
                insMode(BoomboxMode.PAUSE);
        }, () -> getMode() == BoomboxMode.PAUSE);
        this.stopButton = addBoomboxButton(47, 17, 1, n -> insStop());
        this.playButton = addBoomboxButton(69, 17, 2, n -> {
            if (!getCassetteTape().isEmpty())
                insMode(BoomboxMode.PLAY);
        }, () -> getMode() == BoomboxMode.PLAY);
        this.loopButton = addBoomboxButton(91, 17, 3, n -> {
            BoomboxTileEntity boomboxTile = (BoomboxTileEntity) getTileEntity();
            insLoop(!boomboxTile.isMusicLoop());
        }, () -> {
            BoomboxTileEntity boomboxTile = (BoomboxTileEntity) getTileEntity();
            return boomboxTile.isMusicLoop();
        });
        this.volDownButton = addBoomboxButton(119, 17, 4, n -> System.out.println("dwon"));
        this.volUpButton = addBoomboxButton(141, 17, 5, n -> System.out.println("up"));
        this.volMuteButton = addBoomboxButton(163, 17, 6, n -> System.out.println("mute"));
    }

    private BoomboxButton addBoomboxButton(int x, int y, int btnnum, Button.IPressable pressedAction) {
        return addBoomboxButton(x, y, btnnum, pressedAction, () -> false);
    }

    private BoomboxButton addBoomboxButton(int x, int y, int btnnum, Button.IPressable pressedAction, BoomboxButton.IWhetherPresseble whetherpresseble) {
        return this.addWidgetByIKSG(new BoomboxButton(getTexturStartX() + x, getTexturStartY() + y, btnnum, n -> {
            if (isStateOn())
                pressedAction.onPress(n);
        }, whetherpresseble));
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
        if (boomboxTile.isOn()) {

            if (boomboxTile.getMusic() != null) {
                IKSGRenderUtil.guiBindAndBlit(BOOMBOX_GUI_TEXTURES, matx, getTexturStartX() + 116, getTexturStartY() + 47, 154, 165, 8, 8, 256, 256);

                IKSGRenderUtil.guiBindAndBlit(BOOMBOX_GUI_TEXTURES, matx, getTexturStartX() + 36, getTexturStartY() + 58, 154, 173, 91, 3, 256, 256);

                float cumpp = (float) boomboxTile.getCurrentMusicPlayPosition() / (float) boomboxTile.getMusicDuration();
                IKSGRenderUtil.guiBindAndBlit(BOOMBOX_GUI_TEXTURES, matx, getTexturStartX() + 36, getTexturStartY() + 58, 154, 185, (int) (91 * cumpp), 3, 256, 256);


                int zure = getMode() == BoomboxMode.PAUSE ? 1 : getMode() == BoomboxMode.NONE ? 2 : 0;
                IKSGRenderUtil.guiBindAndBlit(BOOMBOX_GUI_TEXTURES, matx, getTexturStartX() + 28, getTexturStartY() + 56, 164 + 7 * zure, 165, 7, 7, 256, 256);

                IKSGRenderUtil.guiBindAndBlit(BOOMBOX_GUI_TEXTURES, matx, getTexturStartX() + 128, getTexturStartY() + 55, 154 + (boomboxTile.isMusicLoop() ? 0 : 11), 176, 11, 8, 256, 256);


                IKSGRenderUtil.matrixPush(matx);
                float fs = 0.75f;
                IKSGRenderUtil.matrixScalf(matx, fs);
                int fx = getTexturStartX() + 126;
                float fy = getTexturStartY() + 47.5f;
                IKSGRenderUtil.matrixTranslatef(matx, (fx / fs) - fx, (fy / fs) - fy, 0);
                IKSGRenderUtil.drawString(this.font, matx, new StringTextComponent("100"), fx, (int) fy, 2722312);
                IKSGRenderUtil.matrixPop(matx);

                if (boomboxTile.isPlayWaiting()) {
                    IKSGRenderUtil.drawString(this.font, matx, new TranslationTextComponent("boombox.loading"), getTexturStartX() + 29, getTexturStartY() + 47, 2722312);
                } else {
                    IKSGRenderUtil.drawString(this.font, matx, new StringTextComponent(StringUtils.getTimeNotationPercentage(boomboxTile.getCurrentMusicPlayPosition(), boomboxTile.getMusicDuration())), getTexturStartX() + 29, getTexturStartY() + 47, 2722312);
                }
            }
        }
    }

    protected void insMode(BoomboxMode mode) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("name", mode.getName());
        this.instruction("Mode", tag);
    }

    protected void insLoop(boolean loop) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("enble", loop);
        this.instruction("Loop", tag);
    }

    protected void insStop() {
        this.instruction("Stop", new CompoundNBT());
    }

    private ItemStack getCassetteTape() {
        return ((BoomboxTileEntity) getTileEntity()).getCassetteTape();
    }

    private static class BoomboxButton extends Button {
        private final int buttunNum;
        private final IWhetherPresseble whetherpresseble;

        public BoomboxButton(int x, int y, int buttonNum, IPressable pressedAction, IWhetherPresseble whetherpresseble) {
            super(x, y, 22, 17, new TranslationTextComponent("narrator.button.boombox"), pressedAction);
            this.buttunNum = buttonNum;
            this.whetherpresseble = whetherpresseble;
        }

        public void renderButton(MatrixStack matx, int mouseX, int mouseY, float partialTicks) {
            IKSGRenderUtil.guiBindAndBlit(BOOMBOX_GUI_TEXTURES, matx, this.x, this.y, buttunNum * 22, (this.isHovered() ? 17 : 0) + (whetherpresseble.isPresseble() ? 34 : 0) + 165, 22, 17, 256, 256);
        }

        @OnlyIn(Dist.CLIENT)
        public interface IWhetherPresseble {
            boolean isPresseble();
        }
    }
}
