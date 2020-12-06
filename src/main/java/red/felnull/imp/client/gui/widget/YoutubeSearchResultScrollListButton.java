package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.client.util.RenderUtil;
import red.felnull.imp.client.util.YoutubeUtils;
import red.felnull.imp.musicplayer.PlayImage;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.ScrollListButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

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

        int upzure = 1 < upOver ? upOver - 1 : 0;
        int downzure = 1 < downOver ? downOver - 1 : 0;
        RenderUtil.drwPlayImage(matrix, new PlayImage(PlayImage.ImageType.URLIMAGE, YoutubeUtils.getThumbnailURL(pl.getInfo().identifier)), x + 2, y + 2, 36, upzure, downzure);

        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        if (upOver < 13 && downOver <= 37) {
            IKSGRenderUtil.drawHorizontalMovementString(matrix, fontrenderer, pl.getInfo().title, "imp.msdfc." + pl.hashCode() + " file.hashCode()", 30, x + 53, y + 3, 131, 30, MusicSharingDeviceScreen.fontStyle);
        }
        int fupzure = 27 < upOver ? upOver - 27 : 0;
        int fdownzure = 5 < downOver ? downOver - 5 : 0;
        //  String chanelImageURL= YoutubeData.getYoutube().channels().list("").
        //pl.getSnippet().getChannelId()
    }

    @Override
    protected int getCont() {
        if (screen.youtubeResilts == null)
            return 0;
        return screen.youtubeResilts.size();
    }
}