package dev.felnull.imp.client.music.tracker;

import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public interface IMusicTracker {
    Supplier<Vec3> getPosition();
}
