package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.client.util.RenderUtil;
import red.felnull.imp.musicplayer.PlayList;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.ScrollListButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.List;

public class GuildPlayListScrollButton extends ScrollListButton {
    private final List<PlayList> playLists;
    private final MusicSharingDeviceScreen screen;

    public GuildPlayListScrollButton(int x, int y, int sizeX, int sizeY, int sizeOne, ScrollBarSlider scrollBar, List<PlayList> playlists, IPressable pressed, MusicSharingDeviceScreen screen) {
        super(x, y, sizeX, sizeY, sizeOne, 0, scrollBar, null, pressed);
        this.playLists = playlists;
        this.screen = screen;
    }

    @Override
    public void renderOneList(MatrixStack matrix, int x, int y, int num, int upOver, int downOver) {
        PlayList pl = playLists.get(num);
        IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x, y + upOver, 235, (pl.equals(screen.getCurrentPlayList()) ? 18 : 0) + upOver, 18, 18 - downOver, 256, 256);
        int upzure = 1 < upOver ? upOver - 1 : 0;
        int downzure = 1 < downOver ? downOver - 1 : 0;
        RenderUtil.drwPlayImage(matrix, pl.getImage(), x + 1, y + 1, 16, upzure, downzure);
    }

    @Override
    protected int getCont() {
        return this.playLists.size();
    }
}
