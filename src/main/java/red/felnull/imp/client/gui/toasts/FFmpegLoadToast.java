package red.felnull.imp.client.gui.toasts;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.ffmpeg.FFmpegDownloader;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.util.IKSGStyles;

public class FFmpegLoadToast implements IToast {
    private static final ResourceLocation FFMPEGICON_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/ffmpeg.png");
    private boolean completed;
    protected long compTime;

    @Override
    public Visibility func_230444_a_(MatrixStack matrix, ToastGui toast, long time) {

        FFmpegDownloader downloader = FFmpegDownloader.getInstance();

        Minecraft mc = toast.getMinecraft();
        FontRenderer fr = mc.fontRenderer;
        IKSGRenderUtil.matrixPush(matrix);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        IKSGRenderUtil.guiBindAndBlit(MusicUploadToast.IMP_TEXTURE_TOASTS, matrix, 0, 0, 0, 44, 160, 32, 256, 256);
        IKSGRenderUtil.matrixPop(matrix);

        IKSGRenderUtil.guiBindAndBlit(FFMPEGICON_TEXTURE, matrix, 8, 8, 0, 0, 16, 16, 16, 16);


        if (downloader.getState() == FFmpegDownloader.FFmpegDwonloadState.DOWNLOAD) {
            IKSGRenderUtil.guiBindAndBlit(MusicUploadToast.IMP_TEXTURE_TOASTS, matrix, 29, 19, 0, 32, 125, 7, 256, 256);
            IKSGRenderUtil.guiBindAndBlit(MusicUploadToast.IMP_TEXTURE_TOASTS, matrix, 30, 20, 0, 39, (int) (123 * downloader.getProgress()), 5, 256, 256);
            IKSGRenderUtil.matrixPush(matrix);
            IFormattableTextComponent tc = IKSGStyles.withStyle(new StringTextComponent((int) Math.ceil(downloader.getProgress() * 100) + "%"), MusicSharingDeviceScreen.fontStyle);
            IKSGRenderUtil.matrixScalf(matrix, 0.5f);
            IKSGRenderUtil.drawCenterString(fr, matrix, tc, (int) (92.5f / 0.5f), (int) (20.5f / 0.5f), 0);
            IKSGRenderUtil.matrixPop(matrix);

            IKSGRenderUtil.drawString(fr, matrix, IKSGStyles.withStyle(downloader.getState().getLocalized(), MusicSharingDeviceScreen.fontStyle), 29, 8, 0);
        } else {
            IKSGRenderUtil.drawString(fr, matrix, IKSGStyles.withStyle(new TranslationTextComponent("ffmpegdlstate.completion"), MusicSharingDeviceScreen.fontStyle), 29, 16 - fr.FONT_HEIGHT / 2, 0);
        }

        if (!downloader.isDwonloading()) {
            if (!completed) {
                compTime = time;
                completed = true;
            }
            return time - compTime < 5000L ? IToast.Visibility.SHOW : IToast.Visibility.HIDE;
        }

        return IToast.Visibility.SHOW;
    }

    @Override
    public Object getType() {
        return FFmpegLoadToast.class;
    }

    public static boolean isAlreadyExists() {
        ToastGui toastgui = Minecraft.getInstance().getToastGui();
        FFmpegLoadToast tos = toastgui.getToast(FFmpegLoadToast.class, FFmpegLoadToast.class);
        return tos != null;
    }
}
