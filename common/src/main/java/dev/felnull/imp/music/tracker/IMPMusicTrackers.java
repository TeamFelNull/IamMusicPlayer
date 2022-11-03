package dev.felnull.imp.music.tracker;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.music.MusicSpeakerFixedInfo;
import dev.felnull.imp.music.MusicSpeakerInfo;
import dev.felnull.imp.music.SpatialType;
import dev.felnull.otyacraftengine.util.OENbtUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class IMPMusicTrackers {
    public static final ResourceLocation FIXED_TRACKER = new ResourceLocation(IamMusicPlayer.MODID, "fixed");
    public static final ResourceLocation ENTITY_TRACKER = new ResourceLocation(IamMusicPlayer.MODID, "entity");
    public static final ResourceLocation PLAYER_TRACKER = new ResourceLocation(IamMusicPlayer.MODID, "player");

    public static MusicTrackerEntry createFixedTracker(Vec3 position, float volume, float range, int channel, SpatialType spatialType) {
        return new MusicTrackerEntry(FIXED_TRACKER, new FixedMusicTracker(new MusicSpeakerInfo(position, volume, range, new MusicSpeakerFixedInfo(channel, spatialType))));
    }

    public static MusicTrackerEntry createFixedTracker(Vec3 position, float volume, float range) {
        return new MusicTrackerEntry(FIXED_TRACKER, new FixedMusicTracker(new MusicSpeakerInfo(position, volume, range, new MusicSpeakerFixedInfo(-1, SpatialType.ENTRUST))));
    }

    public static MusicTrackerEntry createEntityTracker(Entity entity, float volume, float range) {
        if (entity instanceof Player player) return createPlayerTracker(player, volume, range);
        return new MusicTrackerEntry(ENTITY_TRACKER, new EntityMusicTracker(entity, volume, range));
    }

    public static MusicTrackerEntry createPlayerTracker(Player player, float volume, float range) {
        return new MusicTrackerEntry(PLAYER_TRACKER, new PlayerMusicTracker(player, volume, range));
    }

    public static CompoundTag saveToTag(MusicTrackerEntry trackerEntry) {
        var tag = new CompoundTag();
        tag.putString("trackerId", trackerEntry.location().toString());
        OENbtUtil.writeSerializable(tag, "tracker", trackerEntry.tracker());
        return tag;
    }
}
