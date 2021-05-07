package red.felnull.imp.music.info;

import net.minecraft.nbt.CompoundTag;
import red.felnull.imp.music.info.tracker.MusicTracker;
import red.felnull.otyacraftengine.data.ITAGSerializable;

public class MusicPlayInfo implements ITAGSerializable {
    public static final MusicPlayInfo EMPTY = new MusicPlayInfo(MusicTracker.EMPTY);
    private MusicTracker tracker;

    public MusicPlayInfo(CompoundTag tag) {
        this.load(tag);
    }

    public MusicPlayInfo(MusicTracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.put("Tracker", MusicTracker.saveTracker(tracker, new CompoundTag()));
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.tracker = MusicTracker.loadTracker(tag.getCompound("Tracker"));
    }

    public MusicTracker getTracker() {
        return tracker;
    }


}
