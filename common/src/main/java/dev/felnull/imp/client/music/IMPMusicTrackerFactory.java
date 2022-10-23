package dev.felnull.imp.client.music;

import dev.felnull.imp.music.tracker.*;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import dev.felnull.otyacraftengine.server.level.TagSerializable;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class IMPMusicTrackerFactory {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final Map<ResourceLocation, Supplier<? extends MusicTracker>> FACTORY_REGISTRY = new HashMap<>();

    public static void init() {
        register(IMPMusicTrackers.FIXED_TRACKER, FixedMusicTracker::new);
        register(IMPMusicTrackers.ENTITY_TRACKER, () -> new EntityMusicTracker(mc.level, OERenderUtils::getPartialTicks));
        register(IMPMusicTrackers.PLAYER_TRACKER, () -> new PlayerMusicTracker(mc.player, mc.level, OERenderUtils::getPartialTicks));
    }

    public static void register(ResourceLocation location, Supplier<? extends MusicTracker> factory) {
        FACTORY_REGISTRY.put(location, factory);
    }

    public static MusicTracker create(ResourceLocation location) {
        return FACTORY_REGISTRY.get(location).get();
    }

    @Nullable
    public static MusicTracker loadByTag(CompoundTag tag) {
        if (tag.isEmpty())
            return null;
        var id = new ResourceLocation(tag.getString("trackerId"));
        return TagSerializable.loadSavedTag(tag.getCompound("tracker"), create(id));
    }

    public static MusicTracker linked(MusicTrackerEntry trackerEntry) {
        var tag = IMPMusicTrackers.saveToTag(trackerEntry);
        return loadByTag(tag);
    }
}
