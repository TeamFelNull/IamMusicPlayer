package dev.felnull.imp.client.music.tracker;

import dev.felnull.imp.music.ringer.MusicRingManager;
import dev.felnull.imp.music.MusicPlaybackInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class IMPMusicTrackers {
    private static final Map<ResourceLocation, Function<CompoundTag, IMusicTracker>> TRACKERS = new HashMap<>();

    public static void init() {
        registerTracker(MusicRingManager.FIXED_TRACKER, FixedMusicTracker::new);
        registerTracker(MusicRingManager.ENTITY_TRACKER, EntityMusicTracker::new);
        registerTracker(MusicRingManager.PLAYER_TRACKER, PlayerMusicTracker::new);
    }

    public static void registerTracker(ResourceLocation location, Function<CompoundTag, IMusicTracker> tracker) {
        TRACKERS.put(location, tracker);
    }

    public static IMusicTracker createTracker(MusicPlaybackInfo playbackInfo) {
        return createTracker(playbackInfo.getTracker(), playbackInfo.getTrackerTag());
    }

    public static IMusicTracker createTracker(ResourceLocation location, CompoundTag tag) {
        if (TRACKERS.containsKey(location))
            return TRACKERS.get(location).apply(tag);
        return null;
    }
}
