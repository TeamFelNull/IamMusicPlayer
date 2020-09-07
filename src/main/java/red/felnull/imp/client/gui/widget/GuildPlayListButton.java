package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.musicplayer.PlayList;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.ScrollListButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGTextureUtil;

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
        IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getReceiveTexture(IMPWorldData.PLAYLIST_IMAGE, pl.getImageUUID()), matrix, x, y + upOver, 0, upOver, this.getXSize(), this.onesize - downOver, this.getXSize(), this.onesize);
    }

    @Override
    protected int getCont() {
        return this.playLists.size();
    }
}
