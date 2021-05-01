package red.felnull.imp.music.info.tracker;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import red.felnull.otyacraftengine.data.ITAGSerializable;
import red.felnull.otyacraftengine.fluid.IkisugiFluid;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class MusicTracker implements ITAGSerializable {
    public static final MusicTracker EMPTY = ((Supplier<MusicTracker>) () -> new FixedMusicTracker(Vec3.ZERO)).get();

    abstract ResourceLocation getTrackerDefinition();
}
