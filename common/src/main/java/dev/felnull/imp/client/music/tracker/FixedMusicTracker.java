package dev.felnull.imp.client.music.tracker;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class FixedMusicTracker implements IMusicTracker {
    private final Vec3 pos;

    public FixedMusicTracker(CompoundTag tag) {
        this(new Vec3(tag.getInt("x"), tag.getInt("y"), tag.getInt("z")));
    }

    public FixedMusicTracker(Vec3 pos) {
        this.pos = pos;
    }

    @Override
    public Supplier<Vec3> getPosition() {
        return () -> pos;
    }
}
