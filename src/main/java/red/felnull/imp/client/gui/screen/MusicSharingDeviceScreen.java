package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.block.MusicSharingDeviceBlock;
import red.felnull.imp.client.gui.widget.MSDScrollBarSlider;
import red.felnull.imp.container.MusicSharingDeviceContainer;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.item.IMPItems;
import red.felnull.imp.tileentity.MusicSharingDeviceTileEntity;
import red.felnull.otyacraftengine.client.gui.IkisugiDialogTexts;
import red.felnull.otyacraftengine.client.gui.screen.AbstractIkisugiContainerScreen;
import red.felnull.otyacraftengine.client.gui.widget.ChangeableImageButton;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.ScrollListButton;
import red.felnull.otyacraftengine.client.gui.widget.StringImageButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGScreenUtil;
import red.felnull.otyacraftengine.client.util.IKSGTextureUtil;
import red.felnull.otyacraftengine.util.IKSGPictuerUtil;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;
import red.felnull.otyacraftengine.util.IKSGStyles;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicSharingDeviceScreen extends AbstractIkisugiContainerScreen<MusicSharingDeviceContainer> {

    public static final ResourceLocation MSD_GUI_TEXTURES = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device_1.png");
    public static final ResourceLocation MSD_GUI_TEXTURES2 = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device_2.png");
    //  private static final ResourceLocation fontLocation = new ResourceLocation(IamMusicPlayer.MODID, "msd");
    private static final ResourceLocation fontLocation = new ResourceLocation("minecraft", "default");
    private static final Style fontStyle = IKSGStyles.withFont(fontLocation);

    private byte[] picturImage;
    private boolean loading;
    private int picturWidth;
    private int picturHeight;

    private ChangeableImageButton powerButton;
    private StringImageButton allbutton;
    private ImageButton addGuildButton;
    private ScrollBarSlider guildlistbar;
    private ScrollBarSlider playlistbar;
    private ScrollListButton guildButtons;
    private ScrollListButton playlistButtons;
    private TextFieldWidget addGuildNameField;
    private StringImageButton backGuid;
    private StringImageButton createGuid;

    private String listname;

    private Monitors Monitorsa;

    public MusicSharingDeviceScreen(MusicSharingDeviceContainer screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
        this.xSize = 215;
        this.ySize = 242;
        setMonitorsa();
        setListName();
    }

    protected int getMonitorStartX() {
        return getTexturStartX() + 8;
    }

    protected int getMonitorStartY() {
        return getTexturStartY() + 20;
    }

    protected int getMonitorXsize() {
        return 199;
    }

    protected int getMonitorYsize() {
        return 122;
    }

    @Override
    public void initByIKSG() {
        super.initByIKSG();
        instruction("opengui", new CompoundNBT());
        this.loading = false;
        this.field_238745_s_ = this.ySize - 94;
        this.powerButton = this.addWidgetByIKSG(new ChangeableImageButton(getTexturStartX() + 181, getTexturStartY() + 202, 20, 20, 215, 0, 20, MSD_GUI_TEXTURES, 256, 256, n -> {
            insPower(!this.isStateOn());
        }));
        if (isStateOn()) {
            powerButton.setTextuer(235, 0, 20, 256, 256);
        } else {
            powerButton.setTextuer(215, 0, 20, 256, 256);
        }
        this.allbutton = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + 1, getMonitorStartY() + 1, 18, 18, 215, 60, 18, MSD_GUI_TEXTURES, n -> {
        }, IKSGStyles.withStyle(new TranslationTextComponent("msd.all"), fontStyle)));
        this.allbutton.setSizeAdjustment(true);
        this.allbutton.setShadwString(false);
        this.allbutton.setStringColor(0);
        IKSGScreenUtil.setVisible(this.allbutton, false);

        this.addGuildButton = this.addWidgetByIKSG(new ImageButton(getMonitorStartX() + 20, getMonitorStartY() + 1, 9, 18, 235, 40, 18, MSD_GUI_TEXTURES, n -> {
            insMode("addplaylist");
        }));
        IKSGScreenUtil.setVisible(this.addGuildButton, false);

        this.guildlistbar = this.addWidgetByIKSG(new MSDScrollBarSlider(getMonitorStartX() + 20, getMonitorStartY() + 20, 101, 100, 0, -20));
        IKSGScreenUtil.setVisible(this.guildlistbar, false);

        this.playlistbar = this.addWidgetByIKSG(new MSDScrollBarSlider(getMonitorStartX() + 189, getMonitorStartY() + 20, 101, 100, 0, -159));
        IKSGScreenUtil.setVisible(this.playlistbar, false);

        List<ResourceLocation> locations = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            locations.add(MSD_GUI_TEXTURES);
        }
        this.guildButtons = this.addWidgetByIKSG(new ScrollListButton(getMonitorStartX() + 1, getMonitorStartY() + 20, 18, 101, 18, 30, guildlistbar, locations, (n, m) -> {
            System.out.println(m);
        }));
        IKSGScreenUtil.setVisible(this.guildButtons, false);

        this.playlistButtons = this.addWidgetByIKSG(new ScrollListButton(getMonitorStartX() + 30, getMonitorStartY() + 20, 158, 101, 40, 30, playlistbar, locations, (n, m) -> {
            System.out.println(m);
        }));
        IKSGScreenUtil.setVisible(this.playlistButtons, false);

        this.backGuid = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + 92, getMonitorStartY() + 92, 48, 15, 0, 0, 15, MSD_GUI_TEXTURES2, n -> {
            insMode("playlist");
        }, IKSGStyles.withStyle((TranslationTextComponent) IkisugiDialogTexts.BACK, fontStyle)));
        IKSGScreenUtil.setVisible(this.backGuid, false);

        this.createGuid = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + 145, getMonitorStartY() + 92, 48, 15, 0, 0, 15, MSD_GUI_TEXTURES2, n -> {
            PlayListGuildManeger.instance().createPlayListRequest(addGuildNameField.getText(), picturImage);
            insMode("playlist");
        }, IKSGStyles.withStyle((TranslationTextComponent) IkisugiDialogTexts.CRATE, fontStyle)));
        IKSGScreenUtil.setVisible(this.createGuid, false);


        this.addGuildNameField = this.addWidgetByIKSG(new TextFieldWidget(this.field_230712_o_, getMonitorStartX() + 95, getMonitorStartY() + 29, 96, 12, new StringTextComponent("test")));
        this.addGuildNameField.setEnableBackgroundDrawing(false);
        this.addGuildNameField.setMaxStringLength(30);
        this.addGuildNameField.setTextColor(-1);
        this.addGuildNameField.setDisabledTextColour(-1);
        IKSGScreenUtil.setVisible(this.addGuildNameField, false);

        if (isMonitor(Monitors.ADDPLAYLIST)) {
            Path picPath = getPicturPath();
            if (picPath != null) {
                PictureLoadThread plt = new PictureLoadThread(this, picPath);
                plt.start();
            } else {
                instruction("pathset", new CompoundNBT());
            }
        } else {
            instruction("pathset", new CompoundNBT());
        }

    }

    @Override
    public boolean func_231045_a_(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_, double p_231045_8_) {
        boolean flag1 = super.func_231045_a_(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
        boolean flag2 = this.func_241217_q_() != null && this.func_231041_ay__() && p_231045_5_ == 0 ? this.func_241217_q_().func_231045_a_(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_) : false;
        return flag1 & flag2;
    }


    @Override
    protected void drawGuiContainerBackgroundLayerByIKSG(MatrixStack matx, float partTick, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayerByIKSG(matx, partTick, mouseX, mouseY);
        IKSGRenderUtil.matrixPush(matx);
        IKSGRenderUtil.guiBindAndBlit(Monitorsa.getTextuer(), matx, getMonitorStartX(), getMonitorStartY(), 0, 0, 199, 122, 199, 122);
        IKSGRenderUtil.matrixPop(matx);
        switch (Monitorsa) {
            case NOANTENNA:
                drawNoAntenna(matx, partTick, mouseX, mouseY);
                break;
            case PLAYLIST:
                drawPlayList(matx, partTick, mouseX, mouseY);
                break;
            case ADDPLAYLIST:
                drawAddPlayList(matx, partTick, mouseX, mouseY);
                break;
        }
    }

    @Override
    public void tickByIKSG() {
        setMonitorsa();
        if (isStateOn()) {
            powerButton.setTextuer(235, 0, 20, 256, 256);
        } else {
            powerButton.setTextuer(215, 0, 20, 256, 256);
        }
        if (!isMonitor(Monitors.ADDPLAYLIST) && picturImage != null) {
            picturImage = null;
        }
        if (!isMonitor(Monitors.ADDPLAYLIST)) {
            addGuildNameField.setText("");
        } else {
            addGuildNameField.tick();
        }
        IKSGScreenUtil.setVisible(this.allbutton, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.addGuildButton, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.guildlistbar, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.playlistbar, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.guildButtons, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.playlistButtons, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.addGuildNameField, isMonitor(Monitors.ADDPLAYLIST));
        IKSGScreenUtil.setVisible(this.backGuid, isMonitor(Monitors.ADDPLAYLIST));
        IKSGScreenUtil.setVisible(this.createGuid, isMonitor(Monitors.ADDPLAYLIST));

    }

    private boolean isMonitor(Monitors... mo) {
        return Arrays.asList(mo).contains(Monitorsa);
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

    public void insMode(String modename) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("name", modename);
        this.instruction("mode", tag);
    }

    public void insPower(boolean on) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("on", on);
        this.instruction("power", tag);
    }


    private void setMonitorsa() {
        if (!isStateOn()) {
            Monitorsa = Monitors.OFF;
        } else if (getMode() == null) {
            Monitorsa = Monitors.OFF;
        } else if (getMode().equals("noantenna")) {
            Monitorsa = Monitors.NOANTENNA;
        } else if (getMode().equals("playlist")) {
            Monitorsa = Monitors.PLAYLIST;
        } else if (getMode().equals("addplaylist")) {
            Monitorsa = Monitors.ADDPLAYLIST;
        } else {
            Monitorsa = Monitors.ON;
        }
    }

    private void setListName() {
        listname = I18n.format("msd.all");

    }

    protected void drawAddPlayList(MatrixStack matrx, float partTick, int mouseX, int mouseY) {
        drawFontString(matrx, new TranslationTextComponent("msd.addplaylist"), getMonitorStartX() + 2, getMonitorStartY() + 2);
        drawFontString(matrx, new TranslationTextComponent("msd.image"), getMonitorStartX() + 6, getMonitorStartY() + 17);
        drawFontString(matrx, new TranslationTextComponent("msd.name"), getMonitorStartX() + 92, getMonitorStartY() + 17);
        if (loading) {
            IKSGRenderUtil.matrixPush(matrx);
            RenderSystem.enableBlend();
            IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getLoadingIconTextuer(), matrx, getMonitorStartX() + 5, getMonitorStartY() + 108, 0, 0, 8, 8, 8, 8);
            IKSGRenderUtil.matrixPop(matrx);
            drawFontString(matrx, new TranslationTextComponent("msd.imageloading"), getMonitorStartX() + 6 + 9, getMonitorStartY() + 109);
        } else {
            drawFontString(matrx, new TranslationTextComponent("msd.imagedropInfo"), getMonitorStartX() + 6, getMonitorStartY() + 109);
        }

        if (picturImage != null) {
            int xsize = (int) (79 * ((float) picturWidth / 256f));
            int ysize = (int) (79 * ((float) picturHeight / 256f));
            int x = (79 - xsize) / 2;
            int y = (79 - ysize) / 2;
            IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getPictureImageTexture(picturImage), matrx, getMonitorStartX() + 7 + x, getMonitorStartY() + 27 + y, 0, 0, xsize, ysize, xsize, ysize);
        }

        IKSGRenderUtil.matrixPush(matrx);
        addGuildNameField.func_230430_a_(matrx, mouseX, mouseY, partTick);
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        IKSGRenderUtil.matrixPop(matrx);
    }

    protected void drawPlayList(MatrixStack matrx, float partTick, int mouseX, int mouseY) {
        drawFontString(matrx, new StringTextComponent(listname), getMonitorStartX() + 31, getMonitorStartY() + 2);
    }

    protected void drawNoAntenna(MatrixStack matrx, float partTick, int mouseX, int mouseY) {
        drawCenterFontString(matrx, new TranslationTextComponent("msd.noantenna"), getMonitorStartX() + getMonitorXsize() / 2, getTexturStartY() + 70);
        ItemRenderer ir = getMinecraft().getItemRenderer();
        ir.zLevel = 100.0F;
        ir.renderItemAndEffectIntoGUI(new ItemStack(IMPItems.PARABOLIC_ANTENNA), getTexturStartX() + getXSize() / 2 - 8, getTexturStartY() + 85);
        ir.zLevel = 0.0F;
        IKSGRenderUtil.matrixPush(matrx);
        IKSGRenderUtil.matrixTranslatef(matrx, 0, 0, 500);
        IKSGRenderUtil.guiBindAndBlit(MSD_GUI_TEXTURES, matrx, getTexturStartX() + getXSize() / 2 - 10, getTexturStartY() + 83, 215, 40, 20, 20, 256, 256);
        IKSGRenderUtil.matrixPop(matrx);
    }

    protected void drawCenterFontString(MatrixStack matx, IFormattableTextComponent text, int x, int y) {
        IKSGRenderUtil.drawCenterString(this.field_230712_o_, matx, IKSGStyles.withStyle(text, fontStyle), x, y, 0);
    }

    protected void drawFontString(MatrixStack matx, IFormattableTextComponent text, int x, int y) {
        IKSGRenderUtil.drawString(this.field_230712_o_, matx, IKSGStyles.withStyle(text, fontStyle), x, y, 0);
    }

    private static enum Monitors {
        OFF(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_off.png")),
        ON(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_on.png")),
        PLAYLIST(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_list.png")),
        NOANTENNA(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_noantenna.png")),
        ADDPLAYLIST(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_addplaylist.png"));
        private final ResourceLocation location;

        Monitors(ResourceLocation location) {
            this.location = location;
        }

        public ResourceLocation getTextuer() {
            return this.location;
        }
    }

    private Path getPicturPath() {
        String pathst = ((MusicSharingDeviceTileEntity) getTileEntity()).getPlayerPath(IKSGPlayerUtil.getUUID(getMinecraft().player));
        if (pathst != null) {
            try {
                Path pa = Paths.get(pathst);
                if (pa.toFile().exists())
                    return pa;
            } catch (Exception ex) {
            }
        }
        return null;
    }

    @Override
    public void dropAndDragByIKSG(List<Path> dragFiles) {
        if (isMonitor(Monitors.ADDPLAYLIST)) {
            if (dragFiles.size() == 1 && !loading) {
                PictureLoadThread lt = new PictureLoadThread(this, dragFiles.get(0));
                lt.start();
                CompoundNBT tag = new CompoundNBT();
                tag.putString("path", dragFiles.get(0).toString());
                instruction("pathset", tag);
            }
        }
    }

    public void setPicturImage(byte[] picturImage) {
        this.picturImage = picturImage;
    }

    private static class PictureLoadThread extends Thread {
        private MusicSharingDeviceScreen screen;
        private Path path;

        public PictureLoadThread(MusicSharingDeviceScreen screen, Path path) {
            this.screen = screen;
            this.path = path;
        }

        public void run() {
            screen.loading = true;
            try {
                BufferedImage bfi = IKSGPictuerUtil.getBffImage(path);
                if (bfi != null) {
                    int size = 256;
                    float w = bfi.getWidth();
                    float h = bfi.getHeight();
                    int aw = 0;
                    int ah = 0;
                    if (w == h) {
                        aw = size;
                        ah = size;
                    } else if (w > h) {
                        aw = size;
                        ah = (int) ((float) size * (h / w));
                    } else if (w < h) {
                        aw = (int) ((float) size * (w / h));
                        ah = size;
                    }
                    BufferedImage outbfi = new BufferedImage(aw, ah, bfi.getType());
                    outbfi.createGraphics().drawImage(bfi.getScaledInstance(aw, ah, Image.SCALE_AREA_AVERAGING), 0, 0, aw, ah, null);
                    screen.picturWidth = outbfi.getWidth();
                    screen.picturHeight = outbfi.getHeight();
                    screen.setPicturImage(IKSGPictuerUtil.geByteImage(outbfi));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            screen.loading = false;
        }
    }
}
