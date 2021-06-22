package red.felnull.imp.music.info.tracker;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import red.felnull.imp.IamMusicPlayer;

public class EntityMusicTracker extends MusicTracker {
    private int entityID;

    public EntityMusicTracker() {

    }

    public EntityMusicTracker(Vec3 position, float volume, float maxDistance, Entity entity) {
        super(position, volume, maxDistance);
        this.entityID = entity.getId();
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(IamMusicPlayer.MODID, "entity");
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("EntityID", entityID);
        return super.save(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        this.entityID = tag.getInt("EntityID");
        super.load(tag);
    }

    @Override
    public Vec3 getTrackingPosition(Level level) {
        if (level != null && level.getEntity(entityID) != null) {
            return level.getEntity(entityID).position();
        }
        return super.getTrackingPosition(level);
    }
}
