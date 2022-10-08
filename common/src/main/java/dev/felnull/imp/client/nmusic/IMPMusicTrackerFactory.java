package dev.felnull.imp.client.nmusic;

import dev.felnull.imp.nmusic.tracker.*;
import dev.felnull.otyacraftengine.server.level.TagSerializable;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class IMPMusicTrackerFactory {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final Map<ResourceLocation, Supplier<? extends MusicTracker>> FACTORY_REGISTRY = new HashMap<>();

    public static void init() {
        register(IMPMusicTrackers.FIXED_TRACKER, FixedMusicTracker::new);
        register(IMPMusicTrackers.ENTITY_TRACKER, () -> new EntityMusicTracker(mc.level));
        register(IMPMusicTrackers.PLAYER_TRACKER, () -> new PlayerMusicTracker(mc.player, mc.level));
    }

    public static void register(ResourceLocation location, Supplier<? extends MusicTracker> factory) {
        FACTORY_REGISTRY.put(location, factory);
    }

    public static MusicTracker create(ResourceLocation location) {
        return FACTORY_REGISTRY.get(location).get();
    }

    public static MusicTracker loadByTag(CompoundTag tag) {
        var id = new ResourceLocation(tag.getString("trackerId"));
        return TagSerializable.loadSavedTag(tag.getCompound("tracker"), create(id));
    }

    public static MusicTracker linked(Pair<ResourceLocation, ? extends MusicTracker> tracker) {
        var tag = IMPMusicTrackers.saveToTag(tracker);
        return loadByTag(tag);
    }
}
