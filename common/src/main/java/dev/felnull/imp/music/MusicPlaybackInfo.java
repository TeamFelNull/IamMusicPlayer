package dev.felnull.imp.music;

import dev.felnull.otyacraftengine.data.ITAGSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class MusicPlaybackInfo implements ITAGSerializable {
    public static final MusicPlaybackInfo EMPTY = new MusicPlaybackInfo(new ResourceLocation("empty"), new CompoundTag(), 0, 0);
    private ResourceLocation tracker;
    private CompoundTag trackerTag;
    private float volume;
    private float range;

    public MusicPlaybackInfo() {

    }

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
        tag.put("TrackerTag", trackerTag);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicPlaybackInfo that = (MusicPlaybackInfo) o;
        return Float.compare(that.volume, volume) == 0 && Float.compare(that.range, range) == 0 && Objects.equals(tracker, that.tracker) && Objects.equals(trackerTag, that.trackerTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tracker, trackerTag, volume, range);
    }

    @Override
    public String toString() {
        return "MusicPlaybackInfo{" +
                "tracker=" + tracker +
                ", trackerTag=" + trackerTag +
                ", volume=" + volume +
                ", range=" + range +
                '}';
    }
}
