package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sun.javafx.application.PlatformImpl;
import javafx.stage.FileChooser;
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
import red.felnull.imp.client.gui.widget.*;
import red.felnull.imp.client.util.RenderUtil;
import red.felnull.imp.client.util.YoutubeUtils;
import red.felnull.imp.container.MusicSharingDeviceContainer;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.data.PlayMusicManeger;
import red.felnull.imp.item.IMPItems;
import red.felnull.imp.musicplayer.PlayImage;
import red.felnull.imp.musicplayer.PlayList;
import red.felnull.imp.musicplayer.PlayLocation;
import red.felnull.imp.musicplayer.PlayMusic;
import red.felnull.imp.tileentity.MusicSharingDeviceTileEntity;
import red.felnull.imp.util.PathUtil;
import red.felnull.otyacraftengine.client.gui.IkisugiDialogTexts;
import red.felnull.otyacraftengine.client.gui.screen.AbstractIkisugiContainerScreen;
import red.felnull.otyacraftengine.client.gui.widget.ChangeableImageButton;
import red.felnull.otyacraftengine.client.gui.widget.Checkbox;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.StringImageButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGScreenUtil;
import red.felnull.otyacraftengine.client.util.IKSGTextureUtil;
import red.felnull.otyacraftengine.util.*;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
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
    private static final ResourceLocation YOUTUBE_ICON = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/youtube_icon.png");
    private static final ResourceLocation fontLocation = new ResourceLocation(IamMusicPlayer.MODID, "msd");
    //   private static final ResourceLocation fontLocation = new ResourceLocation("minecraft", "default");
    public static final Style fontStyle = IKSGStyles.withFont(fontLocation);

    private static final FileFilter FolderSerchFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            return !file.isDirectory() && file.exists();
        }
    };

    private final List<PlayList> jonPlaylists = new ArrayList<>();
    private final List<PlayList> jonedAllPlaylists = new ArrayList<>();
    private final List<PlayMusic> currentPlaylistsMusics = new ArrayList<>();

    private final FileChooser SoundFileChooser;
    private final FileChooser ImageFileChooser;

    private PlayImage image;

    private List<AudioTrack> youtubeResilts = new ArrayList<>();
    private boolean youtubeSearchLoading;
    private YoutubeSearchThread youtubeSearchThread;

    private byte[] picturImage;
    private boolean pictuerLoading;
    private boolean musicLoading;
    private MusicLoadResult musicLoadResult;
    public UploadLocation uploadLocation;
    private Monitors Monitorsa;
    private MusicSourceClientReferencesType musicSourceClientReferencesType;
    private String formattype;
    private PlayList currentPlayList;
    private boolean initFrist;

    private SourceCheckThread sourceCheckThread;
    protected ClockTimer timer;

    private ChangeableImageButton powerButton;
    private StringImageButton allbutton;
    private ImageButton addGuildButton;
    private ScrollBarSlider guildlistbar;
    private ScrollBarSlider playlistbar;
    private GuildPlayListScrollButton guildButtons;
    private PlayMusicScrollButton playlistButtons;
    private TextFieldWidget createGuildNameField;
    private Checkbox createAnyoneCheckbox;
    private StringImageButton backGuid;
    private StringImageButton createGuid;
    private ChangeableImageButton resetImage;
    private ChangeableImageButton openImage;
    private StringImageButton createJoinGuid;
    private StringImageButton addJoinGuid;
    private StringImageButton backJoinGuid;
    private ScrollBarSlider joinplaylistbar;
    private JoinPlayListScrollButton JoinPlayListScrollButtons;
    private StringImageButton joinplaylistbackButton;
    private ChangeableImageButton addPlayMusicButton;
    private TextFieldWidget addPlayMusicNameField;
    public TextFieldWidget addPlayMusicSourceField;
    private PlayMusicSourceReferenceButton addPlayMusicSourceReferenceButton;
    private ChangeableImageButton addPlayMusicYoutubeSarchButton;
    private ChangeableImageButton addPlayMusicOpenFolder;
    private StringImageButton nextAddPlayMusic;
    private TextFieldWidget addPlayMusicArtistField;
    private TextFieldWidget addPlayMusicAlbumField;
    private TextFieldWidget addPlayMusicYearField;
    private TextFieldWidget addPlayMusicGenreField;
    private StringImageButton addPlayMusic2BackButton;
    private StringImageButton addPlayMusic2CrateButton;
    private UploadLocationSelectButton addPlayMusic2UploadSelectWorld;
    private UploadLocationSelectButton addPlayMusic2UploadSelectURL;
    private UploadLocationSelectButton addPlayMusic2UploadSelectGitHub;
    private StringImageButton addPlayMusicYoutubeSerchBackButton;
    public TextFieldWidget addPlayMusicYoutubeSearchField;
    private ScrollBarSlider addPlayMusicYoutubeSearchlistbar;
    private ChangeableImageButton addPlayMusicYoutubeSearchButton;
    private YoutubeSearchResultScrollListButton addPlayMusicYoutubeSearchFileListButton;

    public MusicSharingDeviceScreen(MusicSharingDeviceContainer screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
        this.xSize = 215;
        this.ySize = 242;
        this.currentPlayList = PlayList.ALL;
        setMonitorsa();
        this.SoundFileChooser = new FileChooser();
        this.SoundFileChooser.setTitle(I18n.format("msd.openSoundFile"));
        this.ImageFileChooser = new FileChooser();
        this.ImageFileChooser.setTitle(I18n.format("msd.openImageFile"));
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
        updatePlayMusic();
        timerSet();
        instruction("opengui", new CompoundNBT());

        if (musicSourceClientReferencesType == null)
            this.musicSourceClientReferencesType = MusicSourceClientReferencesType.LOCAL_FILE;

        this.pictuerLoading = false;
        this.musicLoading = false;
        this.playerInventoryTitleY = this.ySize - 94;
        this.powerButton = this.addWidgetByIKSG(new ChangeableImageButton(getTexturStartX() + 181, getTexturStartY() + 202, 20, 20, 215, 0, 20, MSD_GUI_TEXTURES, 256, 256, n -> {
            insPower(!this.isStateOn());
        }));
        if (isStateOn()) {
            powerButton.setTextuer(235, 0, 20, 256, 256);
        } else {
            powerButton.setTextuer(215, 0, 20, 256, 256);
        }
        this.allbutton = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + 1, getMonitorStartY() + 1, 18, 18, 215, 60, 18, MSD_GUI_TEXTURES, n -> {
            this.currentPlayList = PlayList.ALL;
            updatePlayMusic();
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


        this.guildButtons = this.addWidgetByIKSG(new GuildPlayListScrollButton(getMonitorStartX() + 1, getMonitorStartY() + 20, 18, 101, 18, guildlistbar, jonedAllPlaylists, (n, m) -> {
            this.currentPlayList = jonedAllPlaylists.get(m);
            updatePlayMusic();
        }, this));
        IKSGScreenUtil.setVisible(this.guildButtons, false);

        this.playlistButtons = this.addWidgetByIKSG(new PlayMusicScrollButton(getMonitorStartX() + 30, getMonitorStartY() + 20, 158, 101, 40, playlistbar, currentPlaylistsMusics, (n, m) -> {

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
            PlayListGuildManeger.instance().createPlayListRequest(createGuildNameField.getText(), image, picturImage, createAnyoneCheckbox.isCheck());
            insMode(Monitors.PLAYLIST);
        }, IKSGStyles.withStyle((TranslationTextComponent) IkisugiDialogTexts.CRATE, fontStyle)));
        this.createGuid.setSizeAdjustment(true);
        this.createGuid.setShadwString(false);
        this.createGuid.setStringColor(0);
        IKSGScreenUtil.setVisible(this.createGuid, false);

        this.resetImage = this.addWidgetByIKSG(new ChangeableImageButton(getMonitorStartX() + 86 - 16, getMonitorStartY() + 106 - 8, 8, 8, 215, 198, 8, MSD_GUI_TEXTURES, n -> {
            this.picturImage = null;
            instruction("pathset", new CompoundNBT());
            if (isMonitor(Monitors.CREATEPLAYLIST)) {
                this.image = new PlayImage(PlayImage.ImageType.STRING, createGuildNameField.getText());
            } else if (isMonitor(Monitors.ADDPLAYMUSIC1)) {
                this.image = new PlayImage(PlayImage.ImageType.STRING, addPlayMusicNameField.getText());
            }
        }));
        IKSGScreenUtil.setVisible(this.resetImage, false);

        this.openImage = this.addWidgetByIKSG(new ChangeableImageButton(getMonitorStartX() + 86 - 8, getMonitorStartY() + 106 - 8, 8, 8, 223, 198, 8, MSD_GUI_TEXTURES, n -> {
            PlatformImpl.startup(() -> {
                File file = ImageFileChooser.showOpenDialog(null);
                if (file != null && this.isOpend() && isMonitor(Monitors.CREATEPLAYLIST, Monitors.ADDPLAYMUSIC1) && !file.isDirectory()) {
                    DropAndDragFileLoadThread lt = new DropAndDragFileLoadThread(true, this, file.toPath());
                    lt.start();
                    ImageFileChooser.setInitialDirectory(file.getParentFile());
                }
            });
        }));
        IKSGScreenUtil.setVisible(this.openImage, false);

        this.createGuildNameField = this.addWidgetByIKSG(new TextFieldWidget(this.font, getMonitorStartX() + 95, getMonitorStartY() + 29, 91, 12, new StringTextComponent("test")));
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

        this.joinplaylistbar = this.addWidgetByIKSG(new ScrollBarSlider(getMonitorStartX() + 189, getMonitorStartY() + 20, 101, 100, 0, -189, 215, 126, MSD_GUI_TEXTURES));
        IKSGScreenUtil.setVisible(this.joinplaylistbar, false);

        this.JoinPlayListScrollButtons = this.addWidgetByIKSG(new JoinPlayListScrollButton(getMonitorStartX() + 1, getMonitorStartY() + 20, 187, 101, 40, joinplaylistbar, jonPlaylists, (n, m) -> {
            PlayListGuildManeger.instance().joinPlayListRequest(jonPlaylists.get(m).getUUID());
            insMode(Monitors.PLAYLIST);
        }));
        IKSGScreenUtil.setVisible(this.JoinPlayListScrollButtons, false);

        this.joinplaylistbackButton = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + 1, getMonitorStartY() + 12, 14, 7, 0, 30, 14, MSD_GUI_TEXTURES2, n -> {
            insMode(Monitors.PLAYLIST);
        }, IKSGStyles.withStyle((TranslationTextComponent) IkisugiDialogTexts.BACK, fontStyle)));
        this.joinplaylistbackButton.setSizeAdjustment(true);
        this.joinplaylistbackButton.setShadwString(false);
        this.joinplaylistbackButton.setStringColor(0);
        this.joinplaylistbackButton.setScale(0.5f);
        IKSGScreenUtil.setVisible(this.joinplaylistbackButton, false);

        this.addPlayMusicButton = this.addWidgetByIKSG(new ChangeableImageButton(getMonitorStartX() + 189, getMonitorStartY() + 1, 9, 18, 235, 40, 18, MSD_GUI_TEXTURES, n -> {
            insMode(Monitors.ADDPLAYMUSIC1);
        }));
        IKSGScreenUtil.setVisible(this.addPlayMusicButton, false);

        String MusicNameField = "";
        if (this.addPlayMusicNameField != null)
            MusicNameField = this.addPlayMusicNameField.getText();

        this.addPlayMusicNameField = this.addWidgetByIKSG(new TextFieldWidget(this.font, getMonitorStartX() + 95, getMonitorStartY() + 29, 91, 12, new StringTextComponent("test")));
        this.addPlayMusicNameField.setEnableBackgroundDrawing(false);
        this.addPlayMusicNameField.setMaxStringLength(100);
        this.addPlayMusicNameField.setTextColor(-1);
        this.addPlayMusicNameField.setDisabledTextColour(-1);
        this.addPlayMusicNameField.setText(MusicNameField);
        this.addPlayMusicNameField.setResponder(n -> {
            if (isMonitor(Monitors.ADDPLAYMUSIC1) && picturImage == null) {
                this.image = new PlayImage(PlayImage.ImageType.STRING, n);
            }
        });
        IKSGScreenUtil.setVisible(this.addPlayMusicNameField, false);

        String MusicSourceField = "";
        if (this.addPlayMusicSourceField != null)
            MusicSourceField = this.addPlayMusicSourceField.getText();

        this.addPlayMusicSourceField = this.addWidgetByIKSG(new TextFieldWidget(this.font, getMonitorStartX() + 95, getMonitorStartY() + 55, 55, 12, new StringTextComponent("test")));
        this.addPlayMusicSourceField.setEnableBackgroundDrawing(false);
        this.addPlayMusicSourceField.setMaxStringLength(Integer.MAX_VALUE);
        this.addPlayMusicSourceField.setTextColor(-1);
        this.addPlayMusicSourceField.setDisabledTextColour(-1);
        this.addPlayMusicSourceField.setText(MusicSourceField);
        this.addPlayMusicSourceField.setResponder(n -> {
            if (isMonitor(Monitors.ADDPLAYMUSIC1, Monitors.ADDPLAYMUSICYOUTUBESELECT)) {
                if (this.sourceCheckThread != null)
                    this.sourceCheckThread.setStop(true);
                this.sourceCheckThread = new SourceCheckThread(this, n);
                this.sourceCheckThread.start();
            }
        });
        IKSGScreenUtil.setVisible(this.addPlayMusicSourceField, false);

        this.addPlayMusicSourceReferenceButton = this.addWidgetByIKSG(new PlayMusicSourceReferenceButton(getMonitorStartX() + 175, getMonitorStartY() + 52, 18, 15, 215, 168, 15, MSD_GUI_TEXTURES, n -> {
            setMusicLoadError(null);
            addPlayMusicSourceField.setText("");
            addPlayMusicNameField.setText("");
            addPlayMusicGenreField.setText("");
            addPlayMusicYearField.setText("");
            addPlayMusicNameField.setText("");
            addPlayMusicAlbumField.setText("");
            addPlayMusicArtistField.setText("");
            uploadLocation = null;
            formattype = null;
            image = new PlayImage(PlayImage.ImageType.STRING, "");
            picturImage = null;
            instruction("pathset", new CompoundNBT());
        }, this));
        IKSGScreenUtil.setVisible(this.addPlayMusicSourceReferenceButton, false);

        this.addPlayMusicYoutubeSarchButton = this.addWidgetByIKSG(new ChangeableImageButton(getMonitorStartX() + 157, getMonitorStartY() + 52, 18, 15, 233, 168, 15, MSD_GUI_TEXTURES, n -> {
            insMode(Monitors.ADDPLAYMUSICYOUTUBESELECT);
        }));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSarchButton, false);

        this.addPlayMusicOpenFolder = this.addWidgetByIKSG(new ChangeableImageButton(getMonitorStartX() + 157, getMonitorStartY() + 52, 18, 15, 215, 214, 15, MSD_GUI_TEXTURES, n -> {
            PlatformImpl.startup(() -> {
                File file = SoundFileChooser.showOpenDialog(null);
                if (file != null && this.isOpend() && isMonitor(Monitors.ADDPLAYMUSIC1)) {
                    if (!file.isDirectory()) {
                        addPlayMusicSourceField.setText(file.getPath());
                    }
                    SoundFileChooser.setInitialDirectory(file.getParentFile());
                }
            });
        }));
        IKSGScreenUtil.setVisible(this.addPlayMusicOpenFolder, false);

        this.nextAddPlayMusic = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + 145, getMonitorStartY() + 92, 48, 15, 0, 0, 15, MSD_GUI_TEXTURES2, n -> {
            insMode(Monitors.ADDPLAYMUSIC2);
        }, IKSGStyles.withStyle((TranslationTextComponent) IkisugiDialogTexts.NEXT, fontStyle)));
        this.nextAddPlayMusic.setSizeAdjustment(true);
        this.nextAddPlayMusic.setShadwString(false);
        this.nextAddPlayMusic.setStringColor(0);
        IKSGScreenUtil.setVisible(this.nextAddPlayMusic, false);


        String MusicArtistField = "";
        if (this.addPlayMusicArtistField != null)
            MusicArtistField = this.addPlayMusicArtistField.getText();

        this.addPlayMusicArtistField = this.addWidgetByIKSG(new TextFieldWidget(this.font, getMonitorStartX() + 9, getMonitorStartY() + 29, 76, 12, new StringTextComponent("test")));
        this.addPlayMusicArtistField.setEnableBackgroundDrawing(false);
        this.addPlayMusicArtistField.setMaxStringLength(300);
        this.addPlayMusicArtistField.setTextColor(-1);
        this.addPlayMusicArtistField.setDisabledTextColour(-1);
        this.addPlayMusicArtistField.setText(MusicArtistField);
        this.addPlayMusicArtistField.setResponder(n -> {

        });
        IKSGScreenUtil.setVisible(this.addPlayMusicArtistField, false);

        String MusicAlbumField = "";
        if (this.addPlayMusicAlbumField != null)
            MusicAlbumField = this.addPlayMusicAlbumField.getText();

        this.addPlayMusicAlbumField = this.addWidgetByIKSG(new TextFieldWidget(this.font, getMonitorStartX() + 110, getMonitorStartY() + 29, 76, 12, new StringTextComponent("test")));
        this.addPlayMusicAlbumField.setEnableBackgroundDrawing(false);
        this.addPlayMusicAlbumField.setMaxStringLength(300);
        this.addPlayMusicAlbumField.setTextColor(-1);
        this.addPlayMusicAlbumField.setDisabledTextColour(-1);
        this.addPlayMusicAlbumField.setText(MusicAlbumField);
        this.addPlayMusicAlbumField.setResponder(n -> {

        });
        IKSGScreenUtil.setVisible(this.addPlayMusicAlbumField, false);

        String MusicYearField = "";
        if (this.addPlayMusicYearField != null)
            MusicYearField = this.addPlayMusicYearField.getText();

        this.addPlayMusicYearField = this.addWidgetByIKSG(new TextFieldWidget(this.font, getMonitorStartX() + 9, getMonitorStartY() + 55, 76, 12, new StringTextComponent("test")));
        this.addPlayMusicYearField.setEnableBackgroundDrawing(false);
        this.addPlayMusicYearField.setMaxStringLength(300);
        this.addPlayMusicYearField.setTextColor(-1);
        this.addPlayMusicYearField.setDisabledTextColour(-1);
        this.addPlayMusicYearField.setText(MusicYearField);
        this.addPlayMusicYearField.setResponder(n -> {

        });
        IKSGScreenUtil.setVisible(this.addPlayMusicYearField, false);

        String MusicGenreField = "";
        if (this.addPlayMusicGenreField != null)
            MusicGenreField = this.addPlayMusicGenreField.getText();

        this.addPlayMusicGenreField = this.addWidgetByIKSG(new TextFieldWidget(this.font, getMonitorStartX() + 110, getMonitorStartY() + 55, 76, 12, new StringTextComponent("test")));
        this.addPlayMusicGenreField.setEnableBackgroundDrawing(false);
        this.addPlayMusicGenreField.setMaxStringLength(300);
        this.addPlayMusicGenreField.setTextColor(-1);
        this.addPlayMusicGenreField.setDisabledTextColour(-1);
        this.addPlayMusicGenreField.setText(MusicGenreField);
        this.addPlayMusicGenreField.setResponder(n -> {

        });
        IKSGScreenUtil.setVisible(this.addPlayMusicGenreField, false);

        this.addPlayMusic2BackButton = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + getMonitorXsize() / 2 - 48 - 5, getMonitorStartY() + 105, 48, 15, 0, 0, 15, MSD_GUI_TEXTURES2, n -> {
            insMode(Monitors.ADDPLAYMUSIC1);
        }, IKSGStyles.withStyle((TranslationTextComponent) IkisugiDialogTexts.BACK, fontStyle)));
        this.addPlayMusic2BackButton.setSizeAdjustment(true);
        this.addPlayMusic2BackButton.setShadwString(false);
        this.addPlayMusic2BackButton.setStringColor(0);
        IKSGScreenUtil.setVisible(this.addPlayMusic2BackButton, false);

        this.addPlayMusic2CrateButton = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + getMonitorXsize() / 2 + 5, getMonitorStartY() + 105, 48, 15, 0, 0, 15, MSD_GUI_TEXTURES2, n -> {
            PlayLocation location = new PlayLocation(uploadLocation == UploadLocation.WORLD ? PlayLocation.LocationType.WORLD_FILE : PlayLocation.LocationType.URL, uploadLocation == UploadLocation.WORLD ? UUID.randomUUID().toString() : this.addPlayMusicSourceField.getText());
            PlayMusicManeger.instance().createPlayMusicRequest(this.addPlayMusicNameField.getText(), currentPlayList, this.image, picturImage, location, musicSourceClientReferencesType, this.addPlayMusicSourceField.getText(), this.addPlayMusicArtistField.getText(), this.addPlayMusicAlbumField.getText(), this.addPlayMusicYearField.getText(), this.addPlayMusicGenreField.getText());
            insMode(Monitors.PLAYLIST);
        }, IKSGStyles.withStyle((TranslationTextComponent) IkisugiDialogTexts.CRATE, fontStyle)));
        this.addPlayMusic2CrateButton.setSizeAdjustment(true);
        this.addPlayMusic2CrateButton.setShadwString(false);
        this.addPlayMusic2CrateButton.setStringColor(0);
        IKSGScreenUtil.setVisible(this.addPlayMusic2CrateButton, false);

        this.addPlayMusic2UploadSelectWorld = this.addWidgetByIKSG(new UploadLocationSelectButton(this, UploadLocation.WORLD, getMonitorStartX() + 6, getMonitorStartY() + 78, 53, 15, 0, 44, 15, MSD_GUI_TEXTURES2, n -> {
            this.uploadLocation = UploadLocation.WORLD;
        }, false));
        IKSGScreenUtil.setVisible(this.addPlayMusic2UploadSelectWorld, false);

        this.addPlayMusic2UploadSelectURL = this.addWidgetByIKSG(new UploadLocationSelectButton(this, UploadLocation.URL, getMonitorStartX() + 73, getMonitorStartY() + 78, 53, 15, 0, 44, 15, MSD_GUI_TEXTURES2, n -> {
            this.uploadLocation = UploadLocation.URL;
        }, false));
        IKSGScreenUtil.setVisible(this.addPlayMusic2UploadSelectURL, false);

        this.addPlayMusic2UploadSelectGitHub = this.addWidgetByIKSG(new UploadLocationSelectButton(this, UploadLocation.GITHUB, getMonitorStartX() + 140, getMonitorStartY() + 78, 53, 15, 0, 104, 15, MSD_GUI_TEXTURES2, n -> {
            this.uploadLocation = UploadLocation.GITHUB;
        }, true));
        IKSGScreenUtil.setVisible(this.addPlayMusic2UploadSelectGitHub, false);
        IKSGScreenUtil.setActive(this.addPlayMusic2UploadSelectGitHub, false);


        this.addPlayMusicYoutubeSerchBackButton = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + 150, getMonitorStartY() + 12, 48, 15, 0, 0, 15, MSD_GUI_TEXTURES2, n -> {
            insMode(Monitors.ADDPLAYMUSIC1);
        }, IKSGStyles.withStyle((TranslationTextComponent) IkisugiDialogTexts.BACK, fontStyle)));
        this.addPlayMusicYoutubeSerchBackButton.setSizeAdjustment(true);
        this.addPlayMusicYoutubeSerchBackButton.setShadwString(false);
        this.addPlayMusicYoutubeSerchBackButton.setStringColor(0);
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSerchBackButton, false);

        String SelectionMusicSearchField = "";
        if (this.addPlayMusicYoutubeSearchField != null)
            SelectionMusicSearchField = this.addPlayMusicYoutubeSearchField.getText();

        this.addPlayMusicYoutubeSearchField = this.addWidgetByIKSG(new TextFieldWidget(this.font, getMonitorStartX() + 4 + 29, getMonitorStartY() + 15, 91, 12, new StringTextComponent("test")));
        this.addPlayMusicYoutubeSearchField.setEnableBackgroundDrawing(false);
        this.addPlayMusicYoutubeSearchField.setMaxStringLength(Integer.MAX_VALUE);
        this.addPlayMusicYoutubeSearchField.setTextColor(-1);
        this.addPlayMusicYoutubeSearchField.setDisabledTextColour(-1);
        this.addPlayMusicYoutubeSearchField.setText(SelectionMusicSearchField);
        this.addPlayMusicYoutubeSearchField.setResponder(n -> {
        });
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSearchField, false);

        this.addPlayMusicYoutubeSearchlistbar = this.addWidgetByIKSG(new ScrollBarSlider(getMonitorStartX() + 189, getMonitorStartY() + 28, 93, 100, 0, -189, 215, 126, MSD_GUI_TEXTURES));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSearchlistbar, false);

        this.addPlayMusicYoutubeSearchButton = this.addWidgetByIKSG(new ChangeableImageButton(getMonitorStartX() + 102 + 29, getMonitorStartY() + 12, 18, 15, 233, 168, 15, MSD_GUI_TEXTURES, n -> {
            if (this.youtubeSearchThread != null)
                youtubeSearchThread.stop = true;
            youtubeSearchThread = new YoutubeSearchThread(this, addPlayMusicYoutubeSearchField.getText());
            youtubeSearchThread.start();
        }));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSearchButton, false);


        this.addPlayMusicYoutubeSearchFileListButton = this.addWidgetByIKSG(new YoutubeSearchResultScrollListButton(getMonitorStartX() + 1, getMonitorStartY() + 28, 187, 93, 40, addPlayMusicYoutubeSearchlistbar, this.youtubeResilts, (n, m) -> {

        }));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSearchFileListButton, false);

        if (!initFrist) {
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

        this.initFrist = true;

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
            case ADDPLAYMUSIC2:
                drawAddPlayMusic2(matx, partTick, mouseX, mouseY);
                break;
            case ADDPLAYMUSICYOUTUBESELECT:
                drawAddPlayMusicYoutubeSelect(matx, partTick, mouseX, mouseY);
                break;
        }
    }

    @Override
    public void tickByIKSG() {
        setMonitorsa();


        if (!isMonitor(Monitors.CREATEPLAYLIST, Monitors.ADDPLAYMUSIC1, Monitors.ADDPLAYMUSIC2, Monitors.ADDPLAYMUSICYOUTUBESELECT)) {
            picturImage = null;
            this.image = new PlayImage(PlayImage.ImageType.STRING, "");
        }

        if (isStateOn())
            powerButton.setTextuer(235, 0, 20, 256, 256);
        else
            powerButton.setTextuer(215, 0, 20, 256, 256);

        if (currentPlayList == PlayList.ALL)
            addPlayMusicButton.setTextuer(244, 40, 18, 256, 256);
        else
            addPlayMusicButton.setTextuer(235, 40, 18, 256, 256);

        if (!isMonitor(Monitors.ADDPLAYMUSIC1, Monitors.ADDPLAYMUSIC2, Monitors.ADDPLAYMUSICYOUTUBESELECT)) {
            this.uploadLocation = null;
            this.formattype = "";
        }

        fieldTick();
        IKSGScreenUtil.setVisible(this.allbutton, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.addGuildButton, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.guildlistbar, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.playlistbar, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.guildButtons, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.playlistButtons, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setVisible(this.createGuildNameField, isMonitor(Monitors.CREATEPLAYLIST));
        IKSGScreenUtil.setVisible(this.backGuid, isMonitor(Monitors.CREATEPLAYLIST, Monitors.ADDPLAYMUSIC1));
        IKSGScreenUtil.setVisible(this.createGuid, isMonitor(Monitors.CREATEPLAYLIST));
        IKSGScreenUtil.setVisible(this.resetImage, isMonitor(Monitors.CREATEPLAYLIST, Monitors.ADDPLAYMUSIC1) && image != null && image.getImageType() == PlayImage.ImageType.IMGAE);
        IKSGScreenUtil.setVisible(this.openImage, isMonitor(Monitors.CREATEPLAYLIST, Monitors.ADDPLAYMUSIC1));
        IKSGScreenUtil.setVisible(this.createAnyoneCheckbox, isMonitor(Monitors.CREATEPLAYLIST));
        IKSGScreenUtil.setActive(this.createGuid, image != null && !createGuildNameField.getText().isEmpty());
        IKSGScreenUtil.setVisible(this.createJoinGuid, isMonitor(Monitors.ADDPLAYLIST));
        IKSGScreenUtil.setVisible(this.addJoinGuid, isMonitor(Monitors.ADDPLAYLIST));
        IKSGScreenUtil.setVisible(this.backJoinGuid, isMonitor(Monitors.ADDPLAYLIST));
        IKSGScreenUtil.setVisible(this.joinplaylistbar, isMonitor(Monitors.JOINPLAYLIST));
        IKSGScreenUtil.setVisible(this.JoinPlayListScrollButtons, isMonitor(Monitors.JOINPLAYLIST));
        IKSGScreenUtil.setVisible(this.joinplaylistbackButton, isMonitor(Monitors.JOINPLAYLIST));
        IKSGScreenUtil.setVisible(this.addPlayMusicButton, isMonitor(Monitors.PLAYLIST));
        IKSGScreenUtil.setActive(this.addPlayMusicButton, currentPlayList != PlayList.ALL);
        IKSGScreenUtil.setVisible(this.addPlayMusicNameField, isMonitor(Monitors.ADDPLAYMUSIC1));
        IKSGScreenUtil.setVisible(this.addPlayMusicSourceField, isMonitor(Monitors.ADDPLAYMUSIC1));
        IKSGScreenUtil.setVisible(this.addPlayMusicSourceReferenceButton, isMonitor(Monitors.ADDPLAYMUSIC1));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSarchButton, isMonitor(Monitors.ADDPLAYMUSIC1) && musicSourceClientReferencesType == MusicSourceClientReferencesType.YOUTUBE);
        IKSGScreenUtil.setVisible(this.addPlayMusicOpenFolder, isMonitor(Monitors.ADDPLAYMUSIC1) && musicSourceClientReferencesType == MusicSourceClientReferencesType.LOCAL_FILE);


        IKSGScreenUtil.setVisible(this.nextAddPlayMusic, isMonitor(Monitors.ADDPLAYMUSIC1));
        IKSGScreenUtil.setActive(this.nextAddPlayMusic, image != null && !addPlayMusicNameField.getText().isEmpty() && musicLoadResult == MusicLoadResult.AVAILABLE);
        IKSGScreenUtil.setVisible(this.addPlayMusicArtistField, isMonitor(Monitors.ADDPLAYMUSIC2));
        IKSGScreenUtil.setVisible(this.addPlayMusicAlbumField, isMonitor(Monitors.ADDPLAYMUSIC2));
        IKSGScreenUtil.setVisible(this.addPlayMusicYearField, isMonitor(Monitors.ADDPLAYMUSIC2));
        IKSGScreenUtil.setVisible(this.addPlayMusicGenreField, isMonitor(Monitors.ADDPLAYMUSIC2));
        IKSGScreenUtil.setVisible(this.addPlayMusic2BackButton, isMonitor(Monitors.ADDPLAYMUSIC2));
        IKSGScreenUtil.setVisible(this.addPlayMusic2CrateButton, isMonitor(Monitors.ADDPLAYMUSIC2));
        IKSGScreenUtil.setActive(this.addPlayMusic2CrateButton, !this.addPlayMusicSourceField.getText().isEmpty() && !this.addPlayMusicNameField.getText().isEmpty() && this.uploadLocation != null && musicLoadResult == MusicLoadResult.AVAILABLE);
        IKSGScreenUtil.setVisible(this.addPlayMusic2UploadSelectWorld, isMonitor(Monitors.ADDPLAYMUSIC2));
        IKSGScreenUtil.setActive(this.addPlayMusic2UploadSelectWorld, this.musicSourceClientReferencesType == MusicSourceClientReferencesType.URL || this.musicSourceClientReferencesType == MusicSourceClientReferencesType.LOCAL_FILE);
        IKSGScreenUtil.setVisible(this.addPlayMusic2UploadSelectURL, isMonitor(Monitors.ADDPLAYMUSIC2));
        IKSGScreenUtil.setActive(this.addPlayMusic2UploadSelectURL, this.musicSourceClientReferencesType == MusicSourceClientReferencesType.URL && this.formattype != null && this.formattype.equals("mp3"));
        // IKSGScreenUtil.setVisible(this.addPlayMusic2UploadSelectGitHub, isMonitor(Monitors.ADDPLAYMUSIC2));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSerchBackButton, isMonitor(Monitors.ADDPLAYMUSICYOUTUBESELECT));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSearchField, isMonitor(Monitors.ADDPLAYMUSICYOUTUBESELECT));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSearchlistbar, isMonitor(Monitors.ADDPLAYMUSICYOUTUBESELECT));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSearchButton, isMonitor(Monitors.ADDPLAYMUSICYOUTUBESELECT));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSearchFileListButton, isMonitor(Monitors.ADDPLAYMUSICYOUTUBESELECT) && musicSourceClientReferencesType == MusicSourceClientReferencesType.LOCAL_FILE);
    }

    private void fieldTick() {
        if (isMonitor(Monitors.CREATEPLAYLIST)) {
            createGuildNameField.tick();
        }
        if (isMonitor(Monitors.ADDPLAYMUSIC1)) {
            addPlayMusicNameField.tick();
            addPlayMusicSourceField.tick();
        }
        if (isMonitor(Monitors.ADDPLAYMUSIC2)) {
            addPlayMusicArtistField.tick();
            addPlayMusicAlbumField.tick();
            addPlayMusicYearField.tick();
            addPlayMusicGenreField.tick();
        }
        if (isMonitor(Monitors.ADDPLAYMUSICYOUTUBESELECT)) {
            addPlayMusicYoutubeSearchField.tick();
        }
        if (!isMonitor(Monitors.ADDPLAYMUSICYOUTUBESELECT)) {
            addPlayMusicYoutubeSearchField.setText("");
        }
        if (!isMonitor(Monitors.CREATEPLAYLIST)) {
            createGuildNameField.setText("");
        }
        if (!isMonitor(Monitors.ADDPLAYMUSIC1, Monitors.ADDPLAYMUSIC2, Monitors.ADDPLAYMUSICYOUTUBESELECT)) {
            addPlayMusicNameField.setText("");
            addPlayMusicSourceField.setText("");
            addPlayMusicArtistField.setText("");
            addPlayMusicAlbumField.setText("");
            addPlayMusicYearField.setText("");
            addPlayMusicGenreField.setText("");
            if (!isMonitor(Monitors.ADDPLAYMUSIC1)) {
                if (musicLoadResult != null)
                    musicLoadResult = null;
                if (sourceCheckThread != null && !sourceCheckThread.isStop())
                    sourceCheckThread.setStop(true);
            }
        }
    }

    private boolean isMonitor(Monitors... mo) {
        return isSlectedMonitor(Monitorsa, mo);
    }

    private boolean isSlectedMonitor(Monitors mo, Monitors... mos) {
        return Arrays.asList(mos).contains(mo);
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

        if (isSlectedMonitor(moniter, Monitors.PLAYLIST)) {
            tag.putString("listuuid", currentPlayList.getUUID());
        }

        this.instruction("mode", tag);
        if (isSlectedMonitor(moniter, Monitors.CREATEPLAYLIST, Monitors.ADDPLAYMUSIC1)) {
            Path picPath = getPicturPath();
            if (picPath != null) {
                DropAndDragFileLoadThread plt = new DropAndDragFileLoadThread(true, this, picPath);
                plt.start();
            }
        }

        if (!isSlectedMonitor(moniter, Monitors.CREATEPLAYLIST, Monitors.ADDPLAYMUSIC1, Monitors.ADDPLAYMUSIC2, Monitors.ADDPLAYMUSICYOUTUBESELECT)) {
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

    private void updatePlayMusic() {
        if (!isMonitor(Monitors.PLAYLIST))
            return;
        CompoundNBT tag = new CompoundNBT();
        tag.putString("listuuid", currentPlayList.getUUID());
        instruction("playmusicupdate", tag);
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
                updatePlayMusic();
            }

            @Override
            public long time(ClockTimer clockTimer) {
                return 3000;
            }
        });


    }

    @Override
    public void instructionReturn(String name, CompoundNBT tag) {

        if (name.equals("playlistupdate") || name.equals("mode") || name.equals("power")) {
            CompoundNBT data = name.equals("playlistupdate") ? tag : tag.getCompound("playlist");
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

        if (name.equals("playmusicupdate") || ((name.equals("mode") || name.equals("power")) && tag.contains("playmusic"))) {
            CompoundNBT data = name.equals("playmusicupdate") ? tag : tag.getCompound("playmusic");
            CompoundNBT taga = data.getCompound("list");
            currentPlaylistsMusics.clear();
            for (String pmtagst : taga.keySet()) {
                currentPlaylistsMusics.add(new PlayMusic(pmtagst, taga.getCompound(pmtagst)));
            }
        }

    }


    public void insPower(boolean on) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("on", on);
        tag.putString("listuuid", currentPlayList.getUUID());
        this.instruction("power", tag);
    }


    private void setMonitorsa() {
        if (!isStateOn())
            Monitorsa = Monitors.OFF;
        else
            Monitorsa = Monitors.getValueOf(getMode());
    }

    protected void drawAddPlayMusicYoutubeSelect(MatrixStack matrx, float partTick, int mouseX, int mouseY) {
        drawFontString(matrx, new TranslationTextComponent("msd.addplaymusicsourceslect"), getMonitorStartX() + 2, getMonitorStartY() + 2);
        IKSGRenderUtil.guiBindAndBlit(YOUTUBE_ICON, matrx, getMonitorStartX() + 2, getMonitorStartY() + 16, 0, 0, 27, 7, 27, 7);
        addPlayMusicYoutubeSearchField.render(matrx, mouseX, mouseY, partTick);
    }

    protected void drawAddPlayMusic2(MatrixStack matrx, float partTick, int mouseX, int mouseY) {
        drawFontString(matrx, new TranslationTextComponent("msd.addplaymusic"), getMonitorStartX() + 2, getMonitorStartY() + 2);
        drawFontString(matrx, new TranslationTextComponent("msd.artist"), getMonitorStartX() + 6, getMonitorStartY() + 17);
        drawFontString(matrx, new TranslationTextComponent("msd.album"), getMonitorStartX() + 107, getMonitorStartY() + 17);
        drawFontString(matrx, new TranslationTextComponent("msd.year"), getMonitorStartX() + 6, getMonitorStartY() + 43);
        drawFontString(matrx, new TranslationTextComponent("msd.genre"), getMonitorStartX() + 107, getMonitorStartY() + 43);
        drawFontString(matrx, new TranslationTextComponent("msd.uploadlocation"), getMonitorStartX() + 6, getMonitorStartY() + 69);

        if (uploadLocation != null)
            drawFontString(matrx, new TranslationTextComponent("uploadlocation." + uploadLocation.name().toLowerCase() + ".desc"), getMonitorStartX() + 6, getMonitorStartY() + 95);

        IKSGRenderUtil.matrixPush(matrx);
        addPlayMusicArtistField.render(matrx, mouseX, mouseY, partTick);
        addPlayMusicAlbumField.render(matrx, mouseX, mouseY, partTick);
        addPlayMusicYearField.render(matrx, mouseX, mouseY, partTick);
        addPlayMusicGenreField.render(matrx, mouseX, mouseY, partTick);
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        IKSGRenderUtil.matrixPop(matrx);
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
            if (musicLoadResult != null) {
                drawFontString(matrx, musicLoadResult.getLocalizedName(), getMonitorStartX() + 92, getMonitorStartY() + 70);
                if (musicLoadResult == MusicLoadResult.AVAILABLE && formattype != null)
                    drawFontString(matrx, new TranslationTextComponent("msd.formattype", formattype), getMonitorStartX() + 92, getMonitorStartY() + 78);
            }
        }

        IKSGRenderUtil.matrixPush(matrx);
        addPlayMusicNameField.render(matrx, mouseX, mouseY, partTick);
        addPlayMusicSourceField.render(matrx, mouseX, mouseY, partTick);
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
        createGuildNameField.render(matrx, mouseX, mouseY, partTick);
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        IKSGRenderUtil.matrixPop(matrx);
    }

    protected void drawPlayList(MatrixStack matrx, float partTick, int mouseX, int mouseY) {
        drawFontString(matrx, new StringTextComponent(currentPlayList.getName()), getMonitorStartX() + 31, getMonitorStartY() + 2);
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
        IKSGRenderUtil.drawCenterString(this.font, matx, IKSGStyles.withStyle(text, fontStyle), x, y, 0);
    }

    protected void drawFontString(MatrixStack matx, IFormattableTextComponent text, int x, int y) {
        IKSGRenderUtil.drawString(this.font, matx, IKSGStyles.withStyle(text, fontStyle), x, y, 0);
    }

    private enum Monitors {
        OFF(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_sharing_device_screen/msd_monitor_off.png"), "off"),
        ON(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_sharing_device_screen/msd_monitor_on.png"), "on"),
        PLAYLIST(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_sharing_device_screen/msd_monitor_list.png"), "playlist"),
        NOANTENNA(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_sharing_device_screen/msd_monitor_noantenna.png"), "noantenna"),
        CREATEPLAYLIST(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_sharing_device_screen/msd_monitor_createplaylist.png"), "createplaylist"),
        ADDPLAYLIST(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_sharing_device_screen/msd_monitor_addplaylist.png"), "addplaylist"),
        JOINPLAYLIST(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_sharing_device_screen/msd_monitor_joinplaylist.png"), "joinplaylist"),
        ADDPLAYMUSIC1(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_sharing_device_screen/msd_monitor_addplaymusic_1.png"), "addplaymusic1"),
        ADDPLAYMUSIC2(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_sharing_device_screen/msd_monitor_addplaymusic_2.png"), "addplaymusic2"),
        ADDPLAYMUSICYOUTUBESELECT(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_sharing_device_screen/msd_monitor_addplaymusic_youtubeselect.png"), "addplaymusicyoutubeselect");
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

    public PlayList getCurrentPlayList() {
        return currentPlayList;
    }

    public void setCurrentPlayList(PlayList playList) {
        this.currentPlayList = playList;
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
        if (isMonitor(Monitors.CREATEPLAYLIST, Monitors.ADDPLAYMUSIC1, Monitors.ADDPLAYMUSICYOUTUBESELECT)) {
            if (dragFiles.size() == 1 && !pictuerLoading) {
                DropAndDragFileLoadThread lt = new DropAndDragFileLoadThread(false, this, dragFiles.get(0));
                lt.start();
            }
        }
    }

    @Override
    public void onCloseByIKSG() {
        super.onCloseByIKSG();
    }

    @Override
    public boolean mouseDragged(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_, double p_231045_8_) {
        boolean flag1 = super.mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
        boolean flag2 = this.getListener() != null && this.isDragging() && p_231045_5_ == 0 && this.getListener().mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
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

    public enum MusicLoadResult {
        AVAILABLE("available"),
        NO_SUPPORT_FORMAT("nosupportformat"),
        FILE_NOT_EXIST("filenotexist"),
        INVALID_URL("invalidurl"),
        STREAM("stream");

        private final String name;

        MusicLoadResult(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public TranslationTextComponent getLocalizedName() {
            return new TranslationTextComponent("musicloadresult." + name);
        }

    }

    public enum UploadLocation {
        WORLD,
        URL,
        GITHUB
    }

    public void setMusicLoadError(MusicLoadResult musicLoadError) {
        this.musicLoadResult = musicLoadError;
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
                    BufferedImage outbfi = IKSGPictuerUtil.resize(bfi, aw, ah);
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
                screen.musicLoadResult = null;
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
            screen.musicLoadResult = null;

            if (screen.getMusicSourceClientReferencesType() == MusicSourceClientReferencesType.LOCAL_FILE) {
                try {
                    Path path = Paths.get(source);
                    if (!path.toFile().exists()) {
                        if (!this.stop) {
                            screen.musicLoadResult = MusicLoadResult.FILE_NOT_EXIST;
                            screen.musicLoading = false;
                        }
                        return;
                    }

                    try {
                        MultimediaObject mo = new MultimediaObject(path.toFile());
                        Encoder encoder = new Encoder();
                        if (Arrays.asList(encoder.getSupportedEncodingFormats()).contains(mo.getInfo().getFormat())) {
                            MultimediaInfo info = mo.getInfo();
                            if (info.getFormat().equals("mp3")) {
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
                                    if (id3v2.getArtist() != null) {
                                        if (!this.stop)
                                            screen.addPlayMusicArtistField.setText(id3v2.getArtist());
                                    } else {
                                        if (!this.stop)
                                            screen.addPlayMusicArtistField.setText("");
                                    }
                                    if (id3v2.getAlbum() != null) {
                                        if (!this.stop)
                                            screen.addPlayMusicAlbumField.setText(id3v2.getAlbum());
                                    } else {
                                        if (!this.stop)
                                            screen.addPlayMusicAlbumField.setText("");
                                    }
                                    if (id3v2.getYear() != null) {
                                        if (!this.stop)
                                            screen.addPlayMusicYearField.setText(id3v2.getYear());
                                    } else {
                                        if (!this.stop)
                                            screen.addPlayMusicYearField.setText("");
                                    }
                                    if (id3v2.getGenreDescription() != null) {
                                        if (!this.stop)
                                            screen.addPlayMusicGenreField.setText(id3v2.getGenreDescription());
                                    } else {
                                        if (!this.stop)
                                            screen.addPlayMusicGenreField.setText("");
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
                            } else {
                                if (!this.stop)
                                    screen.addPlayMusicNameField.setText(IKSGStringUtil.deleteExtension(path.toFile().getName()));
                            }
                            if (!this.stop)
                                screen.formattype = info.getFormat();
                        } else {
                            if (!this.stop) {
                                screen.musicLoadResult = MusicLoadResult.NO_SUPPORT_FORMAT;
                                screen.musicLoading = false;
                            }
                            return;
                        }
                    } catch (Exception ex) {
                        if (!this.stop) {
                            screen.musicLoadResult = MusicLoadResult.NO_SUPPORT_FORMAT;
                            screen.musicLoading = false;
                        }
                        return;
                    }
                } catch (Exception ex) {
                    if (!this.stop) {
                        screen.musicLoadResult = MusicLoadResult.FILE_NOT_EXIST;
                        screen.musicLoading = false;
                    }
                    return;
                }
            } else if (screen.getMusicSourceClientReferencesType() == MusicSourceClientReferencesType.URL) {
                try {
                    URL url = new URL(source);
                    MultimediaObject mo = new MultimediaObject(url);
                    Encoder encoder = new Encoder();
                    if (Arrays.asList(encoder.getSupportedEncodingFormats()).contains(mo.getInfo().getFormat())) {
                        MultimediaInfo info = mo.getInfo();
                        if (info.getDuration() == -1) {
                            if (!this.stop) {
                                screen.musicLoadResult = MusicLoadResult.STREAM;
                                screen.musicLoading = false;
                            }
                            return;
                        }
                        if (!this.stop) {
                            screen.formattype = info.getFormat();
                        }
                    } else {
                        if (!this.stop) {
                            screen.musicLoadResult = MusicLoadResult.NO_SUPPORT_FORMAT;
                            screen.musicLoading = false;
                        }
                        return;
                    }
                } catch (Exception ex) {
                    if (!this.stop) {
                        screen.musicLoadResult = MusicLoadResult.INVALID_URL;
                        screen.musicLoading = false;
                    }
                    return;
                }
            }

            if (!this.stop) {
                screen.musicLoadResult = MusicLoadResult.AVAILABLE;
                screen.musicLoading = false;
            }
        }

    }

    private static class YoutubeSearchThread extends Thread {
        private final MusicSharingDeviceScreen screen;
        private final String searchText;
        private boolean stop;


        public YoutubeSearchThread(MusicSharingDeviceScreen screen, String searchText) {
            this.screen = screen;
            this.searchText = searchText;
        }

        public void run() {
            if (this.stop || searchText.isEmpty())
                return;

            screen.youtubeSearchLoading = true;
            screen.youtubeResilts.clear();
            IamMusicPlayer.LOGGER.info("Youtube Search: " + searchText);
            List<AudioTrack> list = YoutubeUtils.getVideoSearchResults(searchText);
            IamMusicPlayer.LOGGER.info("Youtube Search Finished: " + searchText);
            if (!this.stop)
                screen.youtubeResilts.addAll(list);
            screen.youtubeSearchLoading = false;
        }
    }
}
