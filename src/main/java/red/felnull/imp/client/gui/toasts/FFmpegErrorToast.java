package red.felnull.imp.client.gui.toasts;

/*
public class FFmpegErrorToast implements IToast {
    private final IMPFFmpegException exception;

    public FFmpegErrorToast(IMPFFmpegException ex) {
        this.exception = ex;
    }

    @Override
    public Visibility func_230444_a_(MatrixStack matrix, ToastGui toast, long time) {

        Minecraft mc = toast.getMinecraft();
        FontRenderer fr = mc.fontRenderer;

        IKSGRenderUtil.matrixPush(matrix);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        IKSGRenderUtil.guiBindAndBlit(MusicUploadToast.IMP_TEXTURE_TOASTS, matrix, 0, 0, 0, 76, 160, 32, 256, 256);
        IKSGRenderUtil.matrixPop(matrix);

        IKSGRenderUtil.guiBindAndBlit(FFmpegLoadToast.FFMPEGICON_TEXTURE, matrix, 8, 8, 0, 0, 16, 16, 16, 16);

        IKSGRenderUtil.drawString(fr, matrix, IKSGStyles.withStyle(new TranslationTextComponent("ffmpegtoast.error"), IMPAbstractEquipmentScreen.smart_fontStyle), 29, 4 + (24 / 6) - fr.FONT_HEIGHT / 2, 0);
        IKSGRenderUtil.drawString(fr, matrix, IKSGStyles.withStyle(new StringTextComponent(exception.getMessage()), IMPAbstractEquipmentScreen.smart_fontStyle), 29, 4 + (24 / 3) + (24 / 6) - fr.FONT_HEIGHT / 2, 0);
        IKSGRenderUtil.drawString(fr, matrix, IKSGStyles.withStyle(new TranslationTextComponent("ffmpegtoast.reLoading"), IMPAbstractEquipmentScreen.smart_fontStyle), 29, 4 + (24 / 3) * 2 + (24 / 6) - fr.FONT_HEIGHT / 2, 0);

        return time < 5000L ? IToast.Visibility.SHOW : IToast.Visibility.HIDE;
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
*/