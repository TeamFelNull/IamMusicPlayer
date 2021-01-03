package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import red.felnull.imp.client.gui.screen.IMPAbstractEquipmentScreen;
import red.felnull.imp.client.gui.screen.IMusicPlayListScreen;
import red.felnull.imp.client.util.RenderUtil;
import red.felnull.imp.music.resource.PlayList;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.ScrollListButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class PlayListScrollButton extends ScrollListButton {
    private final boolean mini;
    private final IMusicPlayListScreen currentPlayListScreen;

    public PlayListScrollButton(int x, int y, int sizeY, ScrollBarSlider scrollBar, IPressable pressed, IMusicPlayListScreen sc, boolean mini) {
        super(x, y, mini ? 10 : 18, sizeY, mini ? 10 : 18, 0, scrollBar, null, pressed);
        this.mini = mini;
        this.currentPlayListScreen = sc;
    }

    @Override
    public void renderOneList(MatrixStack matrix, int x, int y, int num, int upOver, int downOver) {
        PlayList pl = currentPlayListScreen.getJonedAllPlayLists().get(num);
        int i = mini ? 10 : 18;
        IKSGRenderUtil.guiBindAndBlit(IMPAbstractEquipmentScreen.EQUIPMENT_WIDGETS_TEXTURES, matrix, x, y + upOver, mini ? 46 : 18, 40 + (pl.equals(currentPlayListScreen.getCurrentSelectedPlayList()) ? i : 0) + upOver, i, i - downOver, 256, 256);
        int upzure = 1 < upOver ? upOver - 1 : 0;
        int downzure = 1 < downOver ? downOver - 1 : 0;
        RenderUtil.drwPlayImage(matrix, pl.getImage(), x + 1, y + 1, mini ? 8 : 16, upzure, downzure);
    }

    @Override
    protected int getCont() {
        return currentPlayListScreen.getJonedAllPlayLists().size();
    }

}
