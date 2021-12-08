package dev.felnull.imp.data;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.otyacraftengine.data.OEBaseSaveData;
import dev.felnull.otyacraftengine.data.WorldDataManager;
import net.minecraft.resources.ResourceLocation;

public class IMPWorldData {
    public static void init() {
        register("music_data", new MusicSaveData());
    }

    private static void register(String name, OEBaseSaveData data) {
        WorldDataManager.getInstance().register(new ResourceLocation(IamMusicPlayer.MODID, name), data);
    }
}
