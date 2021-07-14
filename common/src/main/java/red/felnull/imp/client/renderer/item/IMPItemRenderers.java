package red.felnull.imp.client.renderer.item;

import red.felnull.imp.block.IMPBlocks;
import red.felnull.imp.item.IMPItems;
import red.felnull.otyacraftengine.client.util.IKSGClientUtil;

public class IMPItemRenderers {
    public static void init() {
        IKSGClientUtil.registerItemRenderer(IMPBlocks.MUSIC_SHARING_DEVICE, new MusicSharingDeviceItemRenderer());
        CassetteTapeItemRenderer cr = new CassetteTapeItemRenderer();
        IKSGClientUtil.registerItemRenderer(IMPItems.CASSETTE_TAPE, cr);
        IKSGClientUtil.registerItemRenderer(IMPItems.CASSETTE_TAPE_GLASS, cr);
    }
}
