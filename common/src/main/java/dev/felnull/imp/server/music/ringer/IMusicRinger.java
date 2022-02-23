package dev.felnull.imp.server.music.ringer;

import dev.felnull.imp.music.resource.MusicSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IMusicRinger {
    public Component getRingerName();

    public ServerLevel getRingerLevel();

    public UUID getRingerUUID();

    public boolean isRingerExist();

    public boolean isRingerPlaying();

    public void setRingerPlaying(boolean playing);

    public @Nullable MusicSource getRingerMusicSource();

    public boolean isRingerLoop();

    public long getRingerPosition();

    public void setRingerPosition(long position);

    public Pair<ResourceLocation, CompoundTag> getRingerTracker();

    public @NotNull Vec3 getRingerSpatialPosition();

    public float getRingerVolume();

    public float getRingerRange();

    public boolean isRingerStream();

    default public boolean isRingerWait() {
        return MusicRingManager.getInstance().isWaitRinger(getRingerUUID(), getRingerLevel());
    }

    default public void ringerTick() {
        if (!MusicRingManager.getInstance().isExistRinger(getRingerLevel(), getRingerUUID()))
            addRingerInRingManager();
    }

    default public void addRingerInRingManager() {
        MusicRingManager.getInstance().addRinger(getRingerLevel(), this);
    }

    default public void ringerRestart() {
        MusicRingManager.getInstance().restartRinger(getRingerLevel(), getRingerUUID());
    }

    default public void ringerEnd() {

    }

    public boolean isRingerMute();

    default public boolean isRingerRemote() {
        return false;
    }

    @Nullable
    default public String getRingerMusicAuthor() {
        return null;
    }

    @NotNull
    default public ItemStack getRingerAntenna() {
        return ItemStack.EMPTY;
    }
}
