package red.felnull.imp.client;

import red.felnull.imp.client.blockentity.IMPBERenderers;
import red.felnull.imp.client.item.IMPItemRenderers;

public class IamMusicPlayerClient {
    public static void clientInit() {
        IMPBERenderers.init();
        IMPItemRenderers.init();
    }
}
