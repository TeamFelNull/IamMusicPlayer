package dev.felnull.imp.nmusic.tracker;

import dev.felnull.imp.nmusic.MusicSpeakerFixedInfo;
import dev.felnull.imp.nmusic.MusicSpeakerInfo;
import dev.felnull.imp.nmusic.SpatialType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityMusicTracker extends FixedMusicTracker {
    private final Level level;
    private int entityID = -1;

    public EntityMusicTracker(Level level) {
        this.level = level;
    }

    public EntityMusicTracker(Entity entity, float volume, float range) {
        super(new MusicSpeakerInfo(entity.position(), volume, range, new MusicSpeakerFixedInfo(-1, SpatialType.ENTRUST)));
        this.level = null;
        this.entityID = entity.getId();
    }

    @Override
    public MusicSpeakerInfo getSpeakerInfo() {
        var sp = super.getSpeakerInfo();
        if (level != null && entityID >= 0) {
            var en = level.getEntity(entityID);
            if (en != null)
                return new MusicSpeakerInfo(en.position(), sp.volume(), sp.range(), new MusicSpeakerFixedInfo(-1, SpatialType.ENTRUST));
        }
        return sp;
    }

    @Override
    public void save(CompoundTag tag) {
        super.save(tag);
        tag.putInt("EntityID", entityID);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.entityID = tag.getInt("EntityID");
    }
}
