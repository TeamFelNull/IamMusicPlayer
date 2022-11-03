package dev.felnull.imp.music.tracker;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public record MusicTrackerEntry(ResourceLocation location, MusicTracker tracker) {
    public CompoundTag saveToTag() {
        return IMPMusicTrackers.saveToTag(this);
    }
}
