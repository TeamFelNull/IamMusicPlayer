package red.felnull.imp.client.gui.toasts;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class FFmpegErrorToast implements IToast {
    @Override
    public Visibility func_230444_a_(MatrixStack matrix, ToastGui toast, long time) {

        IKSGRenderUtil.matrixPush(matrix);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        IKSGRenderUtil.guiBindAndBlit(MusicUploadToast.IMP_TEXTURE_TOASTS, matrix, 0, 0, 0, 44, 160, 32, 256, 256);
        IKSGRenderUtil.matrixPop(matrix);

        return IToast.Visibility.SHOW;
    }

    @Override
    public Object getType() {
        return FFmpegErrorToast.class;
    }

    public static boolean isAlreadyExists() {
        ToastGui toastgui = Minecraft.getInstance().getToastGui();
        FFmpegErrorToast tos = toastgui.getToast(FFmpegErrorToast.class, FFmpegErrorToast.class);
        return tos != null;
    }
}
