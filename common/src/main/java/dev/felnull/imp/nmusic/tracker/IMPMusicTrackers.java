package dev.felnull.imp.nmusic.tracker;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.nmusic.MusicSpeakerFixedInfo;
import dev.felnull.imp.nmusic.MusicSpeakerInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

public class IMPMusicTrackers {
    public static final ResourceLocation FIXED_TRACKER = new ResourceLocation(IamMusicPlayer.MODID, "fixed");
    public static final ResourceLocation ENTITY_TRACKER = new ResourceLocation(IamMusicPlayer.MODID, "entity");
    public static final ResourceLocation PLAYER_TRACKER = new ResourceLocation(IamMusicPlayer.MODID, "player");

    public static Pair<ResourceLocation, FixedMusicTracker> createFixedTracker(Vec3 position, float volume, float range, int channel, boolean relative) {
        return Pair.of(FIXED_TRACKER, new FixedMusicTracker(new MusicSpeakerInfo(position, volume, range, new MusicSpeakerFixedInfo(channel, relative))));
    }

    public static Pair<ResourceLocation, FixedMusicTracker> createFixedTracker(Vec3 position, float volume, float range) {
        return Pair.of(FIXED_TRACKER, new FixedMusicTracker(new MusicSpeakerInfo(position, volume, range, new MusicSpeakerFixedInfo(-1, false))));
    }

    public static Pair<ResourceLocation, EntityMusicTracker> createEntityTracker(Entity entity, float volume, float range) {
        return Pair.of(ENTITY_TRACKER, new EntityMusicTracker(entity, volume, range));
    }

    public static Pair<ResourceLocation, PlayerMusicTracker> createPlayerTracker(Player player, float volume, float range) {
        return Pair.of(PLAYER_TRACKER, new PlayerMusicTracker(player, volume, range));
    }

    public static CompoundTag saveToTag(Pair<ResourceLocation, ? extends MusicTracker> tracker) {
        var tag = new CompoundTag();
        tag.putString("trackerId", tracker.getLeft().toString());
        tag.put("tracker", tracker.getRight().createSavedTag());
        return tag;
    }
}
