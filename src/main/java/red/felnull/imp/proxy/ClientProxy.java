package red.felnull.imp.proxy;

import net.minecraft.client.Minecraft;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.gui.IMPScrennContainerRegister;
import red.felnull.imp.client.renderer.tileentity.IMPTileEntityRenderers;

public class ClientProxy extends CommonProxy {
    public static void clientInit() {
        IMPTileEntityRenderers.registerTileEntityRenderer();
        IMPScrennContainerRegister.registerFactories();
    }

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
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
