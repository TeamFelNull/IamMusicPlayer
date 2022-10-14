package dev.felnull.imp.nmusic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

/**
 * 音楽が流れるスピーカー情報
 *
 * @param position  スピーカーの場所
 * @param volume    ボリューム
 * @param range     範囲
 * @param fixedInfo 固定情報
 */
public record MusicSpeakerInfo(Vec3 position, float volume, float range, MusicSpeakerFixedInfo fixedInfo) {
    public MusicSpeakerInfo() {
        this(Vec3.ZERO, 0, 0, new MusicSpeakerFixedInfo());
    }

    public CompoundTag toTag() {
        var tag = new CompoundTag();

        tag.putDouble("x", position.x);
        tag.putDouble("y", position.y);
        tag.putDouble("z", position.z);

        tag.putFloat("volume", volume);
        tag.putFloat("range", range);

        tag.put("fixed_info", fixedInfo.toTag());

        return tag;
    }

    public static MusicSpeakerInfo loadByTag(CompoundTag tag) {
        var position = new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));

        float volume = tag.getFloat("volume");
        float range = tag.getFloat("range");

        var fixedInfo = MusicSpeakerFixedInfo.loadByTag(tag.getCompound("fixed_info"));

        return new MusicSpeakerInfo(position, volume, range, fixedInfo);
    }
}
