package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
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
import red.felnull.imp.client.data.MusicSourceClientReferencesType;
import red.felnull.imp.client.gui.widget.GuildPlayListButton;
import red.felnull.imp.client.gui.widget.JoinPlayListButton;
import red.felnull.imp.client.gui.widget.PlayMusicSourceReferenceButton;
import red.felnull.imp.client.util.RenderUtil;
import red.felnull.imp.container.MusicSharingDeviceContainer;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.item.IMPItems;
import red.felnull.imp.musicplayer.PlayImage;
import red.felnull.imp.musicplayer.PlayList;
import red.felnull.imp.tileentity.MusicSharingDeviceTileEntity;
import red.felnull.imp.util.PathUtil;
import red.felnull.otyacraftengine.client.gui.IkisugiDialogTexts;
import red.felnull.otyacraftengine.client.gui.screen.AbstractIkisugiContainerScreen;
import red.felnull.otyacraftengine.client.gui.widget.Checkbox;
import red.felnull.otyacraftengine.client.gui.widget.*;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGScreenUtil;
import red.felnull.otyacraftengine.client.util.IKSGTextureUtil;
import red.felnull.otyacraftengine.util.*;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MusicSharingDeviceScreen extends AbstractIkisugiContainerScreen<MusicSharingDeviceContainer> {

    public static final ResourceLocation MSD_GUI_TEXTURES = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device_1.png");
    public static final ResourceLocation MSD_GUI_TEXTURES2 = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device_2.png");
    //  private static final ResourceLocation fontLocation = new ResourceLocation(IamMusicPlayer.MODID, "msd");
    private static final ResourceLocation fontLocation = new ResourceLocation("minecraft", "default");
    public static final Style fontStyle = IKSGStyles.withFont(fontLocation);

    private List<PlayList> jonPlaylists = new ArrayList<>();
    private List<PlayList> jonedAllPlaylists = new ArrayList<>();

    private PlayImage image;

    private byte[] picturImage;
    private boolean pictuerLoading;
    private boolean musicLoading;
    private MusicLoadResult musicLoadError;

    private SourceCheckThread sourceCheckThread;
    private MusicSourceClientReferencesType musicSourceClientReferencesType;

    private ChangeableImageButton powerButton;
    private StringImageButton allbutton;
    private ImageButton addGuildButton;
    private ScrollBarSlider guildlistbar;
    private ScrollBarSlider playlistbar;
    private GuildPlayListButton guildButtons;
    private ScrollListButton playlistButtons;
    private TextFieldWidget createGuildNameField;
    private Checkbox createAnyoneCheckbox;
    private StringImageButton backGuid;
    private StringImageButton createGuid;
    private StringImageButton createJoinGuid;
    private StringImageButton addJoinGuid;
    private StringImageButton backJoinGuid;
    private ScrollBarSlider joinplaylistbar;
    private JoinPlayListButton joinplaylistButtons;
    private StringImageButton joinplaylistbackButton;
    private ImageButton addPlayMusicButton;
    private TextFieldWidget addPlayMusicNameField;
    public TextFieldWidget addPlayMusicSourceField;
    private PlayMusicSourceReferenceButton addPlayMusicSourceReferenceButton;

    private String listname;

    private Monitors Monitorsa;

    protected ClockTimer timer;

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
        updatePlayList();
        timerSet();
        instruction("opengui", new CompoundNBT());
        this.musicSourceClientReferencesType = MusicSourceClientReferencesType.LOCAL_FILE;
        this.pictuerLoading = false;
        this.musicLoading = false;
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
            insMode(Monitors.ADDPLAYLIST);
        }));
        IKSGScreenUtil.setVisible(this.addGuildButton, false);

        this.guildlistbar = this.addWidgetByIKSG(new ScrollBarSlider(getMonitorStartX() + 20, getMonitorStartY() + 20, 101, 100, 0, -20, 215, 126, MSD_GUI_TEXTURES));
        IKSGScreenUtil.setVisible(this.guildlistbar, false);

        this.playlistbar = this.addWidgetByIKSG(new ScrollBarSlider(getMonitorStartX() + 189, getMonitorStartY() + 20, 101, 100, 0, -159, 215, 126, MSD_GUI_TEXTURES));
        IKSGScreenUtil.setVisible(this.playlistbar, false);

        List<ResourceLocation> locations = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            locations.add(MSD_GUI_TEXTURES);
        }
        this.guildButtons = this.addWidgetByIKSG(new GuildPlayListButton(getMonitorStartX() + 1, getMonitorStartY() + 20, 18, 101, 18, guildlistbar, jonedAllPlaylists, (n, m) -> {
            System.out.println(m);
        }));
        IKSGScreenUtil.setVisible(this.guildButtons, false);

        this.playlistButtons = this.addWidgetByIKSG(new ScrollListButton(getMonitorStartX() + 30, getMonitorStartY() + 20, 158, 101, 40, 30, playlistbar, locations, (n, m) -> {
            System.out.println(m);
        }));
        IKSGScreenUtil.setVisible(this.playlistButtons, false);

        this.backGuid = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + 92, getMonitorStartY() + 92, 48, 15, 0, 0, 15, MSD_GUI_TEXTURES2, n -> {
            insMode(Monitors.PLAYLIST);
        }, IKSGStyles.withStyle((TranslationTextComponent) IkisugiDialogTexts.BACK, fontStyle)));
        this.backGuid.setSizeAdjustment(true);
        this.backGuid.setShadwString(false);
        this.backGuid.setStringColor(0);
        IKSGScreenUtil.setVisible(this.backGuid, false);

        this.createGuid = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + 145, getMonitorStartY() + 92, 48, 15, 0, 0, 15, MSD_GUI_TEXTURES2, n -> {

            if (isMonitor(Monitors.CREATEPLAYLIST)) {
                PlayListGuildManeger.instance().createPlayListRequest(createGuildNameField.getText(), image, picturImage, createAnyoneCheckbox.isCheck());
            }

            insMode(Monitors.PLAYLIST);
        }, IKSGStyles.withStyle((TranslationTextComponent) IkisugiDialogTexts.CRATE, fontStyle)));
        this.createGuid.setSizeAdjustment(true);
        this.createGuid.setShadwString(false);
        this.createGuid.setStringColor(0);
        IKSGScreenUtil.setVisible(this.createGuid, false);

        this.createGuildNameField = this.addWidgetByIKSG(new TextFieldWidget(this.field_230712_o_, getMonitorStartX() + 95, getMonitorStartY() + 29, 91, 12, new StringTextComponent("test")));
        this.createGuildNameField.setEnableBackgroundDrawing(false);
        this.createGuildNameField.setMaxStringLength(30);
        this.createGuildNameField.setTextColor(-1);
        this.createGuildNameField.setDisabledTextColour(-1);
        this.createGuildNameField.setResponder(n -> {
            if (isMonitor(Monitors.CREATEPLAYLIST) && picturImage == null) {
                this.image = new PlayImage(PlayImage.ImageType.STRING, n);
            }
        });
        IKSGScreenUtil.setVisible(this.createGuildNameField, false);

        this.createAnyoneCheckbox = this.addWidgetByIKSG(new Checkbox(getMonitorStartX() + 92, getMonitorStartY() + 56, 15, 15, 215, 96, 256, 256, MSD_GUI_TEXTURES));
        IKSGScreenUtil.setVisible(this.createAnyoneCheckbox, false);

        this.createJoinGuid = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + getMonitorXsize() / 2 - 48 - 5, getMonitorStartY() + getMonitorYsize() / 2, 48, 15, 0, 0, 15, MSD_GUI_TEXTURES2, n -> {
            insMode(Monitors.CREATEPLAYLIST);
        }, IKSGStyles.withStyle((TranslationTextComponent) IkisugiDialogTexts.CRATE, fontStyle)));
        this.createJoinGuid.setSizeAdjustment(true);
        this.createJoinGuid.setShadwString(false);
        this.createJoinGuid.setStringColor(0);
        IKSGScreenUtil.setVisible(this.createJoinGuid, false);

        this.addJoinGuid = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + getMonitorXsize() / 2 + 5, getMonitorStartY() + getMonitorYsize() / 2, 48, 15, 0, 0, 15, MSD_GUI_TEXTURES2, n -> {
            insMode(Monitors.JOINPLAYLIST);
        }, IKSGStyles.withStyle((TranslationTextComponent) IkisugiDialogTexts.JOIN, fontStyle)));
        this.addJoinGuid.setSizeAdjustment(true);
        this.addJoinGuid.setShadwString(false);
        this.addJoinGuid.setStringColor(0);
        IKSGScreenUtil.setVisible(this.addJoinGuid, false);

        this.backJoinGuid = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + getMonitorXsize() / 2 - 24, getMonitorStartY() + getMonitorYsize() / 2 + 18, 48, 15, 0, 0, 15, MSD_GUI_TEXTURES2, n -> {
            insMode(Monitors.PLAYLIST);
        }, IKSGStyles.withStyle((TranslationTextComponent) IkisugiDialogTexts.BACK, fontStyle)));
        this.backJoinGuid.setSizeAdjustment(true);
        this.backJoinGuid.setShadwString(false);
        this.backJoinGuid.setStringColor(0);
        IKSGScreenUtil.setVisible(this.backJoinGuid, false);

        this.joinplaylistbar = this.addWidgetByIKSG(new ScrollBarSlider(getMonitorStartX() + 189, getMonitorStartY() + 20, 101, 100, 0, -187, 215, 126, MSD_GUI_TEXTURES));
        IKSGScreenUtil.setVisible(this.joinplaylistbar, false);

        this.joinplaylistButtons = this.addWidgetByIKSG(new JoinPlayListButton(getMonitorStartX() + 1, getMonitorStartY() + 20, 187, 101, 40, joinplaylistbar, jonPlaylists, (n, m) -> {
            PlayListGuildManeger.instance().joinPlayListRequest(jonPlaylists.get(m).getUUID());
            insMode(Monitors.PLAYLIST);
        }));
        IKSGScreenUtil.setVisible(this.joinplaylistButtons, false);

        this.joinplaylistbackButton = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + 1, getMonitorStartY() + 12, 14, 7, 0, 30, 14, MSD_GUI_TEXTURES2, n -> {
            insMode(Monitors.PLAYLIST);
        }, IKSGStyles.withStyle((TranslationTextComponent) IkisugiDialogTexts.BACK, fontStyle)));
        this.joinplaylistbackButton.setSizeAdjustment(true);
        this.joinplaylistbackButton.setShadwString(false);
        this.joinplaylistbackButton.setStringColor(0);
        this.joinplaylistbackButton.setScale(0.5f);
        IKSGScreenUtil.setVisible(this.joinplaylistbackButton, false);

        this.addPlayMusicButton = this.addWidgetByIKSG(new ImageButton(getMonitorStartX() + 189, getMonitorStartY() + 1, 9, 18, 235, 40, 18, MSD_GUI_TEXTURES, n -> {
            insMode(Monitors.ADDPLAYMUSIC1);
        }));
        IKSGScreenUtil.setVisible(this.addPlayMusicButton, false);

        this.addPlayMusicNameField = this.addWidgetByIKSG(new TextFieldWidget(this.field_230712_o_, getMonitorStartX() + 95, getMonitorStartY() + 29, 91, 12, new StringTextComponent("test")));
        this.addPlayMusicNameField.setEnableBackgroundDrawing(false);
        this.addPlayMusicNameField.setMaxStringLength(100);
        this.addPlayMusicNameField.setTextColor(-1);
        this.addPlayMusicNameField.setDisabledTextColour(-1);
        this.addPlayMusicNameField.setResponder(n -> {
            if (isMonitor(Monitors.ADDPLAYMUSIC1) && picturImage == null) {
                this.image = new PlayImage(PlayImage.ImageType.STRING, n);
            }
        });
        IKSGScreenUtil.setVisible(this.addPlayMusicNameField, false);

        this.addPlayMusicSourceField = this.addWidgetByIKSG(new TextFieldWidget(this.field_230712_o_, getMonitorStartX() + 95, getMonitorStartY() + 55, 73, 12, new StringTextComponent("test")));
        this.addPlayMusicSourceField.setEnableBackgroundDrawing(false);
        this.addPlayMusicSourceField.setMaxStringLength(Integer.MAX_VALUE);
        this.addPlayMusicSourceField.setTextColor(-1);
        this.addPlayMusicSourceField.setDisabledTextColour(-1);
        this.addPlayMusicSourceField.setResponder(n -> {
            if (isMonitor(Monitors.ADDPLAYMUSIC1)) {
                if (this.sourceCheckThread != null)
                    this.sourceCheckThread.setStop(true);
                this.sourceCheckThread = new SourceCheckThread(this, n);
                this.sourceCheckThread.start();
            }
        });
        IKSGScreenUtil.setVisible(this.addPlayMusicSourceField, false);

        this.addPlayMusicSourceReferenceButton = this.addWidgetByIKSG(new PlayMusicSourceReferenceButton(getMonitorStartX() + 175, getMonitorStartY() + 52, 18, 15, 215, 168, 15, MSD_GUI_TEXTURES, n -> {

        }, this));
        IKSGScreenUtil.setVisible(this.addPlayMusicSourceReferenceButton, false);

        if (isMonitor(Monitors.CREATEPLAYLIST, Monitors.ADDPLAYMUSIC1)) {
            Path picPath = getPicturPath();
            if (picPath != null) {
                DropAndDragFileLoadThread plt = new DropAndDragFileLoadThread(true, this, picPath);
                plt.start();
            } else {
                instruction("pathset", new CompoundNBT());
            }
        } else {
            instruction("pathset", new CompoundNBT());
        }

        if (image == null) {
            image = new PlayImage(PlayImage.ImageType.STRING, "");
        }

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
            case CREATEPLAYLIST:
                drawCreatePlaylist(matx, partTick, mouseX, mouseY);
                break;
            case ADDPLAYLIST:
                drawAddPlaylist(matx, partTick, mouseX, mouseY);
                break;
            case JOINPLAYLIST:
                drawJoinPlaylist(matx, partTick, mouseX, mouseY);
                break;
            case ADDPLAYMUSIC1:
                drawAddPlayMusic1(matx, partTick, mouseX, mouseY);
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
        if (!isMonitor(Monitors.CREATEPLAYLIST)) {
            createGuildNameField.setText("");
        } else {
            createGuildNameField.tick();
        }
        if (!isMonitor(Monitors.ADDPLAYMUSIC1)) {
            addPlayMusicNameField.setText("");
            addPlayMusicSourceField.setText("");
            if (musicLoadError != null)
                musicLoadError = null;
            if (sourceCheckThread != null && !sourceCheckThread.isStop())
                sourceCheckThread.setStop(true);
        } else {
            addPlayMusicNameField.tick();
            addPlayMusicSourceField.tick();
        }
        IKSGScreenUtil.setVisible(this.allbutton, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.addGuildButton, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.guildlistbar, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.playlistbar, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.guildButtons, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.playlistButtons, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.createGuildNameField, isMonitor(Monitors.CREATEPLAYLIST));
        IKSGScreenUtil.setVisible(this.backGuid, isMonitor(Monitors.CREATEPLAYLIST, Monitors.ADDPLAYMUSIC1));
        IKSGScreenUtil.setVisible(this.createGuid, isMonitor(Monitors.CREATEPLAYLIST, Monitors.ADDPLAYMUSIC1));
        IKSGScreenUtil.setVisible(this.createAnyoneCheckbox, isMonitor(Monitors.CREATEPLAYLIST));
        IKSGScreenUtil.setActive(this.createGuid, image != null && !createGuildNameField.getText().isEmpty());
        IKSGScreenUtil.setVisible(this.createJoinGuid, isMonitor(Monitors.ADDPLAYLIST));
        IKSGScreenUtil.setVisible(this.addJoinGuid, isMonitor(Monitors.ADDPLAYLIST));
        IKSGScreenUtil.setVisible(this.backJoinGuid, isMonitor(Monitors.ADDPLAYLIST));
        IKSGScreenUtil.setVisible(this.joinplaylistbar, isMonitor(Monitors.JOINPLAYLIST));
        IKSGScreenUtil.setVisible(this.joinplaylistButtons, isMonitor(Monitors.JOINPLAYLIST));
        IKSGScreenUtil.setVisible(this.joinplaylistbackButton, isMonitor(Monitors.JOINPLAYLIST));
        IKSGScreenUtil.setVisible(this.addPlayMusicButton, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.addPlayMusicNameField, isMonitor(Monitors.ADDPLAYMUSIC1));
        IKSGScreenUtil.setVisible(this.addPlayMusicSourceField, isMonitor(Monitors.ADDPLAYMUSIC1));
        IKSGScreenUtil.setVisible(this.addPlayMusicSourceReferenceButton, isMonitor(Monitors.ADDPLAYMUSIC1));
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

    public void insMode(Monitors moniter) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("name", moniter.getName());
        this.instruction("mode", tag);
        if (moniter == Monitors.CREATEPLAYLIST || moniter == Monitors.ADDPLAYMUSIC1) {
            Path picPath = getPicturPath();
            if (picPath != null) {
                DropAndDragFileLoadThread plt = new DropAndDragFileLoadThread(true, this, picPath);
                plt.start();
            }
        }
        if (moniter != Monitors.CREATEPLAYLIST && moniter != Monitors.ADDPLAYMUSIC1) {
            if (picturImage != null) {
                picturImage = null;
                CompoundNBT ptag = new CompoundNBT();
                tag.putString("path", "");
                instruction("pathset", tag);
            }
            this.image = new PlayImage(PlayImage.ImageType.STRING, "");
        }
    }

    private void updatePlayList() {
        if (!isMonitor(Monitors.JOINPLAYLIST, Monitors.PLAYLIST))
            return;
        CompoundNBT tag = new CompoundNBT();
        tag.putString("type", Monitorsa.name);
        instruction("playlistupdate", tag);
    }

    private void timerSet() {
        this.timer = new ClockTimer(n -> this.isOpend());
        this.timer.addTask("updateplaylist", new ClockTimer.ITask() {
            @Override
            public boolean isStop(ClockTimer clockTimer) {
                return false;
            }

            @Override
            public void run(ClockTimer clockTimer) {
                updatePlayList();
            }

            @Override
            public long time(ClockTimer clockTimer) {
                return 3000;
            }
        });


    }

    @Override
    public void instructionReturn(String name, CompoundNBT data) {
        if (name.equals("playlistupdate") || name.equals("mode")) {
            CompoundNBT taga = data.getCompound("list");
            String type = data.getString("type");
            Monitors mtype = Monitors.getValueOf(type);

            if (mtype == Monitors.JOINPLAYLIST) {
                jonPlaylists.clear();
                for (String pltagst : taga.keySet()) {
                    jonPlaylists.add(new PlayList(pltagst, taga.getCompound(pltagst)));
                }
            } else if (mtype == Monitors.PLAYLIST) {
                jonedAllPlaylists.clear();
                for (String pltagst : taga.keySet()) {
                    jonedAllPlaylists.add(new PlayList(pltagst, taga.getCompound(pltagst)));
                }
            }
        }
    }


    public void insPower(boolean on) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("on", on);
        this.instruction("power", tag);
    }


    private void setMonitorsa() {
        if (!isStateOn())
            Monitorsa = Monitors.OFF;
        else
            Monitorsa = Monitors.getValueOf(getMode());
    }

    private void setListName() {
        listname = I18n.format("msd.all");
    }


    protected void drawAddPlayMusic1(MatrixStack matrx, float partTick, int mouseX, int mouseY) {
        drawFontString(matrx, new TranslationTextComponent("msd.addplaymusic"), getMonitorStartX() + 2, getMonitorStartY() + 2);
        drawFontString(matrx, new TranslationTextComponent("msd.image"), getMonitorStartX() + 6, getMonitorStartY() + 17);
        drawFontString(matrx, new TranslationTextComponent("msd.name"), getMonitorStartX() + 92, getMonitorStartY() + 17);
        drawFontString(matrx, new TranslationTextComponent("msd.source"), getMonitorStartX() + 92, getMonitorStartY() + 43);

        if (pictuerLoading) {
            IKSGRenderUtil.matrixPush(matrx);
            RenderSystem.enableBlend();
            IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getLoadingIconTextuer(), matrx, getMonitorStartX() + 5, getMonitorStartY() + 108, 0, 0, 8, 8, 8, 8);
            IKSGRenderUtil.matrixPop(matrx);
            drawFontString(matrx, new TranslationTextComponent("msd.imageloading"), getMonitorStartX() + 6 + 9, getMonitorStartY() + 109);
        } else {
            drawFontString(matrx, new TranslationTextComponent("msd.imagedropInfo"), getMonitorStartX() + 6, getMonitorStartY() + 109);
        }

        if (image != null) {
            RenderUtil.drwPlayImage(matrx, image, picturImage, getMonitorStartX() + 7, getMonitorStartY() + 27, 79);
        }


        if (musicLoading) {
            IKSGRenderUtil.matrixPush(matrx);
            RenderSystem.enableBlend();
            IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getLoadingIconTextuer(), matrx, getMonitorStartX() + 92, getMonitorStartY() + 69, 0, 0, 8, 8, 8, 8);
            IKSGRenderUtil.matrixPop(matrx);
            drawFontString(matrx, new TranslationTextComponent("msd.musicloading"), getMonitorStartX() + 93 + 9, getMonitorStartY() + 70);
        } else {
            if (musicLoadError != null) {
                drawFontString(matrx, musicLoadError.getLocalizedName(), getMonitorStartX() + 92, getMonitorStartY() + 70);
            }
        }

        IKSGRenderUtil.matrixPush(matrx);
        addPlayMusicNameField.func_230430_a_(matrx, mouseX, mouseY, partTick);
        addPlayMusicSourceField.func_230430_a_(matrx, mouseX, mouseY, partTick);
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        IKSGRenderUtil.matrixPop(matrx);
    }

    protected void drawJoinPlaylist(MatrixStack matrx, float partTick, int mouseX, int mouseY) {
        drawFontString(matrx, new TranslationTextComponent("msd.joinplaylist"), getMonitorStartX() + 2, getMonitorStartY() + 2);
    }

    protected void drawAddPlaylist(MatrixStack matrx, float partTick, int mouseX, int mouseY) {
        drawFontString(matrx, new TranslationTextComponent("msd.addplaylist"), getMonitorStartX() + 2, getMonitorStartY() + 2);
        drawCenterFontString(matrx, new TranslationTextComponent("msd.addplaylistInfo"), getMonitorStartX() + getMonitorXsize() / 2, getMonitorStartY() + getMonitorYsize() / 2 - 25);
    }

    protected void drawCreatePlaylist(MatrixStack matrx, float partTick, int mouseX, int mouseY) {
        drawFontString(matrx, new TranslationTextComponent("msd.createplaylist"), getMonitorStartX() + 2, getMonitorStartY() + 2);
        drawFontString(matrx, new TranslationTextComponent("msd.image"), getMonitorStartX() + 6, getMonitorStartY() + 17);
        drawFontString(matrx, new TranslationTextComponent("msd.name"), getMonitorStartX() + 92, getMonitorStartY() + 17);
        drawFontString(matrx, new TranslationTextComponent("msd.anyonecheck"), getMonitorStartX() + 92 + 17, getMonitorStartY() + 59);
        if (pictuerLoading) {
            IKSGRenderUtil.matrixPush(matrx);
            RenderSystem.enableBlend();
            IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getLoadingIconTextuer(), matrx, getMonitorStartX() + 5, getMonitorStartY() + 108, 0, 0, 8, 8, 8, 8);
            IKSGRenderUtil.matrixPop(matrx);
            drawFontString(matrx, new TranslationTextComponent("msd.imageloading"), getMonitorStartX() + 6 + 9, getMonitorStartY() + 109);
        } else {
            drawFontString(matrx, new TranslationTextComponent("msd.imagedropInfo"), getMonitorStartX() + 6, getMonitorStartY() + 109);
        }

        if (image != null) {
            RenderUtil.drwPlayImage(matrx, image, picturImage, getMonitorStartX() + 7, getMonitorStartY() + 27, 79);
        }

        IKSGRenderUtil.matrixPush(matrx);
        createGuildNameField.func_230430_a_(matrx, mouseX, mouseY, partTick);
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
        OFF(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_off.png"), "off"),
        ON(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_on.png"), "on"),
        PLAYLIST(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_list.png"), "playlist"),
        NOANTENNA(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_noantenna.png"), "noantenna"),
        CREATEPLAYLIST(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_createplaylist.png"), "createplaylist"),
        ADDPLAYLIST(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_addplaylist.png"), "addplaylist"),
        JOINPLAYLIST(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_joinplaylist.png"), "joinplaylist"),
        ADDPLAYMUSIC1(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/msd_monitor_addplaymusic_1.png"), "addplaymusic1");

        private final ResourceLocation location;
        private final String name;

        Monitors(ResourceLocation location, String name) {
            this.location = location;
            this.name = name;
        }

        public ResourceLocation getTextuer() {
            return this.location;
        }

        public static Monitors getValueOf(String name) {
            if (name == null)
                return OFF;
            for (Monitors m : Monitors.values()) {
                if (m.name.equals(name))
                    return m;
            }
            return OFF;
        }

        public String getName() {
            return name;
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
        if (isMonitor(Monitors.CREATEPLAYLIST, Monitors.ADDPLAYMUSIC1)) {
            if (dragFiles.size() == 1 && !pictuerLoading) {
                DropAndDragFileLoadThread lt = new DropAndDragFileLoadThread(false, this, dragFiles.get(0));
                lt.start();
            }
        }
    }

    @Override
    public boolean func_231045_a_(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_, double p_231045_8_) {
        boolean flag1 = super.func_231045_a_(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
        boolean flag2 = this.func_241217_q_() != null && this.func_231041_ay__() && p_231045_5_ == 0 ? this.func_241217_q_().func_231045_a_(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_) : false;
        return flag1 & flag2;
    }

    public MusicSourceClientReferencesType getMusicSourceClientReferencesType() {
        return musicSourceClientReferencesType;
    }

    public void setMusicSourceClientReferencesType(MusicSourceClientReferencesType musicSourceClientReferencesType) {
        this.musicSourceClientReferencesType = musicSourceClientReferencesType;
    }

    public void setPicturImage(byte[] picturImage, Path path) {
        this.picturImage = picturImage;
        if (picturImage != null) {
            if (path != null) {
                this.image = new PlayImage(PlayImage.ImageType.IMGAE, UUID.randomUUID().toString());
                CompoundNBT tag = new CompoundNBT();
                tag.putString("path", path.toString());
                instruction("pathset", tag);
            }
        } else {
            if (isMonitor(Monitors.CREATEPLAYLIST))
                this.image = new PlayImage(PlayImage.ImageType.STRING, this.createGuildNameField.getText());
            else if (isMonitor(Monitors.ADDPLAYMUSIC1))
                this.image = new PlayImage(PlayImage.ImageType.STRING, this.addPlayMusicNameField.getText());
        }
    }

    public static enum MusicLoadResult {
        NO_SUPPORT_FORMAT("nosupportformat"),
        FILE_NOT_EXIST("filenotexist");

        private String name;

        private MusicLoadResult(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public TranslationTextComponent getLocalizedName() {
            return new TranslationTextComponent("musicloaderror." + name);
        }

    }

    public void setMusicLoadError(MusicLoadResult musicLoadError) {
        this.musicLoadError = musicLoadError;
    }

    private static class DropAndDragFileLoadThread extends Thread {
        private final MusicSharingDeviceScreen screen;
        private final Path path;
        private final boolean pictuerOnry;

        public DropAndDragFileLoadThread(boolean pictueronry, MusicSharingDeviceScreen screen, Path path) {
            this.screen = screen;
            this.path = path;
            this.pictuerOnry = pictueronry;
        }

        public void run() {
            screen.pictuerLoading = true;
            boolean nopicteur = false;
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
                    if (IKSGScreenUtil.isOpendScreen(screen))
                        screen.setPicturImage(IKSGPictuerUtil.geByteImage(outbfi), path);
                } else {
                    nopicteur = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            screen.pictuerLoading = false;

            if (pictuerOnry)
                return;

            if (nopicteur) {
                screen.musicLoadError = null;
                screen.setMusicSourceClientReferencesType(MusicSourceClientReferencesType.LOCAL_FILE);
                screen.addPlayMusicSourceField.setText(path.toString());
            }
        }
    }

    private static class SourceCheckThread extends Thread {
        private final MusicSharingDeviceScreen screen;
        private final String source;
        private boolean stop;

        public SourceCheckThread(MusicSharingDeviceScreen screen, String source) {
            this.screen = screen;
            this.source = source;
        }

        public void setStop(boolean stop) {
            this.stop = stop;
        }

        public boolean isStop() {
            return stop;
        }

        public void run() {


            if (this.stop || source.isEmpty())
                return;

            screen.musicLoading = true;
            screen.musicLoadError = null;

            if (screen.getMusicSourceClientReferencesType() == MusicSourceClientReferencesType.LOCAL_FILE) {
                try {
                    Path path = Paths.get(source);
                    if (!path.toFile().exists()) {
                        if (!this.stop) {
                            screen.musicLoadError = MusicLoadResult.FILE_NOT_EXIST;
                            screen.musicLoading = false;
                        }
                        return;
                    }

                    try {
                        MultimediaObject mo = new MultimediaObject(path.toFile());
                        Encoder encoder = new Encoder();
                        if (Arrays.asList(encoder.getSupportedEncodingFormats()).contains(mo.getInfo().getFormat())) {
                            if (mo.getInfo().getFormat().equals("mp3")) {
                                Mp3File mp3 = new Mp3File(path);
                                ID3v2 id3v2 = mp3.getId3v2Tag();
                                if (id3v2 != null) {
                                    if (id3v2.getTitle() != null) {
                                        if (!this.stop)
                                            screen.addPlayMusicNameField.setText(id3v2.getTitle());
                                    } else {
                                        if (!this.stop)
                                            screen.addPlayMusicNameField.setText(IKSGStringUtil.deleteExtension(path.toFile().getName()));
                                    }
                                    if (id3v2.getAlbumImage() != null) {
                                        String uuid = UUID.randomUUID().toString();
                                        IKSGFileLoadUtil.fileBytesWriter(id3v2.getAlbumImage(), PathUtil.getClientTmpFolder().resolve(uuid));
                                        if (!this.stop) {
                                            DropAndDragFileLoadThread plt = new DropAndDragFileLoadThread(true, screen, PathUtil.getClientTmpFolder().resolve(uuid));
                                            plt.start();
                                        }
                                    }
                                } else {
                                    if (!this.stop)
                                        screen.addPlayMusicNameField.setText(IKSGStringUtil.deleteExtension(path.toFile().getName()));
                                }
                            }
                        } else {
                            if (!this.stop) {
                                screen.musicLoadError = MusicLoadResult.NO_SUPPORT_FORMAT;
                                screen.musicLoading = false;
                            }
                            return;
                        }
                    } catch (Exception ex) {
                        if (!this.stop) {
                            screen.musicLoadError = MusicLoadResult.NO_SUPPORT_FORMAT;
                            screen.musicLoading = false;
                        }
                        return;
                    }
                } catch (Exception ex) {
                    if (!this.stop) {
                        screen.musicLoadError = MusicLoadResult.FILE_NOT_EXIST;
                        screen.musicLoading = false;
                    }
                    return;
                }
            } else if (screen.getMusicSourceClientReferencesType() == MusicSourceClientReferencesType.URL) {
                try {
                    URL url = new URL("https://www.dropbox.com/s/kq54xk0ylxlch9z/%E3%82%AB%E3%82%AA%E3%82%B9%E9%B3%A5128.mp3?dl=1");
                    MultimediaObject mo = new MultimediaObject(url);
                    Encoder encoder = new Encoder();
                    if (Arrays.asList(encoder.getSupportedEncodingFormats()).contains(mo.getInfo().getFormat())) {

                    } else {
                        if (!this.stop) {
                            screen.musicLoadError = MusicLoadResult.NO_SUPPORT_FORMAT;
                            screen.musicLoading = false;
                        }
                        return;
                    }
                } catch (Exception ex) {
                    if (!this.stop) {
                        screen.musicLoadError = MusicLoadResult.FILE_NOT_EXIST;
                        screen.musicLoading = false;
                    }
                    return;
                }
            }

            if (!this.stop)
                screen.musicLoading = false;
        }
    }
}
