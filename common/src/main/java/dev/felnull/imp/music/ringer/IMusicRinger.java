package dev.felnull.imp.music.ringer;

import dev.felnull.imp.music.resource.MusicSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IMusicRinger {
    public UUID getRingerUUID();

    public boolean isRingerExist(ServerLevel level);

    public boolean isRingerPlaying(ServerLevel level);

    public void setRingerPlaying(ServerLevel level, boolean playing);

    public @Nullable MusicSource getRingerMusicSource(ServerLevel level);

    public boolean isRingerLoop(ServerLevel level);

    public long getRingerPosition(ServerLevel level);

    public void setRingerPosition(ServerLevel level, long position);

    public Pair<ResourceLocation, CompoundTag> getRingerTracker(ServerLevel level);

    public @NotNull Vec3 getRingerVec3Position(ServerLevel level);

    public float getRingerVolume(ServerLevel level);

    public float getRingerRange(ServerLevel level);

    default public boolean isRingerWait(ServerLevel level) {
        return MusicRingManager.getInstance().isWaitRinger(getRingerUUID(), level);
    }

    default public void ringerTick(ServerLevel level) {
        if (!MusicRingManager.getInstance().isExistRinger(level, getRingerUUID()))
            addRingerInRingManager(level);
    }

    default public void addRingerInRingManager(ServerLevel level) {
        var rm = MusicRingManager.getInstance();
        rm.addRinger(level, this);
    }
}
