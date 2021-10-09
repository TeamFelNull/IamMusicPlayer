package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.client.gui.screen.CassetteDeckScreen;
import red.felnull.imp.client.gui.screen.IMusicPlayListScreen;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.client.util.RenderUtil;
import red.felnull.imp.music.resource.PlayLocation;
import red.felnull.imp.music.resource.PlayMusic;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.ScrollListButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGTextureUtil;

import java.util.ArrayList;
import java.util.List;

public class PlayMusicScrollButton extends ScrollListButton {
    private final IMusicPlayListScreen playListScreen;
    private final boolean mini;
    private final MusicSharingDeviceScreen msds;

    public PlayMusicScrollButton(int x, int y, int sizeX, int sizeY, ScrollBarSlider scrollBar, IPressable pressed, IMusicPlayListScreen sc, MusicSharingDeviceScreen msds, boolean mini) {
        super(x, y, sizeX, sizeY, mini ? 14 : 40, 0, scrollBar, null, pressed);
        this.msds = msds;
        this.playListScreen = sc;
        this.mini = mini;
    }

    @Override
    public void renderOneList(MatrixStack matrix, int x, int y, int num, int upOver, int downOver) {
        int i = mini ? 14 : 40;

        IKSGRenderUtil.guiBindAndBlit(mini ? CassetteDeckScreen.CD_GUI_TEXTURES : MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x, y + upOver, mini ? 0 : 53, (mini ? 227 : 40) + upOver, mini ? 167 : 158, i - downOver, 256, 256);

        PlayMusic pm = playListScreen.getCurrentPLPlayMusic().get(num);
        int upzure = 2 < upOver ? upOver - 2 : 0;
        int downzure = 2 < downOver ? downOver - 2 : 0;
        RenderUtil.drwPlayImage(matrix, pm.getImage(), x + 2, y + 2, mini ? 10 : 36, upzure, downzure);

        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        if (upOver < (mini ? 12 : 13) && downOver <= (mini ? 12 : 37)) {
            IKSGRenderUtil.drawHorizontalMovementString(matrix, fontrenderer, pm.getName(), "imp.pmsbpm.name." + pm.getUUID(), 30, x + i, y + 3, 115 + (mini ? 40 : 0), 30);
        }

        if (!mini) {
            if (upOver < 25 && downOver <= 25) {
                List<ITextComponent> descs = new ArrayList<>();

                if (!pm.getArtist().isEmpty())
                    descs.add(new TranslationTextComponent("playmusic.desc.artist", pm.getArtist()));
                if (!pm.getAlbum().isEmpty())
                    descs.add(new TranslationTextComponent("playmusic.desc.album", pm.getAlbum()));
                if (!pm.getGenre().isEmpty())
                    descs.add(new TranslationTextComponent("playmusic.desc.genre", pm.getGenre()));
                if (!pm.getYear().isEmpty())
                    descs.add(new TranslationTextComponent("playmusic.desc.year", pm.getYear()));
                if (!pm.getTimeStamp().isEmpty())
                    descs.add(new TranslationTextComponent("playmusic.desc.timestamp", pm.getTimeStamp()));

                TranslationTextComponent musicdesc = new TranslationTextComponent("playmusic.desc.all." + descs.size(), descs.toArray());

                IKSGRenderUtil.drawHorizontalMovementString(matrix, fontrenderer, musicdesc.getString(), "imp.msdpm.desc." + pm.getUUID(), 30, x + 40, y + 16, 115, 30);
            }
            if (msds != null) {
                int fupzure = 29 < upOver ? upOver - 29 : 0;
                int fdownzure = 3 < downOver ? downOver - 3 : 0;
                PlayLocation pl = pm.getMusicLocation();
                if (msds.musicPlayThread != null && msds.musicPlayThread.isMusicPlayLoading() && msds.musicPlayThread.getMusicPlayLodingSrc() != null && msds.musicPlayThread.getMusicPlayLodingSrc().equals(pl.getIdOrURL()) && msds.musicPlayThread.getMusicPlayLodingType().getLocationType() == pl.getLocationType()) {
                    IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getLoadingIconTextuer(), matrix, x + 41, y + 29 + fupzure, 0, fupzure, 8, 8 - fupzure - fdownzure, 8, 8);
                    if (upOver < 38 && downOver <= 12)
                        IKSGRenderUtil.drawString(fontrenderer, matrix, new TranslationTextComponent("msd.musicloading"), x + 50, y + 29, 0);
                } else {
                    IMusicPlayer player = msds.musicPlayer;
                    if (player != null && player.isPlaying() && player.getMusicSource().equals(pl.getIdOrURL())) {
                        IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x + 41, y + 29 + fupzure, 14, 30 + fupzure, 8, 8 - fupzure - fdownzure, 256, 256);
                        IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x + 50, y + 29 + fupzure, 113, 96 + fupzure, 104, 8 - fupzure - fdownzure, 256, 256);
                        int gagePar = (int) (104f * ((float) player.getCureentElapsed() / (float) player.getDuration()));
                        IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x + 50, y + 29 + fupzure, 113, 104 + fupzure, gagePar, 8 - fupzure - fdownzure, 256, 256);
                    } else {
                        if (upOver < 38 && downOver <= 12)
                            IKSGRenderUtil.drawString(fontrenderer, matrix, new StringTextComponent(pm.getCreatePlayerName()), x + 50, y + 29, 0);
                        IKSGRenderUtil.matrixPush(matrix);
                        ResourceLocation plskin = IKSGTextureUtil.getPlayerSkinTexture(pm.getCreatePlayerName());
                        IKSGRenderUtil.guiBindAndBlit(plskin, matrix, x + 40, y + 29 + fupzure, 8, 8 + fupzure, 8, 8 - fupzure - fdownzure, 64, 64);
                        IKSGRenderUtil.guiBindAndBlit(plskin, matrix, x + 40, y + 29 + fupzure, 40, 8 + fupzure, 8, 8 - fupzure - fdownzure, 64, 64);
                        IKSGRenderUtil.matrixPop(matrix);
                    }
                }
            }
        }
    }


    @Override
    protected int getCont() {
        return playListScreen.getCurrentPLPlayMusic().size();
    }

}
