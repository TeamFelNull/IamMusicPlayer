package red.felnull.imp.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.gui.screen.IMPAbstractEquipmentScreen;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.musicplayer.PlayImage;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGTextureUtil;
import red.felnull.otyacraftengine.util.IKSGColorUtil;
import red.felnull.otyacraftengine.util.IKSGMath;
import red.felnull.otyacraftengine.util.IKSGStyles;

import java.util.Random;

public class RenderUtil {
    private static final ResourceLocation STRING_PLAYIMAGE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/string_playimage.png");
    private static final int maxColor = Integer.valueOf("ffffff", 16);

    public static void drwPlayImage(MatrixStack matrix, PlayImage image, int x, int y, int size) {
        drwPlayImage(matrix, image, x, y, size, 0, 0);
    }

    public static void drwPlayImage(MatrixStack matrix, PlayImage image, byte[] imageData, int x, int y, int size) {
        drwPlayImage(matrix, image, imageData, x, y, size, 0, 0);
    }


    public static void drwPlayImage(MatrixStack matrix, PlayImage image, int x, int y, int size, int upOver, int downOver) {
        PlayImage.ImageType ityepe = image.getImageType();
        switch (ityepe) {
            case IMGAE:
                drwPlayImageImage(matrix, image.getName(), null, x, y, size, upOver, downOver);
                break;
            case STRING:
                drwPlayImageString(matrix, image.getName(), x, y, size, upOver, downOver);
                break;
            case PLAYERFACE:
                drwPlayImagePlayerFace(matrix, image.getName(), x, y, size, upOver, downOver);
                break;
            case URLIMAGE:
                drwPlayImageURLImage(matrix, image.getName(), x, y, size, upOver, downOver);
                break;
        }
    }

    public static void drwPlayImage(MatrixStack matrix, PlayImage image, byte[] imageData, int x, int y, int size, int upOver, int downOver) {
        PlayImage.ImageType ityepe = image.getImageType();
        switch (ityepe) {
            case IMGAE:
                drwPlayImageImage(matrix, image.getName(), imageData, x, y, size, upOver, downOver);
                break;
            case STRING:
                drwPlayImageString(matrix, image.getName(), x, y, size, upOver, downOver);
                break;
            case PLAYERFACE:
                drwPlayImagePlayerFace(matrix, image.getName(), x, y, size, upOver, downOver);
                break;
            case URLIMAGE:
                drwPlayImageURLImage(matrix, image.getName(), x, y, size, upOver, downOver);
                break;
        }
    }

    private static void drwPlayImageString(MatrixStack matrix, String str, int x, int y, int size, int upOver, int downOver) {
        Random r = new Random(IKSGMath.convertStringToInteger(str));
        int[] backColor = IKSGColorUtil.convertRGBFromColorCode(r.nextInt(maxColor));
        RenderSystem.pushMatrix();
        RenderSystem.color4f((float) backColor[0] / 255f, (float) backColor[1] / 255f, (float) backColor[2] / 255f, 1.0F);
        IKSGRenderUtil.guiBindAndBlit(STRING_PLAYIMAGE, matrix, x, y + upOver, 0, upOver, size, size - downOver - upOver, size, size);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.popMatrix();

        Minecraft mc = IamMusicPlayer.proxy.getMinecraft();
        FontRenderer ft = mc.fontRenderer;

        IFormattableTextComponent comp = IKSGStyles.withStyle(new StringTextComponent(str),  IMPAbstractEquipmentScreen.smart_fontStyle);
        int stWidthSize = ft.getStringPropertyWidth(comp);
        float baritu = (float) (size - 3) / (float) stWidthSize;
        float yzure = (size - ft.FONT_HEIGHT * baritu) / 2;

        if (upOver < yzure + ft.FONT_HEIGHT * baritu && downOver < yzure + ft.FONT_HEIGHT * baritu) {
            IKSGRenderUtil.matrixPush(matrix);
            IKSGRenderUtil.matrixScalf(matrix, baritu);
            IKSGRenderUtil.drawCenterString(ft, matrix, comp, (int) ((float) (x + size / 2) / baritu), (int) ((y + yzure) / baritu), 0);
            IKSGRenderUtil.matrixPop(matrix);
        }
    }

    private static void drwPlayImagePlayerFace(MatrixStack matrix, String str, int x, int y, int size, int upOver, int downOver) {
        IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getPlayerSkinTexture(str), matrix, x, y + upOver, size, size + upOver, size, size - downOver - upOver, size * 8, size * 8);
        IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getPlayerSkinTexture(str), matrix, x, y + upOver, size * 5, size + upOver, size, size - downOver - upOver, size * 8, size * 8);
    }

    private static void drwPlayImageImage(MatrixStack matrix, String str, byte[] imageData, int x, int y, int size, int upOver, int downOver) {
        ResourceLocation location = imageData == null ? IKSGTextureUtil.getReceiveTexture(IMPWorldData.IMAGE, str) : IKSGTextureUtil.getPictureImageTexture(imageData);

        int w = IKSGTextureUtil.getWidth(location, 256);
        int h = IKSGTextureUtil.getHeight(location, 256);

        int xsize = (int) (size * ((float) w / 256f));
        int ysize = (int) (size * ((float) h / 256f));
        int xz = (size - xsize) / 2;
        int yz = (size - ysize) / 2;
        int upOverZure = upOver < yz ? 0 : upOver - yz;
        int downOverZure = downOver < yz ? 0 : downOver - yz;
        IKSGRenderUtil.guiBindAndBlit(location, matrix, x + xz, y + yz + upOverZure, 0, upOverZure, xsize, ysize - downOverZure - upOverZure, xsize, ysize);
    }

    private static void drwPlayImageURLImage(MatrixStack matrix, String url, int x, int y, int size, int upOver, int downOver) {
        ResourceLocation location = null;

        if (url == null)
            location = IKSGTextureUtil.TEXTUER_NOTFINED;
        else if (url.isEmpty())
            location = new ResourceLocation("otyacraftengine", "textures/gui/textuer_not_find.png");
        else
            location = IKSGTextureUtil.getPictureImageURLTexture(url);

        float w = IKSGTextureUtil.getWidth(location, 256);
        float h = IKSGTextureUtil.getHeight(location, 256);

        int xsize = 0;
        int ysize = 0;
        if (w == h) {
            xsize = size;
            ysize = size;
        } else if (w > h) {
            xsize = size;
            ysize = (int) ((float) size * (h / w));
        } else if (w < h) {
            xsize = (int) ((float) size * (w / h));
            ysize = size;
        }

        int xz = (size - xsize) / 2;
        int yz = (size - ysize) / 2;

        int upOverZure = upOver < yz ? 0 : upOver - yz;
        int downOverZure = downOver < yz ? 0 : downOver - yz;
        IKSGRenderUtil.guiBindAndBlit(location, matrix, x + xz, y + yz + upOverZure, 0, upOverZure, xsize, ysize - downOverZure - upOverZure, xsize, ysize);
    }
}
