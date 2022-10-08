package dev.felnull.imp.client.music.trackerold;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class FixedMusicTrackerOld implements IMusicTrackerOld {
    private final Vec3 pos;

    public FixedMusicTrackerOld(CompoundTag tag) {
        this(new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z")));
    }

    public FixedMusicTrackerOld(Vec3 pos) {
        this.pos = pos;
    }

    @Override
    public Supplier<Vec3> getPosition() {
        return () -> pos;
    }
}
