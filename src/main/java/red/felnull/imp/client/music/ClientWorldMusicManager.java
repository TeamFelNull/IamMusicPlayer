package red.felnull.imp.client.music;

import net.minecraft.client.Minecraft;

import java.util.*;

public class ClientWorldMusicManager {
    private static final Minecraft mc = Minecraft.getInstance();
    private static ClientWorldMusicManager INSTANCE;

    private Map<UUID, MusicRinger> mplayers = new HashMap<>();

    private boolean allStop;

    public static void init() {
        INSTANCE = new ClientWorldMusicManager();
    }

    public static ClientWorldMusicManager instance() {
        return INSTANCE;
    }

    public void addMusicPlayer(UUID uuid, MusicRinger ringer) {
        stopMusicPlayer(uuid);
        mplayers.put(uuid, ringer);
    }

    public void stopMusicPlayer(UUID uuid) {
        if (mplayers.containsKey(uuid)) {
            mplayers.get(uuid).playStop();
            mplayers.remove(uuid);
        }
    }

    public void stopAllMusicPlayer() {
        allStop = true;
    }

    public void loop() {
        if (mc.player != null) {
            if (!mplayers.isEmpty()) {
                stopAllMusicPlayer();
            }
        }
        if (allStop) {
            allStop = false;
            if (!mplayers.isEmpty()) {
                List<UUID> stopedUUID = new ArrayList<>(new ArrayList<>(mplayers.keySet()));
                stopedUUID.forEach(this::stopMusicPlayer);
            }
        }
    }


}
