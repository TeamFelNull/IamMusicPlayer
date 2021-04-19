package red.felnull.imp;

import red.felnull.imp.block.IMPBlocks;
import red.felnull.imp.blockentity.IMPBlockEntitys;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.item.IMPItems;

public class IamMusicPlayer {
    public static final String MODID = "iammusicplayer";

    public static void init() {
        IMPItems.init();
        IMPBlocks.init();
        IMPBlockEntitys.init();
        IMPWorldData.init();
    }
}


