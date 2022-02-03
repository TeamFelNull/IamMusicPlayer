package dev.felnull.imp.client.music.tracker;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class EntityMusicTracker implements IMusicTracker {
    private static final Minecraft mc = Minecraft.getInstance();
    private final int entityID;
    private Vec3 pos;

    public EntityMusicTracker(CompoundTag tag) {
        this(new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z")), tag.getInt("id"));
    }

    public EntityMusicTracker(Vec3 pos, int entityID) {
        this.entityID = entityID;
        this.pos = pos;
    }

    @Override
    public Supplier<Vec3> getPosition() {
        return () -> {
            if (mc.level != null) {
                var en = mc.level.getEntity(entityID);
                if (en != null)
                    this.pos = en.position();
            }
            return pos;
        };
    }
}
