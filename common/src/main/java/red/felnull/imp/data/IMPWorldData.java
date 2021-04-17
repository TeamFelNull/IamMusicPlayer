package red.felnull.imp.data;

import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.data.IkisugiSaveData;
import red.felnull.otyacraftengine.data.WorldDataManager;

public class IMPWorldData {
    public static void init() {
        register("musicdata", new MusicSaveData());
    }

    public static void register(String name, IkisugiSaveData data) {
        WorldDataManager.getInstance().register(new ResourceLocation(IamMusicPlayer.MODID, name), data);
    }
}
