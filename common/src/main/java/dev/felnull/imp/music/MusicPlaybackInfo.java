package dev.felnull.imp.music;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class MusicPlaybackInfo {
    private final ResourceLocation tracker;
    private final CompoundTag trackerTag;

    public MusicPlaybackInfo(ResourceLocation tracker, CompoundTag trackerTag) {
        this.tracker = tracker;
        this.trackerTag = trackerTag;
    }

    public CompoundTag getTrackerTag() {
        return trackerTag;
    }

    public ResourceLocation getTracker() {
        return tracker;
    }

    public float getVolume() {
        return 0;
    }

    public float getRange() {
        return 0;
    }
}
