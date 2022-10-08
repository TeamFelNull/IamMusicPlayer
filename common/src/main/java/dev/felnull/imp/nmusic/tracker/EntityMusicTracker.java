package dev.felnull.imp.nmusic.tracker;

import dev.felnull.imp.nmusic.MusicSpeakerInfo;
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
        super(new MusicSpeakerInfo(entity.position(), false, volume, range));
        this.level = null;
        this.entityID = entity.getId();
    }

    @Override
    public MusicSpeakerInfo getSpeakerInfo() {
        var sp = super.getSpeakerInfo();
        if (level != null && entityID >= 0) {
            var en = level.getEntity(entityID);
            if (en != null)
                return new MusicSpeakerInfo(en.position(), false, sp.volume(), sp.range());
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
