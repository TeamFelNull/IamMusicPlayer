package red.felnull.imp.client.blockentity;

import red.felnull.imp.blockentity.IMPBlockEntitys;
import red.felnull.otyacraftengine.client.util.IKSGClientUtil;

public class IMPBERenderers {
    public static void init() {
        IKSGClientUtil.registerBlockEntityRenderer(IMPBlockEntitys.MUSIC_SHARING_DEVICE, MusicSharingDeviceRenderer::new);
    }
}
