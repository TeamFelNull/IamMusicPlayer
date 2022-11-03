package dev.felnull.imp.music.tracker;

import dev.architectury.utils.value.FloatSupplier;
import dev.felnull.imp.music.MusicSpeakerFixedInfo;
import dev.felnull.imp.music.MusicSpeakerInfo;
import dev.felnull.imp.music.SpatialType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityMusicTracker extends FixedMusicTracker {
    private final Level level;
    private final FloatSupplier deltaSupplier;
    private int entityID = -1;

    public EntityMusicTracker(Level level, FloatSupplier deltaSupplier) {
        this.level = level;
        this.deltaSupplier = deltaSupplier;
    }

    public EntityMusicTracker(Entity entity, float volume, float range) {
        super(new MusicSpeakerInfo(entity.position(), volume, range, new MusicSpeakerFixedInfo(-1, SpatialType.ENTRUST)));
        this.level = null;
        this.deltaSupplier = null;
        this.entityID = entity.getId();
    }

    @Override
    public MusicSpeakerInfo getSpeakerInfo() {
        var sp = super.getSpeakerInfo();
        if (level != null && deltaSupplier != null && entityID >= 0) {
            var en = level.getEntity(entityID);
            if (en != null)
                return new MusicSpeakerInfo(getEntityPosition(en, deltaSupplier.getAsFloat()), sp.volume(), sp.range(), new MusicSpeakerFixedInfo(-1, SpatialType.ENTRUST));
        }
        return sp;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        tag.putInt("EntityID", entityID);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.entityID = tag.getInt("EntityID");
    }

    public static Vec3 getEntityPosition(Entity entity, float delta) {
        return entity.getEyePosition(delta);
    }
}
