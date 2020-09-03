package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.musicplayer.PlayList;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.ScrollListButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGTextureUtil;

import java.util.List;

public class JoinPlayListButton extends ScrollListButton {
    private final List<PlayList> playLists;

    public JoinPlayListButton(int x, int y, int sizeX, int sizeY, int sizeOne, ScrollBarSlider scrollBar, List<PlayList> playlists, IPressable pressed) {
        super(x, y, sizeX, sizeY, sizeOne, 0, scrollBar, null, pressed);
        this.playLists = playlists;
    }

    @Override
    public void renderOneList(MatrixStack matrix, int x, int y, int num, int upOver, int downOver) {
        PlayList pl = playLists.get(num);
        int size = 36;
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
        IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x, y + upOver, 48, upOver, 187, 40 - downOver, 256, 256);
        IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getReceiveTexture(IMPWorldData.PLAYLIST_IMAGE, pl.getImageUUID()), matrix, x + 2 + (36 - aw) / 2, y + 2 + (36 - ah) / 2, 0, 0, aw, ah, aw, ah);
    }

    @Override
    protected int getCont() {
        return this.playLists.size();
    }

}
