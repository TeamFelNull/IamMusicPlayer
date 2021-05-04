package red.felnull.imp.music.info.tracker;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import red.felnull.imp.api.IMPRegistry;
import red.felnull.otyacraftengine.data.ITAGSerializable;

import java.util.function.Supplier;

public abstract class MusicTracker implements ITAGSerializable {
    public static final MusicTracker EMPTY = ((Supplier<MusicTracker>) () -> new FixedMusicTracker(Vec3.ZERO)).get();
    private Vec3 position;

    public MusicTracker() {

    }

    public MusicTracker(Vec3 position) {
        this.position = position;
    }

    public abstract ResourceLocation getRegistryName();

    public static MusicTracker loadTracker(CompoundTag tag) {
        ResourceLocation location = new ResourceLocation(tag.getString("RegistryName"));
        if (IMPRegistry.isTrackerContains(location)) {
            MusicTracker tracker = IMPRegistry.getTracker(location).get();
            tracker.load(tag.getCompound("TrackerData"));
            return tracker;
        } else {
            return EMPTY;
        }
    }

    public static CompoundTag saveTracker(MusicTracker tracker, CompoundTag tag) {
        tag.putString("RegistryName", tracker.getRegistryName().toString());
        tag.put("TrackerData", tracker.save(new CompoundTag()));
        return tag;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putDouble("PositionX", position.x);
        tag.putDouble("PositionY", position.y);
        tag.putDouble("PositionZ", position.z);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.position = new Vec3(tag.getDouble("PositionX"), tag.getDouble("PositionY"), tag.getDouble("PositionZ"));
    }

    public Vec3 getTrackingPosition(Level level) {
        return position;
    }

}
