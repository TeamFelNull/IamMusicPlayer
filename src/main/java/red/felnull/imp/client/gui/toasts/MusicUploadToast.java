package red.felnull.imp.client.gui.toasts;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.data.MusicUploadData;
import red.felnull.imp.client.data.MusicUploader;
import red.felnull.imp.client.util.RenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class MusicUploadToast implements IToast {
    private static final ResourceLocation IMP_TEXTURE_TOASTS = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/toasts.png");
    private final String uuid;
    private boolean completedOrError;
    protected long coeTime;

    public MusicUploadToast(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public Visibility func_230444_a_(MatrixStack matrix, ToastGui toast, long time) {

        if (!MusicUploader.instance().isUploaded(uuid))
            return IToast.Visibility.HIDE;

        MusicUploadData sd = MusicUploader.instance().getStateData(uuid);

        Minecraft mc = toast.getMinecraft();
        FontRenderer fr = mc.fontRenderer;
        IKSGRenderUtil.guiBindAndBlit(IMP_TEXTURE_TOASTS, matrix, 0, 0, 0, 0, 160, 32, 256, 256);
        IKSGRenderUtil.drawString(fr, matrix, new StringTextComponent(sd.getState().name()), 35, 3, 0);

        RenderUtil.drwPlayImage(matrix, sd.getImage(), 6, 6, 20, 20);

        if (sd.getState().isProgressble())
            IKSGRenderUtil.drawString(fr, matrix, new StringTextComponent(Math.ceil(sd.getProgress() * 100) + "%"), 35, 15, 0);


        if (sd.getState() == MusicUploadData.UploadState.COMPLETION || sd.getState() == MusicUploadData.UploadState.ERROR) {
            if (!completedOrError) {
                coeTime = time;
                completedOrError = true;
            }
            return time - coeTime < 5000L ? IToast.Visibility.SHOW : IToast.Visibility.HIDE;
        }

        return IToast.Visibility.SHOW;
    }

    public Object getType() {
        return uuid;
    }

    public static boolean isAlreadyExists(ToastGui toast, String uuid) {
        MusicUploadToast tos = toast.getToast(MusicUploadToast.class, uuid);
        return tos != null;
    }

    public static void add(String uuid) {
        ToastGui toastgui = Minecraft.getInstance().getToastGui();
        if (!MusicUploadToast.isAlreadyExists(toastgui, uuid)) {
            toastgui.add(new MusicUploadToast(uuid));
        }
    }


}
