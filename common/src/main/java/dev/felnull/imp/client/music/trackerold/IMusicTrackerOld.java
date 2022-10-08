package dev.felnull.imp.client.music.trackerold;

import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public interface IMusicTrackerOld {
    Supplier<Vec3> getPosition();
}
