package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.client.music.IMusicPlayer;
import red.felnull.imp.client.util.RenderUtil;
import red.felnull.imp.musicplayer.PlayLocation;
import red.felnull.imp.musicplayer.PlayMusic;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.ScrollListButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGTextureUtil;
import red.felnull.otyacraftengine.util.IKSGStyles;

import java.util.ArrayList;
import java.util.List;

public class PlayMusicScrollButton extends ScrollListButton {
    private final List<PlayMusic> playMusic;
    private final MusicSharingDeviceScreen screen;

    public PlayMusicScrollButton(int x, int y, int sizeX, int sizeY, int sizeOne, ScrollBarSlider scrollBar, List<PlayMusic> playMusic, IPressable pressed, MusicSharingDeviceScreen screen) {
        super(x, y, sizeX, sizeY, sizeOne, 0, scrollBar, null, pressed);
        this.screen = screen;
        this.playMusic = playMusic;
    }

    @Override
    public void renderOneList(MatrixStack matrix, int x, int y, int num, int upOver, int downOver) {
        IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x, y + upOver, 53, 40 + upOver, 158, 40 - downOver, 256, 256);
        PlayMusic pm = playMusic.get(num);
        int upzure = 2 < upOver ? upOver - 2 : 0;
        int downzure = 2 < downOver ? downOver - 2 : 0;
        RenderUtil.drwPlayImage(matrix, pm.getImage(), x + 2, y + 2, 36, upzure, downzure);
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        if (upOver < 13 && downOver <= 37) {
            IKSGRenderUtil.drawHorizontalMovementString(matrix, fontrenderer, pm.getName(), "imp.msdpm.name." + pm.getUUID(), 30, x + 40, y + 3, 115, 30, MusicSharingDeviceScreen.fontStyle);
        }
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

            IKSGRenderUtil.drawHorizontalMovementString(matrix, fontrenderer, musicdesc.getString(), "imp.msdpm.desc." + pm.getUUID(), 30, x + 40, y + 16, 115, 30, MusicSharingDeviceScreen.fontStyle);
        }

        int fupzure = 29 < upOver ? upOver - 29 : 0;
        int fdownzure = 3 < downOver ? downOver - 3 : 0;

        PlayLocation pl = pm.getMusicLocation();

        if (screen.musicPlayThread != null && screen.musicPlayThread.isMusicPlayLoading() && screen.musicPlayThread.getMusicPlayLodingSrc() != null && screen.musicPlayThread.getMusicPlayLodingSrc().equals(pl.getIdOrURL()) && screen.musicPlayThread.getMusicPlayLodingType().getLocationType() == pl.getLocationType()) {
            IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getLoadingIconTextuer(), matrix, x + 41, y + 29 + fupzure, 0, fupzure, 8, 8 - fupzure - fdownzure, 8, 8);
            if (upOver < 38 && downOver <= 12)
                IKSGRenderUtil.drawString(fontrenderer, matrix, IKSGStyles.withStyle(new TranslationTextComponent("msd.musicloading"), MusicSharingDeviceScreen.fontStyle), x + 50, y + 29, 0);
        } else {
            IMusicPlayer player = screen.musicPlayer;
            if (player != null && player.isPlaying() && player.getMusicSource().equals(pl.getIdOrURL())) {
                IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x + 41, y + 29 + fupzure, 14, 30 + fupzure, 8, 8 - fupzure - fdownzure, 256, 256);
                IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x + 50, y + 29 + fupzure, 113, 96 + fupzure, 104, 8 - fupzure - fdownzure, 256, 256);
                int gagePar = (int) (104f * ((float) player.getCureentElapsed() / (float) player.getDuration()));
                IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x + 50, y + 29 + fupzure, 113, 104 + fupzure, gagePar, 8 - fupzure - fdownzure, 256, 256);
            } else {
                if (upOver < 38 && downOver <= 12)
                    IKSGRenderUtil.drawString(fontrenderer, matrix, IKSGStyles.withStyle(new StringTextComponent(pm.getCreatePlayerName()), MusicSharingDeviceScreen.fontStyle), x + 50, y + 29, 0);
                IKSGRenderUtil.matrixPush(matrix);
                ResourceLocation plskin = IKSGTextureUtil.getPlayerSkinTexture(pm.getCreatePlayerName());
                IKSGRenderUtil.guiBindAndBlit(plskin, matrix, x + 40, y + 29 + fupzure, 8, 8 + fupzure, 8, 8 - fupzure - fdownzure, 64, 64);
                IKSGRenderUtil.guiBindAndBlit(plskin, matrix, x + 40, y + 29 + fupzure, 40, 8 + fupzure, 8, 8 - fupzure - fdownzure, 64, 64);
                IKSGRenderUtil.matrixPop(matrix);
            }
        }
    }


    @Override
    protected int getCont() {
        return this.playMusic.size();
    }

}
