package dev.felnull.imp.server.music.ringer;

import dev.felnull.imp.IamMusicPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MusicRingManager {
    private static final MusicRingManager INSTANCE = new MusicRingManager();
    public static final ResourceLocation FIXED_TRACKER = new ResourceLocation(IamMusicPlayer.MODID, "fixed");
    public static final ResourceLocation ENTITY_TRACKER = new ResourceLocation(IamMusicPlayer.MODID, "entity");
    public static final ResourceLocation PLAYER_TRACKER = new ResourceLocation(IamMusicPlayer.MODID, "player");
    private final Map<ServerLevel, MusicRing> MUSIC_RINGERS = new HashMap<>();

    public static MusicRingManager getInstance() {
        return INSTANCE;
    }

    public void tick(ServerLevel level) {
        var ringer = MUSIC_RINGERS.get(level);
        if (ringer == null) {
            ringer = new MusicRing(level);
            MUSIC_RINGERS.put(level, ringer);
        }
        ringer.tick(level);
    }

    public Map<ServerLevel, MusicRing> getMusicRingers() {
        return MUSIC_RINGERS;
    }

    public void restartRinger(ServerLevel level, UUID uuid) {
        var ring = MUSIC_RINGERS.get(level);
        if (ring != null)
            ring.restart(uuid);
    }

    public void addRinger(ServerLevel level, IMusicRinger ringer) {
        var ring = MUSIC_RINGERS.get(level);
        if (ring != null)
            ring.addRinger(ringer);
    }

    public boolean isWaitRinger(UUID uuid, ServerLevel level) {
        var ring = MUSIC_RINGERS.get(level);
        if (ring != null)
            return ring.isWaitRinger(uuid);
        return false;
    }

    public void onUpdate(ServerPlayer player, UUID uuid, UUID waitUUID, int state) {
        var ring = MUSIC_RINGERS.get(player.getLevel());
        if (ring != null)
            ring.onUpdate(player, uuid, waitUUID, state);
    }

    public void addReadyPlayer(ServerPlayer player, UUID uuid, UUID waitUUID, boolean result, boolean retry, long elapsed) {
        var ring = MUSIC_RINGERS.get(player.getLevel());
        if (ring != null)
            ring.addReadyPlayer(player, uuid, waitUUID, result, retry, elapsed);
    }

    public boolean isExistRinger(UUID uuid) {
        for (ServerLevel serverLevel : MUSIC_RINGERS.keySet()) {
            if (isExistRinger(serverLevel, uuid))
                return true;
        }
        return false;
    }

    public boolean isExistRinger(ServerLevel level, UUID uuid) {
        var ring = MUSIC_RINGERS.get(level);
        if (ring != null)
            return ring.getRingers().containsKey(uuid);
        return false;
    }

    public IMusicRinger getRinger(UUID uuid) {
        for (ServerLevel serverLevel : MUSIC_RINGERS.keySet()) {
            var mr = MUSIC_RINGERS.get(serverLevel);
            if (mr != null) {
                var r = mr.getRingers().get(uuid);
                if (r != null)
                    return r;
            }
        }
        return null;
    }

    @Nullable
    public ServerLevel getLevel(MusicRing ring) {
        for (Map.Entry<ServerLevel, MusicRing> entry : MUSIC_RINGERS.entrySet()) {
            if (entry.getValue() == ring)
                return entry.getKey();
        }
        return null;
    }

    public void pause() {
        MUSIC_RINGERS.forEach((n, m) -> m.pause());
    }

    public void resume() {
        MUSIC_RINGERS.forEach((n, m) -> m.resume());
    }

    public void clear() {
        MUSIC_RINGERS.forEach((n, m) -> m.depose());
        MUSIC_RINGERS.clear();
    }

    public static CompoundTag createFixedTracker(Vec3 vec3) {
        var tag = new CompoundTag();
        tag.putDouble("x", vec3.x);
        tag.putDouble("y", vec3.y);
        tag.putDouble("z", vec3.z);
        return tag;
    }

    public static CompoundTag createEntityTracker(Entity entity) {
        var tag = new CompoundTag();
        var p = entity.position();
        tag.putDouble("x", p.x);
        tag.putDouble("y", p.y);
        tag.putDouble("z", p.z);
        tag.putInt("id", entity.getId());
        return tag;
    }

    public static CompoundTag createPlayerTracker(Player player) {
        var tag = new CompoundTag();
        var p = player.position();
        tag.putDouble("x", p.x);
        tag.putDouble("y", p.y);
        tag.putDouble("z", p.z);
        tag.putUUID("id", player.getGameProfile().getId());
        return tag;
    }
}
