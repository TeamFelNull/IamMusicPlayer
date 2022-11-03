package dev.felnull.imp.music;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * スピーカー固定情報
 *
 * @param channel     チャンネル数(-1で全チャンネル) 0=L 1=R
 * @param spatialType 空間的な再生かどうか
 */
public record MusicSpeakerFixedInfo(int channel, SpatialType spatialType) {
    public MusicSpeakerFixedInfo() {
        this(-1, SpatialType.ENTRUST);
    }

    public CompoundTag toTag() {
        var tag = new CompoundTag();
        tag.putInt("channel", channel);
        tag.putInt("spatial_type", spatialType.ordinal());
        return tag;
    }

    public static MusicSpeakerFixedInfo loadByTag(CompoundTag tag) {
        int channel = tag.getInt("channel");
        var spatialType = readEnumByOrdinal(tag, "spatial_type", SpatialType.class, SpatialType.ENTRUST);
        return new MusicSpeakerFixedInfo(channel, spatialType);
    }

    private static <T extends Enum<T>> T readEnumByOrdinal(@NotNull CompoundTag tag, @NotNull String name, @NotNull Class<T> enumClass, @Nullable T defaultEnum) {
        var ens = ((T[]) enumClass.getEnumConstants());
        int num = tag.getInt(name);
        if (num < 0 || ens.length <= num) return defaultEnum;
        return ens[num];
    }
}
