package dev.felnull.imp.server.music.ringer;

import dev.felnull.imp.music.resource.MusicSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DummyRinger implements IMusicRinger {
    private final UUID uuid = UUID.randomUUID();
    private final ServerLevel level;

    public DummyRinger(ServerLevel level) {
        this.level = level;
    }

    @Override
    public Component getRingerName(ServerLevel level) {
        return new TextComponent("dummy");
    }

    @Override
    public ServerLevel getRingerLevel() {
        return level;
    }

    @Override
    public UUID getRingerUUID() {
        return uuid;
    }

    @Override
    public boolean isRingerExist(ServerLevel level) {
        return false;
    }

    @Override
    public boolean isRingerPlaying(ServerLevel level) {
        return false;
    }

    @Override
    public void setRingerPlaying(ServerLevel level, boolean playing) {

    }

    @Override
    public @Nullable MusicSource getRingerMusicSource(ServerLevel level) {
        return MusicSource.EMPTY;
    }

    @Override
    public boolean isRingerLoop(ServerLevel level) {
        return false;
    }

    @Override
    public long getRingerPosition(ServerLevel level) {
        return 0;
    }

    @Override
    public void setRingerPosition(ServerLevel level, long position) {

    }

    @Override
    public Pair<ResourceLocation, CompoundTag> getRingerTracker(ServerLevel level) {
        return Pair.of(MusicRingManager.FIXED_TRACKER, MusicRingManager.createFixedTracker(Vec3.ZERO));
    }

    @Override
    public @NotNull Vec3 getRingerSpatialPosition(ServerLevel level) {
        return Vec3.ZERO;
    }

    @Override
    public float getRingerVolume(ServerLevel level) {
        return 0;
    }

    @Override
    public float getRingerRange(ServerLevel level) {
        return 0;
    }
}
