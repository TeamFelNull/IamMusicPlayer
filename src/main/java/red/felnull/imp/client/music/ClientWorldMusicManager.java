package red.felnull.imp.client.music;

import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientWorldMusicManager {
    private static final Minecraft mc = Minecraft.getInstance();
    private static ClientWorldMusicManager INSTANCE;

    private Map<UUID, MusicRinger> mplayers = new HashMap<>();
    private Map<UUID, MusicRinger> waitmplayers = new HashMap<>();

    private boolean allStop;

    public static void init() {
        INSTANCE = new ClientWorldMusicManager();
    }

    public static ClientWorldMusicManager instance() {
        return INSTANCE;
    }

    public void loop() {

        if (!waitmplayers.isEmpty()) {
            mplayers.putAll(waitmplayers);
            waitmplayers.clear();
        }

        if (allStop) {
            allStop = false;
            if (!mplayers.isEmpty()) {
                //   mplayers.forEach((n, m) -> m.stop());
                mplayers.clear();
            }
        }

        if (mc.player != null) {
            //   mplayers.values().stream().filter(n -> !n.isPlaying()).forEach(n -> {

        }

    }

    public void addMusicPlayer(UUID uuid, MusicRinger ringer) {
        if (!waitmplayers.containsKey(uuid)) {
            waitmplayers.put(uuid, ringer);
        }else {

            waitmplayers.put(uuid, ringer);
        }
    }

    public void stopAllMusicPlayer() {
        allStop = true;
    }
}
