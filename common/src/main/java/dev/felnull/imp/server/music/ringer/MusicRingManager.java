package dev.felnull.imp.server.music.ringer;

import dev.felnull.imp.networking.IMPPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MusicRingManager {
    private static final MusicRingManager INSTANCE = new MusicRingManager();
    private final Map<ServerLevel, MusicRing> musicRings = new HashMap<>();

    public static MusicRingManager getInstance() {
        return INSTANCE;
    }

    public void tick(ServerLevel level) {
        getMusicRing(level).tick();
    }

    public Map<ServerLevel, MusicRing> getMusicRingers() {
        return musicRings;
    }

    public void restartRinger(ServerLevel level, UUID uuid) {
        getMusicRing(level).restart(uuid);
    }

    public void addRinger(ServerLevel level, IMusicRinger ringer) {
        getMusicRing(level).addRinger(ringer);
    }

    public boolean isWaitRinger(UUID uuid, ServerLevel level) {
        return getMusicRing(level).isWaitRinger(uuid);
    }

    public void onUpdate(ServerPlayer player, UUID uuid, UUID waitUUID, IMPPackets.MusicRingResponseStateType state) {
        getMusicRing(player.getLevel()).onUpdate(player, uuid, waitUUID, state);
    }

    public void addReadyPlayer(ServerPlayer player, UUID uuid, UUID waitUUID, boolean result, boolean retry, long elapsed) {
        getMusicRing(player.getLevel()).addReadyPlayer(player, uuid, waitUUID, result, retry, elapsed);
    }

    @NotNull
    public MusicRing getMusicRing(ServerLevel level) {
        return musicRings.computeIfAbsent(level, MusicRing::new);
    }

    public boolean hasRinger(UUID uuid) {
        for (ServerLevel serverLevel : musicRings.keySet()) {
            if (hasRinger(serverLevel, uuid))
                return true;
        }
        return false;
    }

    public boolean hasRinger(ServerLevel level, UUID uuid) {
        return getMusicRing(level).hasRinger(uuid);
    }

    public IMusicRinger getRinger(UUID uuid) {
        for (Map.Entry<ServerLevel, MusicRing> entry : musicRings.entrySet()) {
            var mr = entry.getValue();
            var r = mr.getRingers().get(uuid);
            if (r != null)
                return r;
        }
        return null;
    }

    @Nullable
    public ServerLevel getLevel(MusicRing ring) {
        for (Map.Entry<ServerLevel, MusicRing> entry : musicRings.entrySet()) {
            if (entry.getValue() == ring)
                return entry.getKey();
        }
        return null;
    }

    public void pause() {
        musicRings.forEach((n, m) -> m.pause());
    }

    public void resume() {
        musicRings.forEach((n, m) -> m.resume());
    }

    public void clear() {
        musicRings.forEach((n, m) -> m.depose());
        musicRings.clear();
    }
}
