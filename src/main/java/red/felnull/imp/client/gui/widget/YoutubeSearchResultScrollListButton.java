package red.felnull.imp.client.gui.widget;

import com.google.api.services.youtube.model.SearchResult;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.ScrollListButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.List;

public class YoutubeSearchResultScrollListButton extends ScrollListButton {
    private List<SearchResult> youtubeResilts;

    public YoutubeSearchResultScrollListButton(int x, int y, int sizeX, int sizeY, int sizeOne, ScrollBarSlider scrollBar, List<SearchResult> youtubeResilts, IPressable pressed) {
        super(x, y, sizeX, sizeY, sizeOne, 0, scrollBar, null, pressed);
        this.youtubeResilts = youtubeResilts;
    }

    @Override
    public void renderOneList(MatrixStack matrix, int x, int y, int num, int upOver, int downOver) {
        IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x, y + upOver, 48, upOver, 187, 40 - downOver, 256, 256);
        SearchResult pl = youtubeResilts.get(num);

        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        if (upOver < 13 && downOver <= 37) {
            IKSGRenderUtil.drawHorizontalMovementString(matrix, fontrenderer, pl.getSnippet().getTitle(), "imp.msdfc." + pl.hashCode() + " file.hashCode()", 30, x + 3, y + 3, 181, 30, MusicSharingDeviceScreen.fontStyle);
        }

    }

    @Override
    protected int getCont() {
        if (youtubeResilts == null)
            return 0;

        return youtubeResilts.size();
    }
}