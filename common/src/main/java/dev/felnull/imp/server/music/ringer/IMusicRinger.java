package dev.felnull.imp.server.music.ringer;

import dev.felnull.imp.api.MusicRingerAccess;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.music.tracker.MusicTrackerEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IMusicRinger extends MusicRingerAccess {
    Component getRingerName();

    ServerLevel getRingerLevel();

    UUID getRingerUUID();

    boolean exists();

    boolean isRingerPlaying();

    void setRingerPlaying(boolean playing);

    @Nullable
    MusicSource getRingerMusicSource();

    boolean isRingerLoop();

    long getRingerPosition();

    void setRingerPosition(long position);

    MusicTrackerEntry getRingerTracker();

    @NotNull
    Vec3 getRingerSpatialPosition();

    float getRingerVolume();

    float getRingerRange();

    boolean isRingerStream();

    default boolean isRingerWait() {
        return getMusicRing().isWaitRinger(getRingerUUID());
    }

    default MusicRing getMusicRing() {
        return MusicRingManager.getInstance().getMusicRing(getRingerLevel());
    }

    default boolean alreadyAdded() {
        return getMusicRing().hasRinger(getRingerUUID());
    }

    default void ringerTick() {
        if (!alreadyAdded())
            addRingerInRingManager();
    }

    default void addRingerInRingManager() {
        getMusicRing().addRinger(this);
    }

    default void ringerRestart() {
        getMusicRing().restart(getRingerUUID());
    }

    default void ringerEnd() {
    }

    boolean isRingerMute();

    default boolean isRingerRemote() {
        return false;
    }

    @Nullable
    default String getRingerMusicAuthor() {
        return null;
    }

    @NotNull
    default ItemStack getRingerAntenna() {
        return ItemStack.EMPTY;
    }

    @Override
    default Component getName() {
        return getRingerName();
    }

    @Override
    default Vec3 getSpatialPosition() {
        return getRingerSpatialPosition();
    }

    @Override
    default boolean isPlaying() {
        return isRingerPlaying();
    }

    @Override
    default ServerLevel getServerLevel() {
        return getRingerLevel();
    }
}
