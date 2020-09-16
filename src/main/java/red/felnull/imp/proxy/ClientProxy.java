package red.felnull.imp.proxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import red.felnull.imp.client.data.MusicUploader;
import red.felnull.imp.client.gui.IMPScrennContainerRegister;
import red.felnull.imp.client.handler.MusicUploadHandler;
import red.felnull.imp.client.renderer.tileentity.IMPTileEntityRenderers;

public class ClientProxy extends CommonProxy {
    public static void clientInit() {
        IMPTileEntityRenderers.registerTileEntityRenderer();
        IMPScrennContainerRegister.registerFactories();
    }

    @Override
    public void preInit() {
        super.preInit();
        MusicUploader.init();
    }

    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(MusicUploadHandler.class);
    }

    @Override
    public void posInit() {
        super.posInit();
    }

    @Override
    public Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

}
