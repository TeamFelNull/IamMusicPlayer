package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.data.MusicSourceClientReferencesType;
import red.felnull.imp.client.gui.widget.*;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.client.util.FileUtils;
import red.felnull.imp.client.util.RenderUtil;
import red.felnull.imp.client.util.YoutubeUtils;
import red.felnull.imp.container.MusicSharingDeviceContainer;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.data.PlayMusicManeger;
import red.felnull.imp.item.IMPItems;
import red.felnull.imp.music.resource.PlayImage;
import red.felnull.imp.music.resource.PlayList;
import red.felnull.imp.music.resource.PlayLocation;
import red.felnull.imp.music.resource.PlayMusic;
import red.felnull.imp.tileentity.MusicSharingDeviceTileEntity;
import red.felnull.imp.util.FFmpegUtils;
import red.felnull.imp.util.PathUtils;
import red.felnull.otyacraftengine.client.gui.IkisugiDialogTexts;
import red.felnull.otyacraftengine.client.gui.widget.ChangeableImageButton;
import red.felnull.otyacraftengine.client.gui.widget.Checkbox;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.StringImageButton;
import red.felnull.otyacraftengine.client.util.IKSGClientUtil;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGScreenUtil;
import red.felnull.otyacraftengine.client.util.IKSGTextureUtil;
import red.felnull.otyacraftengine.util.*;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MusicSharingDeviceScreen extends IMPAbstractPLEquipmentScreen<MusicSharingDeviceContainer> {

    public static final ResourceLocation MSD_GUI_TEXTURES = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device_1.png");
    public static final ResourceLocation MSD_GUI_TEXTURES2 = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device_2.png");
    private static final ResourceLocation YOUTUBE_ICON = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/youtube_icon.png");
    private final List<PlayList> jonPlaylists = new ArrayList<>();
    private final List<String> playListPlayers = new ArrayList<>();
    private final boolean canOpenFileChooser;
    public List<AudioTrack> youtubeResilts = new ArrayList<>();
    public TextFieldWidget addPlayMusicSourceField;
    public TextFieldWidget addPlayMusicYoutubeSearchField;
    protected ClockTimer timer;
    private PlayImage image;
    private boolean youtubeSearchLoading;
    private YoutubeSearchThread youtubeSearchThread;
    private byte[] picturImage;
    private boolean pictuerLoading;
    private boolean musicLoading;
    private MusicLoadResult musicLoadResult;
    //   public UploadLocation uploadLocation;
    public IMusicPlayer musicPlayer;
    public MusicPlayThread musicPlayThread;

    private MusicSourceClientReferencesType musicSourceClientReferencesType;
    private String formattype;
    private boolean initFrist;
    private SourceCheckThread sourceCheckThread;
    private StringImageButton allbutton;
    private ImageButton addGuildButton;
    private ScrollBarSlider guildlistbar;
    private ScrollBarSlider playlistbar;
    private PlayListScrollButton guildButtons;
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
    private ScrollBarSlider addPlayMusicYoutubeSearchlistbar;
    private ChangeableImageButton addPlayMusicYoutubeSearchButton;
    private YoutubeSearchResultScrollListButton addPlayMusicYoutubeSearchFileListButton;
    private StringImageButton playlistDetails;
    private StringImageButton playlistDetailsBack;
    private StringImageButton playlistDetailsSave;
    public TextFieldWidget playlistDetailsNameChangeField;
    private Checkbox playlistDetailsAnyoneCheckbox;

    public MusicSharingDeviceScreen(MusicSharingDeviceContainer screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
        this.xSize = 215;
        this.ySize = 242;
        this.canOpenFileChooser = FileUtils.isCanOpenFileChooser();
        this.playerInventoryTitleY = this.ySize - 94;
    }

    @Override
    public int getMonitorStartX() {
        return getTexturStartX() + 8;
    }

    @Override
    public int getMonitorStartY() {
        return getTexturStartY() + 20;
    }

    @Override
    public int getMonitorXsize() {
        return 199;
    }

    @Override
    public int getMonitorYsize() {
        return 122;
    }

    @Override
    public void initByIKSG() {
        super.initByIKSG();
        updateAll();
        updatePlayListPlayers();
        timerSet();

        if (musicSourceClientReferencesType == null)
            this.musicSourceClientReferencesType = MusicSourceClientReferencesType.LOCAL_FILE;

        this.pictuerLoading = false;
        this.musicLoading = false;

        this.allbutton = addStringImageButton(new TranslationTextComponent("playlist.all"), 1, 1, 18, 18, 0, 40, n -> {
            setCurrentSelectedPlayList(PlayList.ALL);
            updatePlayMusic();
        });

        this.addGuildButton = this.addWidgetByIKSG(new ImageButton(getMonitorStartX() + 20, getMonitorStartY() + 1, 9, 18, 235, 40, 18, MSD_GUI_TEXTURES, n -> {
            insMode(MusicSharingDeviceTileEntity.Screen.ADD_PLAYLIST);
        }));
        IKSGScreenUtil.setVisible(this.addGuildButton, false);

        this.guildlistbar = this.addWidgetByIKSG(new ScrollBarSlider(getMonitorStartX() + 20, getMonitorStartY() + 20, 101, 100, 0, -20, 0, 76, EQUIPMENT_WIDGETS_TEXTURES));
        IKSGScreenUtil.setVisible(this.guildlistbar, false);

        this.playlistbar = this.addWidgetByIKSG(new ScrollBarSlider(getMonitorStartX() + 189, getMonitorStartY() + 20, 101, 100, 0, -159, 0, 76, EQUIPMENT_WIDGETS_TEXTURES));
        IKSGScreenUtil.setVisible(this.playlistbar, false);

        this.guildButtons = this.addWidgetByIKSG(new PlayListScrollButton(getMonitorStartX() + 1, getMonitorStartY() + 20, 101, guildlistbar, (n, m) -> {
            setCurrentSelectedPlayList(getJonedAllPlayLists().get(m));
            updateAll();
        }, this, false));
        IKSGScreenUtil.setVisible(this.guildButtons, false);

        this.playlistButtons = this.addWidgetByIKSG(new PlayMusicScrollButton(getMonitorStartX() + 30, getMonitorStartY() + 20, 158, 101, playlistbar, (n, m) -> {
            PlayMusic music = getCurrentPLPlayMusic().get(m);
            playMusic(MusicSourceClientReferencesType.getTypeByLocationType(music.getMusicLocation().getLocationType()), music.getMusicLocation().getIdOrURL());
        }, this, this, false));
        IKSGScreenUtil.setVisible(this.playlistButtons, false);

        this.backGuid = addSmartStringButton((TranslationTextComponent) IkisugiDialogTexts.BACK, 92, 92, n -> insMode(MusicSharingDeviceTileEntity.Screen.PLAYLIST));

        this.createGuid = addSmartStringButton((TranslationTextComponent) IkisugiDialogTexts.CRATE, 145, 92, n -> {
            PlayListGuildManeger.instance().createPlayListRequest(createGuildNameField.getText(), image, picturImage, createAnyoneCheckbox.isCheck());
            insMode(MusicSharingDeviceTileEntity.Screen.PLAYLIST);
        });

        this.resetImage = this.addWidgetByIKSG(new ChangeableImageButton(getMonitorStartX() + 86 - 16, getMonitorStartY() + 106 - 8, 8, 8, 215, 198, 8, MSD_GUI_TEXTURES, n -> {
            removePictuerPath();
            if (isMonitor(MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST)) {
                setImage(PlayImage.ImageType.STRING, createGuildNameField.getText());
            } else if (isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1)) {
                setImage(PlayImage.ImageType.STRING, addPlayMusicNameField.getText());
            }
        }));
        IKSGScreenUtil.setVisible(this.resetImage, false);

        this.openImage = this.addWidgetByIKSG(new ChangeableImageButton(getMonitorStartX() + 86 - 8, getMonitorStartY() + 106 - 8, 8, 8, 223, 198, 8, MSD_GUI_TEXTURES, n -> {
            FileUtils.openFileChoser(I18n.format("msd.openImageFile"), file -> {
                if (file != null && this.isOpend() && isMonitor(MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST, MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1) && !file.isDirectory()) {
                    DropAndDragFileLoadThread lt = new DropAndDragFileLoadThread(true, file.toPath());
                    lt.start();
                    //    ImageFileChooser.setInitialDirectory(file.getParentFile());
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
            if (isMonitor(MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST) && image.getImageType() == PlayImage.ImageType.STRING) {
                setImage(PlayImage.ImageType.STRING, n);
            }
        });
        IKSGScreenUtil.setVisible(this.createGuildNameField, false);

        this.createAnyoneCheckbox = this.addWidgetByIKSG(new Checkbox(getMonitorStartX() + 92, getMonitorStartY() + 56, 15, 15, 215, 96, 256, 256, MSD_GUI_TEXTURES));
        IKSGScreenUtil.setVisible(this.createAnyoneCheckbox, false);

        this.createJoinGuid = addSmartStringButton((TranslationTextComponent) IkisugiDialogTexts.CRATE, getMonitorXsize() / 2 - 48 - 5, getMonitorYsize() / 2, n -> insMode(MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST));

        this.addJoinGuid = addSmartStringButton((TranslationTextComponent) IkisugiDialogTexts.JOIN, getMonitorXsize() / 2 + 5, getMonitorYsize() / 2, n -> insMode(MusicSharingDeviceTileEntity.Screen.JOIN_PLAYLIST));

        this.backJoinGuid = addSmartStringButton((TranslationTextComponent) IkisugiDialogTexts.BACK, getMonitorXsize() / 2 - 24, getMonitorYsize() / 2 + 18, n -> insMode(MusicSharingDeviceTileEntity.Screen.PLAYLIST));


        this.joinplaylistbar = this.addWidgetByIKSG(new ScrollBarSlider(getMonitorStartX() + 189, getMonitorStartY() + 20, 101, 100, 0, -189, 0, 76, EQUIPMENT_WIDGETS_TEXTURES));
        IKSGScreenUtil.setVisible(this.joinplaylistbar, false);

        this.JoinPlayListScrollButtons = this.addWidgetByIKSG(new JoinPlayListScrollButton(getMonitorStartX() + 1, getMonitorStartY() + 20, 187, 101, 40, joinplaylistbar, jonPlaylists, (n, m) -> {
            PlayListGuildManeger.instance().joinPlayListRequest(jonPlaylists.get(m).getUUID());
            insMode(MusicSharingDeviceTileEntity.Screen.PLAYLIST);
        }));
        IKSGScreenUtil.setVisible(this.JoinPlayListScrollButtons, false);

        this.joinplaylistbackButton = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + 1, getMonitorStartY() + 12, 14, 7, 0, 30, 14, MSD_GUI_TEXTURES2, n -> {
            insMode(MusicSharingDeviceTileEntity.Screen.PLAYLIST);
        }, IKSGStyles.withStyle((TranslationTextComponent) IkisugiDialogTexts.BACK, smart_fontStyle)));
        this.joinplaylistbackButton.setSizeAdjustment(true);
        this.joinplaylistbackButton.setShadwString(false);
        this.joinplaylistbackButton.setStringColor(0);
        this.joinplaylistbackButton.setScale(0.5f);
        IKSGScreenUtil.setVisible(this.joinplaylistbackButton, false);


        this.addPlayMusicButton = this.addWidgetByIKSG(new ChangeableImageButton(getMonitorStartX() + 189, getMonitorStartY() + 1, 9, 18, 235, 40, 18, MSD_GUI_TEXTURES, n -> {
            insMode(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1);
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
            if (isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1) && image.getImageType() == PlayImage.ImageType.STRING) {
                setImage(PlayImage.ImageType.STRING, n);
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
            if (isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1, MusicSharingDeviceTileEntity.Screen.YOUTUBE_SEARCH)) {
                if (musicSourceClientReferencesType != MusicSourceClientReferencesType.YOUTUBE || YoutubeUtils.isYoutubeURL(n)) {
                    if (this.sourceCheckThread != null)
                        this.sourceCheckThread.setStop(true);
                    this.sourceCheckThread = new SourceCheckThread(n);
                    this.sourceCheckThread.start();
                }
            }
        });
        IKSGScreenUtil.setVisible(this.addPlayMusicSourceField, false);
        this.addPlayMusicSourceReferenceButton = this.addWidgetByIKSG(new PlayMusicSourceReferenceButton(getMonitorStartX() + 175, getMonitorStartY() + 52, 18, 15, 215, 168, 15, MSD_GUI_TEXTURES, n -> {
            setMusicLoadError(null);
            addPlayMusicSourceField.setText("");
            addPlayMusicGenreField.setText("");
            addPlayMusicYearField.setText("");
            addPlayMusicAlbumField.setText("");
            addPlayMusicArtistField.setText("");
            formattype = null;
            removePictuerPath();
        }, this));
        IKSGScreenUtil.setVisible(this.addPlayMusicSourceReferenceButton, false);

        this.addPlayMusicYoutubeSarchButton = this.addWidgetByIKSG(new ChangeableImageButton(getMonitorStartX() + 157, getMonitorStartY() + 52, 18, 15, 233, 168, 15, MSD_GUI_TEXTURES, n -> {
            insMode(MusicSharingDeviceTileEntity.Screen.YOUTUBE_SEARCH);
        }));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSarchButton, false);
        this.addPlayMusicOpenFolder = this.addWidgetByIKSG(new ChangeableImageButton(getMonitorStartX() + 157, getMonitorStartY() + 52, 18, 15, 215, 214, 15, MSD_GUI_TEXTURES, n -> {
            FileUtils.openFileChoser(I18n.format("msd.openSoundFile"), file -> {
                if (file != null && this.isOpend() && isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1)) {
                    if (!file.isDirectory()) {
                        addPlayMusicSourceField.setText(file.getPath());
                    }
                }
            });
        }));
        IKSGScreenUtil.setVisible(this.addPlayMusicOpenFolder, false);

        this.nextAddPlayMusic = addSmartStringButton((TranslationTextComponent) IkisugiDialogTexts.NEXT, 145, 92, n -> insMode(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_2));

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

        this.addPlayMusic2BackButton = addSmartStringButton((TranslationTextComponent) IkisugiDialogTexts.BACK, getMonitorXsize() / 2 - 48 - 5, 105, n -> insMode(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1));

        this.addPlayMusic2CrateButton = addSmartStringButton((TranslationTextComponent) IkisugiDialogTexts.CRATE, getMonitorXsize() / 2 + 5, 105, n -> {
            PlayLocation location = new PlayLocation(musicSourceClientReferencesType.getLocationType(), musicSourceClientReferencesType == MusicSourceClientReferencesType.LOCAL_FILE ? UUID.randomUUID().toString() : this.addPlayMusicSourceField.getText());
            PlayMusicManeger.instance().createPlayMusicRequest(this.addPlayMusicNameField.getText(), getCurrentSelectedPlayList(), this.image, picturImage, location, musicSourceClientReferencesType, this.addPlayMusicSourceField.getText(), this.addPlayMusicArtistField.getText(), this.addPlayMusicAlbumField.getText(), this.addPlayMusicYearField.getText(), this.addPlayMusicGenreField.getText());
            insMode(MusicSharingDeviceTileEntity.Screen.PLAYLIST);
        });

        this.addPlayMusic2UploadSelectWorld = this.addWidgetByIKSG(new UploadLocationSelectButton(this, UploadLocation.WORLD, getMonitorStartX() + 6, getMonitorStartY() + 78, 53, 15, 0, 44, 15, MSD_GUI_TEXTURES2, n -> {
            //       this.uploadLocation = UploadLocation.WORLD;
        }, false));
        IKSGScreenUtil.setVisible(this.addPlayMusic2UploadSelectWorld, false);
        this.addPlayMusic2UploadSelectURL = this.addWidgetByIKSG(new UploadLocationSelectButton(this, UploadLocation.URL, getMonitorStartX() + 73, getMonitorStartY() + 78, 53, 15, 0, 44, 15, MSD_GUI_TEXTURES2, n -> {
            //        this.uploadLocation = UploadLocation.URL;
        }, false));
        IKSGScreenUtil.setVisible(this.addPlayMusic2UploadSelectURL, false);

        this.addPlayMusic2UploadSelectGitHub = this.addWidgetByIKSG(new UploadLocationSelectButton(this, UploadLocation.GITHUB, getMonitorStartX() + 140, getMonitorStartY() + 78, 53, 15, 0, 104, 15, MSD_GUI_TEXTURES2, n -> {
            //      this.uploadLocation = UploadLocation.GITHUB;
        }, true));
        IKSGScreenUtil.setVisible(this.addPlayMusic2UploadSelectGitHub, false);
        IKSGScreenUtil.setActive(this.addPlayMusic2UploadSelectGitHub, false);


        this.addPlayMusicYoutubeSerchBackButton = addSmartStringButton((TranslationTextComponent) IkisugiDialogTexts.BACK, 150, 12, n -> insMode(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1));

        String SelectionMusicSearchField = "";
        if (this.addPlayMusicYoutubeSearchField != null)
            SelectionMusicSearchField = this.addPlayMusicYoutubeSearchField.getText();

        this.addPlayMusicYoutubeSearchField = this.addWidgetByIKSG(new TextFieldWidget(this.font, getMonitorStartX() + 4 + 29, getMonitorStartY() + 15, 91, 12, new StringTextComponent("test")));
        this.addPlayMusicYoutubeSearchField.setEnableBackgroundDrawing(false);
        this.addPlayMusicYoutubeSearchField.setMaxStringLength(Integer.MAX_VALUE);
        this.addPlayMusicYoutubeSearchField.setTextColor(-1);
        this.addPlayMusicYoutubeSearchField.setDisabledTextColour(-1);
        this.addPlayMusicYoutubeSearchField.setText(SelectionMusicSearchField);
        this.addPlayMusicYoutubeSearchField.setResponder(n ->

        {
        });
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSearchField, false);
        this.addPlayMusicYoutubeSearchlistbar = this.addWidgetByIKSG(new ScrollBarSlider(getMonitorStartX() + 189, getMonitorStartY() + 28, 93, 100, 0, -189, 0, 76, EQUIPMENT_WIDGETS_TEXTURES));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSearchlistbar, false);

        this.addPlayMusicYoutubeSearchButton = this.addWidgetByIKSG(new ChangeableImageButton(getMonitorStartX() + 102 + 29, getMonitorStartY() + 12, 18, 15, 233, 168, 15, MSD_GUI_TEXTURES, n -> {
            if (this.youtubeSearchThread != null)
                youtubeSearchThread.stop = true;
            youtubeSearchThread = new YoutubeSearchThread(addPlayMusicYoutubeSearchField.getText());
            youtubeSearchThread.start();
        }));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSearchButton, false);


        this.addPlayMusicYoutubeSearchFileListButton = this.addWidgetByIKSG(new YoutubeSearchResultScrollListButton(getMonitorStartX() + 1, getMonitorStartY() + 28, 187, 93, 40, addPlayMusicYoutubeSearchlistbar, this, (n, m) -> {
            AudioTrack at = youtubeResilts.get(m);
            if (IKSGClientUtil.isKeyInput(getMinecraft().gameSettings.keyBindSneak, false)) {
                playYoutubeMusic(at.getInfo().identifier);
            } else {
                if (!at.getInfo().isStream) {
                    removePictuerPath();
                    if (YoutubeUtils.getThumbnailURL(at.getIdentifier()) != null) {
                        setImage(PlayImage.ImageType.URLIMAGE, YoutubeUtils.getThumbnailURL(at.getIdentifier()));
                    } else {
                        setImage(PlayImage.ImageType.STRING, at.getInfo().title);
                    }
                    addPlayMusicSourceField.setText(at.getInfo().identifier);
                    addPlayMusicNameField.setText(at.getInfo().title);
                    addPlayMusicArtistField.setText(at.getInfo().author);
                    this.musicLoadResult = MusicLoadResult.AVAILABLE;
                    this.formattype = "Youtube";
                    insMode(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1);
                } else {
                    setMusicLoadError(MusicLoadResult.STREAM);
                }
            }
        }));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSearchFileListButton, false);

        this.playlistDetails = addStringImageButton(new TranslationTextComponent("msd.details"), 156, 1, 32, 10, 109, 0, n -> {
            this.playlistDetailsNameChangeField.setText(getCurrentSelectedPlayList().getName());
            this.playlistDetailsAnyoneCheckbox.setCheck(getCurrentSelectedPlayList().isAnyone());
            insMode(MusicSharingDeviceTileEntity.Screen.PLAYLIST_DETAILS);
        });

        this.playlistDetailsBack = addSmartStringButton(new TranslationTextComponent("msd.nosave"), getMonitorXsize() / 2 - 48 - 5, 106, n -> insMode(MusicSharingDeviceTileEntity.Screen.PLAYLIST));
        this.playlistDetailsSave = addSmartStringButton(new TranslationTextComponent("msd.save"), getMonitorXsize() / 2 + 5, 106, n -> {
            updatePlayList();
            savePlayListDetails();
            insMode(MusicSharingDeviceTileEntity.Screen.PLAYLIST);
        });

        String DetailsNameChangeField = "";
        if (this.playlistDetailsNameChangeField != null) {
            DetailsNameChangeField = this.playlistDetailsNameChangeField.getText();
        } else {
            if (isMonitor(MusicSharingDeviceTileEntity.Screen.PLAYLIST_DETAILS) && getCurrentSelectedPlayList() != null) {
                if (getTileEntity() instanceof MusicSharingDeviceTileEntity) {
                    DetailsNameChangeField = ((MusicSharingDeviceTileEntity) getTileEntity()).getPLDetalsName(getMinecraft().player);
                }
            }
        }

        this.playlistDetailsNameChangeField = this.addWidgetByIKSG(new TextFieldWidget(this.font, getMonitorStartX() + 50, getMonitorStartY() + 20, 137, 12, new StringTextComponent("test")));
        this.playlistDetailsNameChangeField.setEnableBackgroundDrawing(false);
        this.playlistDetailsNameChangeField.setMaxStringLength(30);
        this.playlistDetailsNameChangeField.setTextColor(-1);
        this.playlistDetailsNameChangeField.setDisabledTextColour(-1);
        this.playlistDetailsNameChangeField.setText(DetailsNameChangeField);
        this.playlistDetailsNameChangeField.setResponder(n -> {
            if (isMonitor(MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST) && image.getImageType() == PlayImage.ImageType.STRING) {
                setImage(PlayImage.ImageType.STRING, n);
            }
        });
        IKSGScreenUtil.setVisible(this.playlistDetailsNameChangeField, false);

        this.playlistDetailsAnyoneCheckbox = this.addWidgetByIKSG(new Checkbox(getMonitorStartX() + 47, getMonitorStartY() + 38, 15, 15, 215, 96, 256, 256, MSD_GUI_TEXTURES));
        IKSGScreenUtil.setVisible(this.playlistDetailsAnyoneCheckbox, false);


        if (!initFrist) {
            if (isMonitor(MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST, MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1)) {
                Path picPath = getPicturPath();
                if (picPath != null) {
                    DropAndDragFileLoadThread plt = new DropAndDragFileLoadThread(true, picPath);
                    plt.start();
                } else {
                    removePictuerPath();
                }
            } else {
                removePictuerPath();
            }

            if (image == null) {
                setImage(PlayImage.ImageType.STRING, "");
            }

        }
        this.initFrist = true;


    }

    @Override
    protected void drawGuiContainerBackgroundLayerByIKSG(MatrixStack matx, float partTick, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayerByIKSG(matx, partTick, mouseX, mouseY);
        IKSGRenderUtil.matrixPush(matx);
        IKSGRenderUtil.guiBindAndBlit(getCurrentScreen().getTexLocation(), matx, getMonitorStartX(), getMonitorStartY(), 0, 0, 199, 122, 199, 122);
        IKSGRenderUtil.matrixPop(matx);
        switch (getCurrentScreen()) {
            case NO_ANTENNA:
                drawNoAntenna(matx, partTick, mouseX, mouseY);
                break;
            case PLAYLIST:
                drawPlayList(matx, partTick, mouseX, mouseY);
                break;
            case CREATE_PLAYLIST:
                drawCreatePlaylist(matx, partTick, mouseX, mouseY);
                break;
            case ADD_PLAYLIST:
                drawAddPlaylist(matx, partTick, mouseX, mouseY);
                break;
            case JOIN_PLAYLIST:
                drawJoinPlaylist(matx, partTick, mouseX, mouseY);
                break;
            case ADD_PLAYMUSIC_1:
                drawAddPlayMusic1(matx, partTick, mouseX, mouseY);
                break;
            case ADD_PLAYMUSIC_2:
                drawAddPlayMusic2(matx, partTick, mouseX, mouseY);
                break;
            case YOUTUBE_SEARCH:
                drawAddPlayMusicYoutubeSelect(matx, partTick, mouseX, mouseY);
                break;
            case NOTEXIST:
                drawNotexist(matx, partTick, mouseX, mouseY);
                break;
            case PLAYLIST_DETAILS:
                drawPlaylistDetails(matx, partTick, mouseX, mouseY);
                break;
        }
    }


    @Override
    public void tickByIKSG() {
        super.tickByIKSG();

        if (getJonedAllPlayLists().isEmpty()) {
            if (isMonitor(MusicSharingDeviceTileEntity.Screen.PLAYLIST))
                insMode(MusicSharingDeviceTileEntity.Screen.NOTEXIST);
        } else {
            if (isMonitor(MusicSharingDeviceTileEntity.Screen.NOTEXIST))
                insMode(MusicSharingDeviceTileEntity.Screen.PLAYLIST);
        }

        if (getCurrentSelectedPlayList() == PlayList.ALL)
            addPlayMusicButton.setTextuer(244, 40, 18, 256, 256);
        else
            addPlayMusicButton.setTextuer(235, 40, 18, 256, 256);

        if (getTileEntity() instanceof MusicSharingDeviceTileEntity && ((MusicSharingDeviceTileEntity) getTileEntity()).getPAntenna().isEmpty()) {
            stopPlayMusic();
        }

        fieldTick();
        IKSGScreenUtil.setVisible(this.allbutton, isMonitor(MusicSharingDeviceTileEntity.Screen.PLAYLIST));
        IKSGScreenUtil.setVisible(this.addGuildButton, isMonitor(MusicSharingDeviceTileEntity.Screen.PLAYLIST));
        IKSGScreenUtil.setVisible(this.guildlistbar, isMonitor(MusicSharingDeviceTileEntity.Screen.PLAYLIST));
        IKSGScreenUtil.setVisible(this.playlistbar, isMonitor(MusicSharingDeviceTileEntity.Screen.PLAYLIST));
        IKSGScreenUtil.setVisible(this.guildButtons, isMonitor(MusicSharingDeviceTileEntity.Screen.PLAYLIST));
        IKSGScreenUtil.setVisible(this.playlistButtons, isMonitor(MusicSharingDeviceTileEntity.Screen.PLAYLIST));
        IKSGScreenUtil.setVisible(this.createGuildNameField, isMonitor(MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST));
        IKSGScreenUtil.setVisible(this.backGuid, isMonitor(MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST, MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1));
        IKSGScreenUtil.setVisible(this.createGuid, isMonitor(MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST));
        IKSGScreenUtil.setVisible(this.resetImage, isMonitor(MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST, MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1) && image != null && image.getImageType() != PlayImage.ImageType.STRING);
        IKSGScreenUtil.setVisible(this.openImage, isMonitor(MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST, MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1) && canOpenFileChooser);
        IKSGScreenUtil.setVisible(this.createAnyoneCheckbox, isMonitor(MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST));
        IKSGScreenUtil.setActive(this.createGuid, image != null && !createGuildNameField.getText().isEmpty());
        IKSGScreenUtil.setVisible(this.createJoinGuid, isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYLIST, MusicSharingDeviceTileEntity.Screen.NOTEXIST));
        IKSGScreenUtil.setVisible(this.addJoinGuid, isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYLIST, MusicSharingDeviceTileEntity.Screen.NOTEXIST));
        IKSGScreenUtil.setVisible(this.backJoinGuid, isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYLIST));
        IKSGScreenUtil.setVisible(this.joinplaylistbar, isMonitor(MusicSharingDeviceTileEntity.Screen.JOIN_PLAYLIST));
        IKSGScreenUtil.setVisible(this.JoinPlayListScrollButtons, isMonitor(MusicSharingDeviceTileEntity.Screen.JOIN_PLAYLIST));
        IKSGScreenUtil.setVisible(this.joinplaylistbackButton, isMonitor(MusicSharingDeviceTileEntity.Screen.JOIN_PLAYLIST));
        IKSGScreenUtil.setVisible(this.addPlayMusicButton, isMonitor(MusicSharingDeviceTileEntity.Screen.PLAYLIST));
        IKSGScreenUtil.setActive(this.addPlayMusicButton, getCurrentSelectedPlayList() != PlayList.ALL);
        IKSGScreenUtil.setVisible(this.addPlayMusicNameField, isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1));
        IKSGScreenUtil.setVisible(this.addPlayMusicSourceField, isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1));
        IKSGScreenUtil.setVisible(this.addPlayMusicSourceReferenceButton, isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSarchButton, isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1) && musicSourceClientReferencesType == MusicSourceClientReferencesType.YOUTUBE);
        IKSGScreenUtil.setVisible(this.addPlayMusicOpenFolder, isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1) && musicSourceClientReferencesType == MusicSourceClientReferencesType.LOCAL_FILE && canOpenFileChooser);
        IKSGScreenUtil.setVisible(this.nextAddPlayMusic, isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1));
        IKSGScreenUtil.setActive(this.nextAddPlayMusic, image != null && !addPlayMusicNameField.getText().isEmpty() && musicLoadResult == MusicLoadResult.AVAILABLE);
        IKSGScreenUtil.setVisible(this.addPlayMusicArtistField, isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_2));
        IKSGScreenUtil.setVisible(this.addPlayMusicAlbumField, isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_2));
        IKSGScreenUtil.setVisible(this.addPlayMusicYearField, isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_2));
        IKSGScreenUtil.setVisible(this.addPlayMusicGenreField, isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_2));
        IKSGScreenUtil.setVisible(this.addPlayMusic2BackButton, isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_2));
        IKSGScreenUtil.setVisible(this.addPlayMusic2CrateButton, isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_2));
        IKSGScreenUtil.setActive(this.addPlayMusic2CrateButton, !this.addPlayMusicSourceField.getText().isEmpty() && !this.addPlayMusicNameField.getText().isEmpty() && musicLoadResult == MusicLoadResult.AVAILABLE);
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSerchBackButton, isMonitor(MusicSharingDeviceTileEntity.Screen.YOUTUBE_SEARCH));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSearchField, isMonitor(MusicSharingDeviceTileEntity.Screen.YOUTUBE_SEARCH));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSearchlistbar, isMonitor(MusicSharingDeviceTileEntity.Screen.YOUTUBE_SEARCH));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSearchButton, isMonitor(MusicSharingDeviceTileEntity.Screen.YOUTUBE_SEARCH));
        IKSGScreenUtil.setVisible(this.addPlayMusicYoutubeSearchFileListButton, isMonitor(MusicSharingDeviceTileEntity.Screen.YOUTUBE_SEARCH));
        IKSGScreenUtil.setVisible(this.playlistDetails, isMonitor(MusicSharingDeviceTileEntity.Screen.PLAYLIST) && getCurrentSelectedPlayList() != PlayList.ALL);
        IKSGScreenUtil.setVisible(this.playlistDetailsBack, isMonitor(MusicSharingDeviceTileEntity.Screen.PLAYLIST_DETAILS));
        IKSGScreenUtil.setVisible(this.playlistDetailsSave, isMonitor(MusicSharingDeviceTileEntity.Screen.PLAYLIST_DETAILS));
        IKSGScreenUtil.setVisible(this.playlistDetailsNameChangeField, isMonitor(MusicSharingDeviceTileEntity.Screen.PLAYLIST_DETAILS));
        IKSGScreenUtil.setVisible(this.playlistDetailsAnyoneCheckbox, isMonitor(MusicSharingDeviceTileEntity.Screen.PLAYLIST_DETAILS));
    }

    private void fieldTick() {
        if (isMonitor(MusicSharingDeviceTileEntity.Screen.PLAYLIST_DETAILS)) {
            playlistDetailsNameChangeField.tick();
        }

        if (isMonitor(MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST)) {
            createGuildNameField.tick();
        }
        if (isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1)) {
            addPlayMusicNameField.tick();
            addPlayMusicSourceField.tick();
        }
        if (isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_2)) {
            addPlayMusicArtistField.tick();
            addPlayMusicAlbumField.tick();
            addPlayMusicYearField.tick();
            addPlayMusicGenreField.tick();
        }
        if (isMonitor(MusicSharingDeviceTileEntity.Screen.YOUTUBE_SEARCH)) {
            addPlayMusicYoutubeSearchField.tick();
        }
        if (!isMonitor(MusicSharingDeviceTileEntity.Screen.YOUTUBE_SEARCH)) {
            addPlayMusicYoutubeSearchField.setText("");
        }
        if (!isMonitor(MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST)) {
            createGuildNameField.setText("");
        }
        if (!isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1, MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_2, MusicSharingDeviceTileEntity.Screen.YOUTUBE_SEARCH)) {
            addPlayMusicNameField.setText("");
            addPlayMusicSourceField.setText("");
            addPlayMusicArtistField.setText("");
            addPlayMusicAlbumField.setText("");
            addPlayMusicYearField.setText("");
            addPlayMusicGenreField.setText("");
            if (!isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1)) {
                if (musicLoadResult != null)
                    musicLoadResult = null;
                if (sourceCheckThread != null && !sourceCheckThread.isStop())
                    sourceCheckThread.setStop(true);
            }
        }
    }

    private boolean isMonitor(MusicSharingDeviceTileEntity.Screen... mo) {
        return isSlectedMonitor(getCurrentScreen(), mo);
    }

    private boolean isSlectedMonitor(MusicSharingDeviceTileEntity.Screen mo, MusicSharingDeviceTileEntity.Screen... mos) {
        return Arrays.asList(mos).contains(mo);
    }

    @Override
    public ResourceLocation getBackGrandTextuer() {
        return MSD_GUI_TEXTURES;
    }

    public MusicSharingDeviceTileEntity.Screen getCurrentScreen() {
        return ((MusicSharingDeviceTileEntity) getTileEntity()).getScreen(IamMusicPlayer.proxy.getMinecraft().player);
    }

    public void insMode(MusicSharingDeviceTileEntity.Screen moniter) {

        CompoundNBT tag = new CompoundNBT();
        tag.putString("name", moniter.getName());

        this.instruction("Mode", tag);
        if (isSlectedMonitor(moniter, MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST, MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1)) {
            Path picPath = getPicturPath();
            if (picPath != null) {
                DropAndDragFileLoadThread plt = new DropAndDragFileLoadThread(true, picPath);
                plt.start();
            }
        }

        if (!isSlectedMonitor(moniter, MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST, MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1, MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_2, MusicSharingDeviceTileEntity.Screen.YOUTUBE_SEARCH)) {
            removePictuerPath();
            setImage(PlayImage.ImageType.STRING, "");
        }
        if (!isSlectedMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1, MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_2, MusicSharingDeviceTileEntity.Screen.YOUTUBE_SEARCH)) {
            //       this.uploadLocation = null;
            this.formattype = "";
        }
        updateAll();
        stopPlayMusic();
    }

    private void updatePlayListPlayers() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("uuid", getCurrentSelectedPlayList().getUUID());
        instruction("AllPlayListPlayersUpdate", tag);
    }

    private void updateCanJoinPlayList() {
        instruction("CanJoinPlayListUpdate", new CompoundNBT());
    }

    @Override
    public void updateAll() {
        super.updateAll();
        updateCanJoinPlayList();
        updatePlayListPlayers();
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
                updateAll();
            }

            @Override
            public long time(ClockTimer clockTimer) {
                return 3000;
            }
        });


    }

    @Override
    public void instructionReturn(String name, CompoundNBT tag) {
        super.instructionReturn(name, tag);
        if (name.equals("CanJoinPlayListUpdate")) {
            jonPlaylists.clear();
            for (String pltagst : tag.keySet()) {
                jonPlaylists.add(new PlayList(pltagst, tag.getCompound(pltagst)));
            }
        } else if (name.equals("AllPlayListPlayersUpdate")) {
            playListPlayers.clear();
            playListPlayers.addAll(IKSGNBTUtil.readStringList(tag.getCompound("list").getCompound("players")));
        }
    }

    @Override
    protected void insPower(boolean on) {
        super.insPower(on);
        stopPlayMusic();
    }

    protected void savePlayListDetails() {
        PlayList cul = getCurrentSelectedPlayList();
        PlayListGuildManeger.instance().changePlayListRequest(cul.getUUID(), playlistDetailsNameChangeField.getText(), PlayImage.EMPTY, picturImage, playlistDetailsAnyoneCheckbox.isCheck());
    }

    protected void drawPlaylistDetails(MatrixStack matrx, float partTick, int mouseX, int mouseY) {
        drawFontString(matrx, new TranslationTextComponent("msd.playlistdetails"), getMonitorStartX() + 2, getMonitorStartY() + 2);
        drawMiniFontString(matrx, new TranslationTextComponent("msd.image"), getMonitorStartX() + 6, getMonitorStartY() + 13);
        drawMiniFontString(matrx, new TranslationTextComponent("msd.name"), getMonitorStartX() + 47, getMonitorStartY() + 13);
        drawFontString(matrx, new TranslationTextComponent("msd.anyonecheck"), getMonitorStartX() + 47 + 17, getMonitorStartY() + 41);
        RenderUtil.drwPlayImage(matrx, getCurrentSelectedPlayList().getImage(), getMonitorStartX() + 7, getMonitorStartY() + 18, 34);

     /*   if (image != null) {
            RenderUtil.drwPlayImage(matrx, image, picturImage, getMonitorStartX() + 7, getMonitorStartY() + 27, 79);
        }
*/
    }

    protected void drawNotexist(MatrixStack matrx, float partTick, int mouseX, int mouseY) {
        drawFontString(matrx, new TranslationTextComponent("msd.addplaylist"), getMonitorStartX() + 2, getMonitorStartY() + 2);
        drawCenterFontString(matrx, new TranslationTextComponent("msd.noplaylist"), getMonitorStartX() + getMonitorXsize() / 2, getMonitorStartY() + getMonitorYsize() / 2 - 25);
        drawCenterFontString(matrx, new TranslationTextComponent("msd.addplaylistInfo"), getMonitorStartX() + getMonitorXsize() / 2, getMonitorStartY() + getMonitorYsize() / 2 - 25 + this.font.FONT_HEIGHT + 3);

    }

    protected void drawAddPlayMusicYoutubeSelect(MatrixStack matrx, float partTick, int mouseX, int mouseY) {
        drawFontString(matrx, new TranslationTextComponent("msd.youtubesearch"), getMonitorStartX() + 2, getMonitorStartY() + 2);
        IKSGRenderUtil.guiBindAndBlit(YOUTUBE_ICON, matrx, getMonitorStartX() + 2, getMonitorStartY() + 16, 0, 0, 27, 7, 27, 7);
        addPlayMusicYoutubeSearchField.render(matrx, mouseX, mouseY, partTick);
    }

    protected void drawAddPlayMusic2(MatrixStack matrx, float partTick, int mouseX, int mouseY) {
        drawFontString(matrx, new TranslationTextComponent("msd.addplaymusic"), getMonitorStartX() + 2, getMonitorStartY() + 2);
        drawFontString(matrx, new TranslationTextComponent("msd.artist"), getMonitorStartX() + 6, getMonitorStartY() + 17);
        drawFontString(matrx, new TranslationTextComponent("msd.album"), getMonitorStartX() + 107, getMonitorStartY() + 17);
        drawFontString(matrx, new TranslationTextComponent("msd.year"), getMonitorStartX() + 6, getMonitorStartY() + 43);
        drawFontString(matrx, new TranslationTextComponent("msd.genre"), getMonitorStartX() + 107, getMonitorStartY() + 43);
        //   drawFontString(matrx, new TranslationTextComponent("msd.uploadlocation"), getMonitorStartX() + 6, getMonitorStartY() + 69);

        //     if (uploadLocation != null)
        //         drawFontString(matrx, new TranslationTextComponent("uploadlocation." + uploadLocation.name().toLowerCase() + ".desc"), getMonitorStartX() + 6, getMonitorStartY() + 95);

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
        drawFontString(matrx, new StringTextComponent(getCurrentSelectedPlayList().getName()), getMonitorStartX() + 31, getMonitorStartY() + 2);

        if (!playListPlayers.isEmpty()) {
            int isze = 0;
            if (playListPlayers.size() > 2) {
                IFormattableTextComponent stc = new StringTextComponent("+").appendString(String.valueOf(playListPlayers.size() - 2));
                isze = font.getStringPropertyWidth(stc);
                drawFontString(matrx, stc, getMonitorStartX() + 155 - isze, getMonitorStartY() + 2);
            }

            if (playListPlayers.size() != 1) {
                IKSGRenderUtil.drawPlayerFase(matrx, "katyou", getMonitorStartX() + 155 - isze - 10, getMonitorStartY() + 2);
            }

            IKSGRenderUtil.drawPlayerFase(matrx, "MoriMori_0317_jp", getMonitorStartX() + 155 - isze - 20 + (playListPlayers.size() != 1 ? 0 : 10), getMonitorStartY() + 2);

        }
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

    private Path getPicturPath() {
        String pathst = ((MusicSharingDeviceTileEntity) getTileEntity()).getPlayerPath(getMinecraft().player);
        if (!pathst.isEmpty()) {
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
        if (isMonitor(MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST, MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1, MusicSharingDeviceTileEntity.Screen.YOUTUBE_SEARCH)) {
            if (dragFiles.size() == 1 && !pictuerLoading) {
                DropAndDragFileLoadThread lt = new DropAndDragFileLoadThread(false, dragFiles.get(0));
                lt.start();
            }
        }
    }

    @Override
    public void onCloseByIKSG() {
        super.onCloseByIKSG();

        if (isMonitor(MusicSharingDeviceTileEntity.Screen.PLAYLIST_DETAILS) && getCurrentSelectedPlayList() != null) {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("PLName", getCurrentSelectedPlayList().getName());
            instruction("PlaylistDetailsSet", tag);
        }
        stopPlayMusic();
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
                setImage(PlayImage.ImageType.IMGAE, UUID.randomUUID().toString());
                setPictuerPath(path);
            }
        } else {
            if (isMonitor(MusicSharingDeviceTileEntity.Screen.CREATE_PLAYLIST))
                setImage(PlayImage.ImageType.STRING, this.createGuildNameField.getText());
            else if (isMonitor(MusicSharingDeviceTileEntity.Screen.ADD_PLAYMUSIC_1))
                setImage(PlayImage.ImageType.STRING, this.addPlayMusicNameField.getText());
        }
    }

    public void setImage(PlayImage.ImageType type, String str) {
        if (type != PlayImage.ImageType.IMGAE) {
            picturImage = null;
        }
        image = new PlayImage(type, str);
    }

    public void removePictuerPath() {
        instruction("PathSet", new CompoundNBT());
    }

    public void setPictuerPath(Path path) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("path", path.toString());
        instruction("PathSet", tag);
    }

    public void setMusicLoadError(MusicLoadResult musicLoadError) {
        this.musicLoadResult = musicLoadError;
    }

    public void playYoutubeMusic(String videoID) {
        playMusic(MusicSourceClientReferencesType.YOUTUBE, videoID);
    }

    public void playMusic(MusicSourceClientReferencesType type, String url) {
        stopPlayMusic();
        musicPlayThread = new MusicPlayThread(type, url, 0, getCurrentScreen());
        musicPlayThread.start();
    }

    public void stopPlayMusic() {

        if (musicPlayThread != null)
            musicPlayThread.stopd();

        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer = null;
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

    private class DropAndDragFileLoadThread extends Thread {
        private final Path path;
        private final boolean pictuerOnry;

        public DropAndDragFileLoadThread(boolean pictueronry, Path path) {

            this.path = path;
            this.pictuerOnry = pictueronry;
        }

        public void run() {
            pictuerLoading = true;
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
                    if (isOpend())
                        setPicturImage(IKSGPictuerUtil.geByteImage(outbfi), path);
                } else {
                    nopicteur = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            pictuerLoading = false;

            if (pictuerOnry)
                return;

            if (nopicteur) {
                musicLoadResult = null;
                setMusicSourceClientReferencesType(MusicSourceClientReferencesType.LOCAL_FILE);
                addPlayMusicSourceField.setText(path.toString());
            }
        }
    }

    private class SourceCheckThread extends Thread {
        private final String source;
        private boolean stop;

        public SourceCheckThread(String source) {
            this.setName("Music Source Check");
            this.source = source;
        }

        public boolean isStop() {
            return stop;
        }

        public void setStop(boolean stop) {
            this.stop = stop;
        }

        public void run() {


            if (this.stop || source.isEmpty())
                return;

            musicLoading = true;
            musicLoadResult = null;

            if (getMusicSourceClientReferencesType() == MusicSourceClientReferencesType.LOCAL_FILE) {
                try {
                    Path path = Paths.get(source);
                    if (!path.toFile().exists()) {
                        if (!this.stop) {
                            musicLoadResult = MusicLoadResult.FILE_NOT_EXIST;
                            musicLoading = false;
                        }
                        return;
                    }

                    try {
                        MultimediaObject mo = FFmpegUtils.createMultimediaObject(path.toFile());
                        Encoder encoder = new Encoder();
                        if (Arrays.asList(encoder.getSupportedEncodingFormats()).contains(FFmpegUtils.getInfo(mo).getFormat())) {
                            MultimediaInfo info = FFmpegUtils.getInfo(mo);
                            if (info.getFormat().equals("mp3")) {
                                Mp3File mp3 = new Mp3File(path);
                                ID3v2 id3v2 = mp3.getId3v2Tag();
                                if (id3v2 != null) {
                                    if (id3v2.getTitle() != null) {
                                        if (!this.stop)
                                            addPlayMusicNameField.setText(id3v2.getTitle());
                                    } else {
                                        if (!this.stop)
                                            addPlayMusicNameField.setText(IKSGStringUtil.deleteExtension(path.toFile().getName()));
                                    }
                                    if (id3v2.getArtist() != null) {
                                        if (!this.stop)
                                            addPlayMusicArtistField.setText(id3v2.getArtist());
                                    } else {
                                        if (!this.stop)
                                            addPlayMusicArtistField.setText("");
                                    }
                                    if (id3v2.getAlbum() != null) {
                                        if (!this.stop)
                                            addPlayMusicAlbumField.setText(id3v2.getAlbum());
                                    } else {
                                        if (!this.stop)
                                            addPlayMusicAlbumField.setText("");
                                    }
                                    if (id3v2.getYear() != null) {
                                        if (!this.stop)
                                            addPlayMusicYearField.setText(id3v2.getYear());
                                    } else {
                                        if (!this.stop)
                                            addPlayMusicYearField.setText("");
                                    }
                                    if (id3v2.getGenreDescription() != null) {
                                        if (!this.stop)
                                            addPlayMusicGenreField.setText(id3v2.getGenreDescription());
                                    } else {
                                        if (!this.stop)
                                            addPlayMusicGenreField.setText("");
                                    }
                                    if (id3v2.getAlbumImage() != null) {
                                        String uuid = UUID.randomUUID().toString();
                                        IKSGFileLoadUtil.fileBytesWriter(id3v2.getAlbumImage(), PathUtils.getIMPTmpFolder().resolve(uuid));
                                        if (!this.stop) {
                                            DropAndDragFileLoadThread plt = new DropAndDragFileLoadThread(true, PathUtils.getIMPTmpFolder().resolve(uuid));
                                            plt.start();
                                        }
                                    }
                                } else {
                                    if (!this.stop)
                                        addPlayMusicNameField.setText(IKSGStringUtil.deleteExtension(path.toFile().getName()));
                                }
                            } else {
                                if (!this.stop)
                                    addPlayMusicNameField.setText(IKSGStringUtil.deleteExtension(path.toFile().getName()));
                            }
                            if (!this.stop)
                                formattype = info.getFormat();
                        } else {
                            if (!this.stop) {
                                musicLoadResult = MusicLoadResult.NO_SUPPORT_FORMAT;
                                musicLoading = false;
                            }
                            return;
                        }
                    } catch (Exception ex) {
                        if (!this.stop) {
                            musicLoadResult = MusicLoadResult.NO_SUPPORT_FORMAT;
                            musicLoading = false;
                        }
                        return;
                    }
                } catch (Exception ex) {
                    if (!this.stop) {
                        musicLoadResult = MusicLoadResult.FILE_NOT_EXIST;
                        musicLoading = false;
                    }
                    return;
                }
            } else if (getMusicSourceClientReferencesType() == MusicSourceClientReferencesType.URL) {
                try {
                    URL url = new URL(source);
                    MultimediaObject mo = FFmpegUtils.createMultimediaObject(url);
                    Encoder encoder = new Encoder();
                    MultimediaInfo info = FFmpegUtils.getInfo(mo);
                    if (Arrays.asList(encoder.getSupportedEncodingFormats()).contains(info.getFormat())) {
                        if (info.getDuration() == -1) {
                            if (!this.stop) {
                                musicLoadResult = MusicLoadResult.STREAM;
                                musicLoading = false;
                            }
                            return;
                        }
                        if (!this.stop) {
                            formattype = info.getFormat();
                        }
                    } else {
                        if (!this.stop) {
                            musicLoadResult = MusicLoadResult.NO_SUPPORT_FORMAT;
                            musicLoading = false;
                        }
                        return;
                    }
                } catch (Exception ex) {
                    if (!this.stop) {
                        musicLoadResult = MusicLoadResult.INVALID_URL;
                        musicLoading = false;
                    }
                    return;
                }
            } else if (getMusicSourceClientReferencesType() == MusicSourceClientReferencesType.YOUTUBE) {
                if (YoutubeUtils.isYoutubeURL(source)) {
                    addPlayMusicSourceField.setText(YoutubeUtils.getYoutubeIDFromURL(source));
                    formattype = "Youtube";
                }
            }

            if (!this.stop) {
                musicLoadResult = MusicLoadResult.AVAILABLE;
                musicLoading = false;
            }
        }

    }

    public static final Logger YOUTUBESEARCH_LOGGER = LogManager.getLogger(YoutubeSearchThread.class);

    private class YoutubeSearchThread extends Thread {
        private final String searchText;
        private boolean stop;


        public YoutubeSearchThread(String searchText) {
            this.setName("Youtube Search");
            this.searchText = searchText;
        }

        public void run() {
            if (this.stop || searchText.isEmpty())
                return;

            youtubeSearchLoading = true;
            youtubeResilts.clear();
            YOUTUBESEARCH_LOGGER.debug("Youtube Search: " + searchText);
            List<AudioTrack> list = YoutubeUtils.getVideoSearchResults(searchText);
            YOUTUBESEARCH_LOGGER.debug("Youtube Search Finished: " + searchText);
            if (!this.stop)
                youtubeResilts.addAll(list);
            youtubeSearchLoading = false;
        }
    }

    public class MusicPlayThread extends Thread {
        private final long startTime;
        private final MusicSourceClientReferencesType type;
        private final String src;
        private final MusicSharingDeviceTileEntity.Screen monitor;
        private boolean musicPlayLoading;
        private String musicPlayLodingSrc;
        private MusicSourceClientReferencesType musicPlayLodingType;
        private boolean stop;

        public MusicPlayThread(MusicSourceClientReferencesType type, String src, long time, MusicSharingDeviceTileEntity.Screen monitors) {
            this.setName("MSD Music Play");
            this.startTime = time;
            this.type = type;
            this.src = src;
            this.monitor = monitors;
        }

        public void run() {
            try {
                musicPlayLodingSrc = src;
                musicPlayLodingType = type;
                musicPlayLoading = true;
                if (!stop)
                    musicPlayer = type.getMusicPlayer(src);
                if (!stop && getCurrentScreen() == monitor && musicPlayer != null && isOpend()) {
                    musicPlayer.playAndReady(startTime);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                musicPlayLoading = false;
                musicPlayLodingSrc = null;
                musicPlayLodingType = null;
            }
        }

        public String getMusicPlayLodingSrc() {
            return musicPlayLodingSrc;
        }

        public MusicSourceClientReferencesType getMusicPlayLodingType() {
            return musicPlayLodingType;
        }

        public boolean isMusicPlayLoading() {
            return musicPlayLoading;
        }

        public void stopd() {
            this.stop = true;
        }
    }
}
