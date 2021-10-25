package dev.felnull.imp.music;

import dev.felnull.otyacraftengine.data.ITAGSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class MusicPlaybackInfo implements ITAGSerializable {
    private ResourceLocation tracker;
    private CompoundTag trackerTag;
    private float volume;
    private float range;

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

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("TrackerID", tracker.toString());
        tag.put("TrackerTag", tag);
        tag.putFloat("Volume", volume);
        tag.putFloat("Range", range);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.tracker = new ResourceLocation(tag.getString("TrackerID"));
        this.trackerTag = tag.getCompound("TrackerTag");
        this.volume = tag.getFloat("Volume");
        this.range = tag.getFloat("Range");
    }
}
