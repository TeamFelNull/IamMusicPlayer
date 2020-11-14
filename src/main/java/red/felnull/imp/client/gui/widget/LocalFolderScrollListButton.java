package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.client.util.RenderUtil;
import red.felnull.imp.musicplayer.PlayImage;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.ScrollListButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.io.File;

public class LocalFolderScrollListButton extends ScrollListButton {
    private final MusicSharingDeviceScreen screen;

    public LocalFolderScrollListButton(int x, int y, int sizeX, int sizeY, int sizeOne, ScrollBarSlider scrollBar, MusicSharingDeviceScreen screen, IPressable pressed) {
        super(x, y, sizeX, sizeY, sizeOne, 0, scrollBar, null, pressed);
        this.screen = screen;
    }

    @Override
    public void renderOneList(MatrixStack matrix, int x, int y, int num, int upOver, int downOver) {
        IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x, y + upOver, 48, upOver, 187, 40 - downOver, 256, 256);
        File file = screen.searchFolders[num];
        int upzure = 2 < upOver ? upOver - 2 : 0;
        int downzure = 2 < downOver ? downOver - 2 : 0;
        PlayImage image = new PlayImage(PlayImage.ImageType.STRING, file.getName());


        RenderUtil.drwPlayImage(matrix, image, x + 2, y + 2, 36, upzure, downzure);


    }

    @Override
    protected int getCont() {
        File[] files = screen.searchFolders;
        if (files != null)
            return files.length;
        return 0;
    }
}