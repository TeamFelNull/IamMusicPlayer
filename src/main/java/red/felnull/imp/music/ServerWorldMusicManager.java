package red.felnull.imp.music;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import red.felnull.imp.music.resource.PlayMusic;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;

import java.util.*;
import java.util.stream.Collectors;

public class ServerWorldMusicManager {
    private static ServerWorldMusicManager INSTANCE;
    private Map<UUID, WorldMusicRinger> ringdMusics = new HashMap<>();

    public static void init() {
        INSTANCE = new ServerWorldMusicManager();
    }

    public static ServerWorldMusicManager instance() {
        return INSTANCE;
    }

    public void play(UUID uuid, ResourceLocation dimention, PlayMusic music, IWorldRingWhether whether) {
        WorldMusicRinger wrp = new WorldMusicRinger(uuid, dimention, music, whether);
        ringdMusics.put(uuid, wrp);
        wrp.play();
    }

    public boolean isPlaying(UUID uuid) {
        return ringdMusics.containsKey(uuid);
    }

    public void stop(UUID uuid) {

        if (ringdMusics.containsKey(uuid))
            ringdMusics.get(uuid).pause();

        ringdMusics.remove(uuid);
    }

    public void clear() {
        List<UUID> removeRingers = new ArrayList<>(ringdMusics.keySet());
        removeRingers.forEach(this::stop);
    }

    public void tick() {
        List<UUID> removeRingers = ringdMusics.entrySet().stream().filter(n -> n.getValue().tick()).map(Map.Entry::getKey).collect(Collectors.toList());
        removeRingers.forEach(this::stop);
    }

    public void loadingFinish(UUID uuid, ServerPlayerEntity player) {
        if (ringdMusics.containsKey(uuid))
            ringdMusics.get(uuid).musicLoadingFinish(UUID.fromString(IKSGPlayerUtil.getUUID(player)));
    }

}
