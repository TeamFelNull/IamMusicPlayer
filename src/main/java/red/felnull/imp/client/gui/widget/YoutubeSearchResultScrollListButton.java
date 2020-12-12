package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.client.music.IMusicPlayer;
import red.felnull.imp.client.music.YoutubeMusicPlayer;
import red.felnull.imp.client.util.RenderUtil;
import red.felnull.imp.client.util.YoutubeUtils;
import red.felnull.imp.musicplayer.PlayImage;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.ScrollListButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGTextureUtil;
import red.felnull.otyacraftengine.util.IKSGStyles;

public class YoutubeSearchResultScrollListButton extends ScrollListButton {
    private MusicSharingDeviceScreen screen;

    public YoutubeSearchResultScrollListButton(int x, int y, int sizeX, int sizeY, int sizeOne, ScrollBarSlider scrollBar, MusicSharingDeviceScreen screen, IPressable pressed) {
        super(x, y, sizeX, sizeY, sizeOne, 0, scrollBar, null, pressed);
        this.screen = screen;
    }

    @Override
    public void renderOneList(MatrixStack matrix, int x, int y, int num, int upOver, int downOver) {
        IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x, y + upOver, 48, upOver, 187, 40 - downOver, 256, 256);
        AudioTrack pl = screen.youtubeResilts.get(num);

        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        if (upOver < 13 && downOver <= 37) {
            IKSGRenderUtil.drawHorizontalMovementString(matrix, fontrenderer, pl.getInfo().title, "imp.msdfc." + pl.hashCode() + " file.hashCode()", 30, x + 41, y + 3, 133, 30, MusicSharingDeviceScreen.fontStyle);
        }

        int upzure = 1 < upOver ? upOver - 1 : 0;
        int downzure = 1 < downOver ? downOver - 1 : 0;
        RenderUtil.drwPlayImage(matrix, new PlayImage(PlayImage.ImageType.URLIMAGE, YoutubeUtils.getThumbnailURL(pl.getInfo().identifier)), x + 2, y + 2, 36, upzure, downzure);

        int fupzure = 29 < upOver ? upOver - 29 : 0;
        int fdownzure = 3 < downOver ? downOver - 3 : 0;

        if (screen.musicPlayLoading && screen.musicPlayLodingSrc != null && screen.musicPlayLodingSrc.equals(pl.getIdentifier())) {
            IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getLoadingIconTextuer(), matrix, x + 41, y + 29 + fupzure, 0, fupzure, 8, 8 - fupzure - fdownzure, 8, 8);
            if (upOver < 38 && downOver <= 12) {
                IKSGRenderUtil.drawString(fontrenderer, matrix, IKSGStyles.withStyle(new TranslationTextComponent("msd.musicloading"), MusicSharingDeviceScreen.fontStyle), x + 50, y + 29, 0);
            }
        } else {
            IMusicPlayer player = screen.musicPlayer;
            if (player instanceof YoutubeMusicPlayer && player.isPlaying()) {
                String playingVideoID = (String) player.getMusicSource();
                if (playingVideoID.equals(pl.getIdentifier())) {
                    IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x + 41, y + 29 + fupzure, 14, 30 + fupzure, 8, 8 - fupzure - fdownzure, 256, 256);
                    IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x + 50, y + 29 + fupzure, 113, 80 + fupzure, 134, 8 - fupzure - fdownzure, 256, 256);
                    int gagePar = (int) (134f * ((float) player.getCureentElapsed() / (float) player.getDuration()));
                    IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x + 50, y + 29 + fupzure, 113, 88 + fupzure, gagePar, 8 - fupzure - fdownzure, 256, 256);
                }
            }
        }
    }

    @Override
    protected int getCont() {
        if (screen.youtubeResilts == null)
            return 0;
        return screen.youtubeResilts.size();
    }
}