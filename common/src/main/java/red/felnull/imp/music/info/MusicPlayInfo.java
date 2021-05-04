package red.felnull.imp.music.info;

import net.minecraft.nbt.CompoundTag;
import red.felnull.imp.music.info.tracker.MusicTracker;
import red.felnull.otyacraftengine.data.ITAGSerializable;

public class MusicPlayInfo implements ITAGSerializable {
    public static final MusicPlayInfo EMPTY = new MusicPlayInfo(MusicTracker.EMPTY, 0, 0);
    private MusicTracker tracker;
    private float volume;
    private float maxDistance;

    public MusicPlayInfo(CompoundTag tag) {
        this.load(tag);
    }

    public MusicPlayInfo(MusicTracker tracker, float volume, float maxDistance) {
        this.tracker = tracker;
        this.volume = volume;
        this.maxDistance = maxDistance;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.put("Tracker", MusicTracker.saveTracker(tracker, new CompoundTag()));
        tag.putFloat("Volume", volume);
        tag.putFloat("MaxDistance", maxDistance);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.tracker = MusicTracker.loadTracker(tag.getCompound("Tracker"));
        this.volume = tag.getFloat("Volume");
        this.maxDistance = tag.getFloat("MaxDistance");
    }

    public MusicTracker getTracker() {
        return tracker;
    }

    public float getMaxDistance() {
        return maxDistance;
    }

    public float getVolume() {
        return volume;
    }
}
