package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.client.util.RenderUtil;
import red.felnull.imp.musicplayer.PlayList;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.ScrollListButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.List;

public class GuildPlayListButton extends ScrollListButton {
    private final List<PlayList> playLists;

    public GuildPlayListButton(int x, int y, int sizeX, int sizeY, int sizeOne, ScrollBarSlider scrollBar, List<PlayList> playlists, IPressable pressed) {
        super(x, y, sizeX, sizeY, sizeOne, 0, scrollBar, null, pressed);
        this.playLists = playlists;
    }

    @Override
    public void renderOneList(MatrixStack matrix, int x, int y, int num, int upOver, int downOver) {
        PlayList pl = playLists.get(num);
        IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x, y + upOver, 235, upOver, 18, 18 - downOver, 256, 256);
/*
        int size = 16;
        float w = pl.getImageWidth();
        float h = pl.getImageHeight();
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
        int tatezure = 1 + (size - ah) / 2;
        int upzure = tatezure < upOver ? upOver - tatezure : 0;
        int downzure = tatezure < downOver ? downOver - tatezure : 0;

        IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getReceiveTexture(IMPWorldData.IMAGE, pl.getImageUUID()), matrix, x + 1 + (size - aw) / 2, y + tatezure + upzure, 0, upzure, aw, ah - upzure - downzure, aw, ah);
*/

        int upzure = 1 < upOver ? upOver - 1 : 0;
        int downzure = 1 < downOver ? downOver - 1 : 0;
        RenderUtil.drwPlayImage(matrix, pl.getImage(), x + 1, y + 1, 16, upzure, downzure);
    }

    @Override
    protected int getCont() {
        return this.playLists.size();
    }
}
