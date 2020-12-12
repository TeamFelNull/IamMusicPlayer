package red.felnull.imp.client.data;

import net.minecraft.nbt.CompoundNBT;
import red.felnull.otyacraftengine.data.INBTReadWriter;

public class MusicDownloader {
    private static MusicDownloader INSTANCE;

    public static void init() {
        INSTANCE = new MusicDownloader();
    }

    public static MusicDownloader instance() {
        return INSTANCE;
    }

}


