package red.felnull.imp.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import red.felnull.imp.IamMusicPlayer;
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
    private static int maxColor = Integer.valueOf("ffffff", 16);

    public static void drwPlayImage(MatrixStack matrix, PlayImage image, int x, int y, int size) {
        PlayImage.ImageType ityepe = image.getImageType();
        switch (ityepe) {
            case IMGAE:
                ResourceLocation location = IKSGTextureUtil.getReceiveTexture(IMPWorldData.PLAYLIST_IMAGE, image.getName());
                int xsize = (int) (size * ((float) IKSGTextureUtil.getWidth(location, 256) / 256f));
                int ysize = (int) (size * ((float) IKSGTextureUtil.getHeight(location, 256) / 256f));
                int xz = (size - xsize) / 2;
                int yz = (size - ysize) / 2;
                IKSGRenderUtil.guiBindAndBlit(location, matrix, x + xz, y + yz, 0, 0, xsize, ysize, xsize, ysize);
                break;
            case STRING:
                Random r = new Random(IKSGMath.convertStringToInteger(image.getName()));
                int strColor = r.nextInt(maxColor);
                int[] backColor = IKSGColorUtil.convertRGBFromColorCode(r.nextInt(maxColor));
                RenderSystem.pushMatrix();
                RenderSystem.color4f((float) backColor[0] / 255f, (float) backColor[1] / 255f, (float) backColor[2] / 255f, 1.0F);
                IKSGRenderUtil.guiBindAndBlit(STRING_PLAYIMAGE, matrix, x, y, 0, 0, size, size, size, size);
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.popMatrix();
                FontRenderer ft = IamMusicPlayer.proxy.getMinecraft().fontRenderer;
                IFormattableTextComponent stc = IKSGStyles.withStyle(new StringTextComponent(image.getName()), MusicSharingDeviceScreen.fontStyle);
                float brit = (float) (size - 4) / (float) ft.func_238414_a_(stc);
                IKSGRenderUtil.matrixPush(matrix);
                IKSGRenderUtil.matrixScalf(matrix, brit);
                IKSGRenderUtil.drawCenterString(ft, matrix, stc, (int) ((x + size / 2) / brit), (int) ((y + size / 2) / brit) - (int) (2.5f * brit), 0);
                IKSGRenderUtil.matrixPop(matrix);
                break;
            case PLAYERFACE:
                IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getPlayerSkinTexture(image.getName()), matrix, x, y, size, size, size, size, size * 8, size * 8);
                IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getPlayerSkinTexture(image.getName()), matrix, x * 5, y, size, size, size, size, size * 8, size * 8);
                break;
        }
    }

    public static void drwPlayImage(MatrixStack matrix, PlayImage image, byte[] imageData, int x, int y, int size) {
        PlayImage.ImageType ityepe = image.getImageType();
        switch (ityepe) {
            case IMGAE:
                ResourceLocation location = IKSGTextureUtil.getPictureImageTexture(imageData);
                int xsize = (int) (size * ((float) IKSGTextureUtil.getWidth(location, 256) / 256f));
                int ysize = (int) (size * ((float) IKSGTextureUtil.getHeight(location, 256) / 256f));
                int xz = (size - xsize) / 2;
                int yz = (size - ysize) / 2;
                IKSGRenderUtil.guiBindAndBlit(location, matrix, x + xz, y + yz, 0, 0, xsize, ysize, xsize, ysize);
                break;
            case STRING:
                Random r = new Random(IKSGMath.convertStringToInteger(image.getName()));
                int strColor = r.nextInt(maxColor);
                int[] backColor = IKSGColorUtil.convertRGBFromColorCode(r.nextInt(maxColor));
                RenderSystem.pushMatrix();
                RenderSystem.color4f((float) backColor[0] / 255f, (float) backColor[1] / 255f, (float) backColor[2] / 255f, 1.0F);
                IKSGRenderUtil.guiBindAndBlit(STRING_PLAYIMAGE, matrix, x, y, 0, 0, size, size, size, size);
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.popMatrix();
                FontRenderer ft = IamMusicPlayer.proxy.getMinecraft().fontRenderer;
                IFormattableTextComponent stc = IKSGStyles.withStyle(new StringTextComponent(image.getName()), MusicSharingDeviceScreen.fontStyle);
                float brit = (float) (size - 4) / (float) ft.func_238414_a_(stc);
                IKSGRenderUtil.matrixPush(matrix);
                IKSGRenderUtil.matrixScalf(matrix, brit);
                IKSGRenderUtil.drawCenterString(ft, matrix, stc, (int) ((x + size / 2) / brit), (int) ((y + size / 2) / brit) - (int) (2.5f * brit), 0);
                IKSGRenderUtil.matrixPop(matrix);
                break;
            case PLAYERFACE:
                IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getPlayerSkinTexture(image.getName()), matrix, x, y, size, size, size, size, size * 8, size * 8);
                IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getPlayerSkinTexture(image.getName()), matrix, x * 5, y, size, size, size, size, size * 8, size * 8);
                break;
        }
    }
}
