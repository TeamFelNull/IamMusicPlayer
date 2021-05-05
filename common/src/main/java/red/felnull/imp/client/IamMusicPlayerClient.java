package red.felnull.imp.client;

import red.felnull.imp.api.client.IMPClientRegistry;
import red.felnull.imp.client.renderer.blockentity.IMPBERenderers;
import red.felnull.imp.client.renderer.blockentity.gui.IMPScreenFactoryRegister;
import red.felnull.imp.client.renderer.item.IMPItemRenderers;

public class IamMusicPlayerClient {
    public static void clientInit() {
        IMPClientRegistry.init();
        IMPBERenderers.init();
        IMPItemRenderers.init();
        IMPScreenFactoryRegister.init();
    }
}
