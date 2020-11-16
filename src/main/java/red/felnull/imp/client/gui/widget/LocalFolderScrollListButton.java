package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
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
        IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x, y + upOver, 53, 110 + upOver, 187, 15 - downOver, 256, 256);
        File file = screen.searchFolders[num];
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        if (upOver < 13 && downOver <= 37) {
            IKSGRenderUtil.drawHorizontalMovementString(matrix, fontrenderer, file.getName(), "imp.msdfc.name." + file.hashCode(), 30, x + 3, y + 3, 181, 30, MusicSharingDeviceScreen.fontStyle);
        }

    }

    @Override
    protected int getCont() {
        File[] files = screen.searchFolders;
        if (files != null)
            return files.length;
        return 0;
    }
}