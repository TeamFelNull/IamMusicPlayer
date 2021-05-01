package red.felnull.imp;

import red.felnull.imp.block.IMPBlocks;
import red.felnull.imp.blockentity.IMPBlockEntitys;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.inventory.IMPMenus;
import red.felnull.imp.item.IMPItems;
import red.felnull.imp.music.MusicRegistry;
import red.felnull.imp.packet.IMPPackets;

public class IamMusicPlayer {
    public static final String MODID = "iammusicplayer";

    public static void init() {
        IMPPackets.init();
        IMPItems.init();
        IMPBlocks.init();
        IMPBlockEntitys.init();
        MusicRegistry.init();
        IMPWorldData.init();
        IMPMenus.init();
    }
}


