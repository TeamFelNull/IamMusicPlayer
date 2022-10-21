package dev.felnull.imp.music;

import dev.felnull.otyacraftengine.util.OENbtUtils;
import net.minecraft.nbt.CompoundTag;

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
        OENbtUtils.writeEnumByOrdinal(tag, "spatial_type", spatialType);
        return tag;
    }

    public static MusicSpeakerFixedInfo loadByTag(CompoundTag tag) {
        int channel = tag.getInt("channel");
        var spatialType = OENbtUtils.readEnumByOrdinal(tag, "spatial_type", SpatialType.class, SpatialType.ENTRUST);
        return new MusicSpeakerFixedInfo(channel, spatialType);
    }
}
