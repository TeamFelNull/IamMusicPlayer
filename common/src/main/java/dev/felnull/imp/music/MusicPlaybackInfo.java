package dev.felnull.imp.music;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class MusicPlaybackInfo {
    private final ResourceLocation tracker;
    private final CompoundTag trackerTag;
    private final float volume;
    private final float range;

    public MusicPlaybackInfo(ResourceLocation tracker, CompoundTag trackerTag, float volume, float range) {
        this.tracker = tracker;
        this.trackerTag = trackerTag;
        this.volume = volume;
        this.range = range;
    }

    public CompoundTag getTrackerTag() {
        return trackerTag;
    }

    public ResourceLocation getTracker() {
        return tracker;
    }

    public float getVolume() {
        return volume;
    }

    public float getRange() {
        return range;
    }
}
