package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.gui.widget.PlayListScrollButton;
import red.felnull.imp.client.gui.widget.PlayMusicScrollButton;
import red.felnull.imp.container.CassetteDeckContainer;
import red.felnull.imp.musicplayer.PlayList;
import red.felnull.imp.tileentity.CassetteDeckTileEntity;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.StringImageButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGScreenUtil;

import java.util.Arrays;

public class CassetteDeckScreen extends IMPAbstractPLEquipmentScreen<CassetteDeckContainer> {
    public static final ResourceLocation CD_GUI_TEXTURES = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/cassette_deck.png");

    private StringImageButton playButton;
    private StringImageButton writeButton;
    private StringImageButton eraseButton;
    private StringImageButton copyButton;
    private StringImageButton allPlayListButton;
    private ScrollBarSlider playListBar;
    private ScrollBarSlider playMusicBar;
    private PlayListScrollButton playListButtons;
    private PlayMusicScrollButton playMusicButtons;

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

        this.playButton = addSmartStringButton(new TranslationTextComponent("cd.play"), (getMonitorXsize() / 4 - 48) / 2, getMonitorYsize() - 16, n -> insScreen(CassetteDeckTileEntity.Screen.PLAY));

        this.writeButton = addSmartStringButton(new TranslationTextComponent("cd.write"), (getMonitorXsize() / 4) + (getMonitorXsize() / 4 - 48) / 2, getMonitorYsize() - 16, n -> insScreen(CassetteDeckTileEntity.Screen.WRITE_1));

        this.eraseButton = addSmartStringButton(new TranslationTextComponent("cd.erase"), (getMonitorXsize() / 2) + (getMonitorXsize() / 4 - 48) / 2, getMonitorYsize() - 16, n -> insScreen(CassetteDeckTileEntity.Screen.ERASE));

        this.copyButton = addSmartStringButton(new TranslationTextComponent("cd.copy"), (getMonitorXsize() / 4 * 3) + (getMonitorXsize() / 4 - 48) / 2, getMonitorYsize() - 16, n -> insScreen(CassetteDeckTileEntity.Screen.COPY));

        this.allPlayListButton = addStringImageButton(new TranslationTextComponent("playlist.all"), 1, 1, 10, 10, 36, 40, n -> setCurrentSelectedPlayList(PlayList.ALL));

        this.playListBar = this.addWidgetByIKSG(new ScrollBarSlider(getMonitorStartX() + 12, getMonitorStartY() + 12, 56, 100, 0, -12, 0, 76, EQUIPMENT_WIDGETS_TEXTURES));
        IKSGScreenUtil.setVisible(this.playListBar, false);

        this.playMusicBar = this.addWidgetByIKSG(new ScrollBarSlider(getMonitorStartX() + 190, getMonitorStartY() + 12, 56, 100, 0, -168, 0, 76, EQUIPMENT_WIDGETS_TEXTURES));
        IKSGScreenUtil.setVisible(this.playMusicBar, false);

        this.playListButtons = this.addWidgetByIKSG(new PlayListScrollButton(getMonitorStartX() + 1, getMonitorStartY() + 12, 56, playListBar, (n, m) -> {
            setCurrentSelectedPlayList(getJonedAllPlayLists().get(m));
            updateAll();
        }, this, true));
        IKSGScreenUtil.setVisible(this.playListButtons, false);

        this.playMusicButtons = this.addWidgetByIKSG(new PlayMusicScrollButton(getMonitorStartX() + 22, getMonitorStartY() + 12, 167, 56, 14, playMusicBar, (n, m) -> {

        }, this, null, true));
        IKSGScreenUtil.setVisible(this.playMusicButtons, false);

    }

    @Override
    public int getMonitorStartX() {
        return getTexturStartX() + 7;
    }

    @Override
    public int getMonitorStartY() {
        return getTexturStartY() + 19;
    }

    @Override
    public int getMonitorXsize() {
        return 200;
    }

    @Override
    public int getMonitorYsize() {
        return 80;
    }

    @Override
    protected void drawGuiContainerBackgroundLayerByIKSG(MatrixStack matx, float partTick, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayerByIKSG(matx, partTick, mouseX, mouseY);
        IKSGRenderUtil.matrixPush(matx);
        IKSGRenderUtil.guiBindAndBlit(getCurrentScreen().getTexLocation(), matx, getMonitorStartX(), getMonitorStartY(), 0, 0, getMonitorXsize(), getMonitorYsize(), getMonitorXsize(), getMonitorYsize());
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
        IKSGScreenUtil.setVisible(this.playButton, isScreen(CassetteDeckTileEntity.Screen.SELECTION));
        IKSGScreenUtil.setVisible(this.writeButton, isScreen(CassetteDeckTileEntity.Screen.SELECTION));
        IKSGScreenUtil.setVisible(this.eraseButton, isScreen(CassetteDeckTileEntity.Screen.SELECTION));
        IKSGScreenUtil.setVisible(this.copyButton, isScreen(CassetteDeckTileEntity.Screen.SELECTION));
        IKSGScreenUtil.setVisible(this.allPlayListButton, isScreen(CassetteDeckTileEntity.Screen.WRITE_1));
        IKSGScreenUtil.setVisible(this.playListBar, isScreen(CassetteDeckTileEntity.Screen.WRITE_1));
        IKSGScreenUtil.setVisible(this.playMusicBar, isScreen(CassetteDeckTileEntity.Screen.WRITE_1));
        IKSGScreenUtil.setVisible(this.playListButtons, isScreen(CassetteDeckTileEntity.Screen.WRITE_1));
        IKSGScreenUtil.setVisible(this.playMusicButtons, isScreen(CassetteDeckTileEntity.Screen.WRITE_1));
    }

    protected CassetteDeckTileEntity.Screen getCurrentScreen() {
        return ((CassetteDeckTileEntity) getTileEntity()).getScreen();
    }

    private boolean isScreen(CassetteDeckTileEntity.Screen... mo) {
        return isSlectedScreen(getCurrentScreen(), mo);
    }

    private boolean isSlectedScreen(CassetteDeckTileEntity.Screen mo, CassetteDeckTileEntity.Screen... mos) {
        return Arrays.asList(mos).contains(mo);
    }

    public void insScreen(CassetteDeckTileEntity.Screen screen) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("name", screen.getName());
        this.instruction("Mode", tag);
    }
}
