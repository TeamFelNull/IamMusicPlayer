package red.felnull.imp.music;

import net.minecraftforge.event.world.WorldEvent;
import red.felnull.otyacraftengine.util.IKSGServerUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerWorldMusicManager {
    private static ServerWorldMusicManager INSTANCE;
    private Map<UUID, String> ringdMusics = new HashMap<>();

    public static void init() {
        INSTANCE = new ServerWorldMusicManager();
    }

    public static ServerWorldMusicManager instance() {
        return INSTANCE;
    }

    public static void play(){
    }
}
