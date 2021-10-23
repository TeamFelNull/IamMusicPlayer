package dev.felnull.imp;

import dev.felnull.imp.item.IMPItems;
import dev.felnull.imp.net.IMPPackets;

public class IamMusicPlayer {
    public static final String MODID = "iammusicplayer";

    public static void init() {
        IMPPackets.init();
        IMPItems.init();

    }
}
