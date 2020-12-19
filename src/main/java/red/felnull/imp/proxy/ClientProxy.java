package red.felnull.imp.proxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import red.felnull.imp.client.data.MusicDownloader;
import red.felnull.imp.client.data.MusicUploader;
import red.felnull.imp.client.data.YoutubeData;
import red.felnull.imp.client.gui.IMPScrennContainerRegister;
import red.felnull.imp.client.gui.toasts.FFmpegLoadToast;
import red.felnull.imp.client.handler.MusicUploadHandler;
import red.felnull.imp.client.handler.RenderHandler;
import red.felnull.imp.client.handler.WorldMusicClientHandler;
import red.felnull.imp.client.renderer.tileentity.IMPTileEntityRenderers;
import red.felnull.imp.ffmpeg.FFmpegDownloader;

public class ClientProxy extends CommonProxy {
    public static void clientInit() {
        IMPTileEntityRenderers.registerTileEntityRenderer();
        IMPScrennContainerRegister.registerFactories();
        YoutubeData.init();
    }

    @Override
    public void preInit() {
        super.preInit();
        MusicUploader.init();
        MusicDownloader.init();
    }

    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(RenderHandler.class);
        MinecraftForge.EVENT_BUS.register(MusicUploadHandler.class);
        MinecraftForge.EVENT_BUS.register(WorldMusicClientHandler.class);
    }

    @Override
    public void posInit() {
        super.posInit();
    }

    @Override
    public Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    @Override
    public void addFFmpegLoadToast() {
        if (FFmpegDownloader.getInstance().isDwonloading() && !FFmpegLoadToast.isAlreadyExists()) {
            Minecraft.getInstance().getToastGui().add(new FFmpegLoadToast());
        }
    }
}
