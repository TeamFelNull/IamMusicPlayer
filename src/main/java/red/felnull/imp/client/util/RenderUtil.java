package red.felnull.imp.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import red.felnull.imp.musicplayer.PlayImage;
import red.felnull.otyacraftengine.client.gui.widget.IkisugiWidget;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGTextureUtil;

public class RenderUtil {
    public static void drwPlayImage(MatrixStack matrix, PlayImage image, int x, int y, int sizeX, int sizeY) {
        IKSGRenderUtil.guiBindAndBlit(IkisugiWidget.OE_WIDGET, matrix, x, y, 0, 0, sizeX, sizeY, sizeX, sizeY);
        PlayImage.ImageType ityepe = image.getImageType();
        switch (ityepe) {
            case IMGAE:
                break;
            case STRING:
                break;
            case PLAYERFACE:
                IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getPlayerSkinTexture(image.getName()), matrix, x, y, 0, 0, sizeX, sizeY, sizeX, sizeY);
                break;
        }

    }
}
