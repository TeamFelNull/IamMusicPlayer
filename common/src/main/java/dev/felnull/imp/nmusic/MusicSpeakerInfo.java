package dev.felnull.imp.nmusic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

/**
 * 音楽が流れるスピーカー情報
 *
 * @param position スピーカーの場所
 * @param relative 相対的な音源かどうか
 * @param volume   ボリューム
 * @param range    範囲
 */
public record MusicSpeakerInfo(Vec3 position, boolean relative, float volume, float range) {
    public MusicSpeakerInfo() {
        this(Vec3.ZERO, false, 0, 0);
    }

    public CompoundTag toTag() {
        var tag = new CompoundTag();

        tag.putDouble("x", position.x);
        tag.putDouble("y", position.y);
        tag.putDouble("z", position.z);

        tag.putFloat("volume", volume);
        tag.putFloat("range", range);

        tag.putBoolean("relative", relative);

        return tag;
    }

    public static MusicSpeakerInfo loadByTag(CompoundTag tag) {
        var position = new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));

        float volume = tag.getFloat("volume");
        float range = tag.getFloat("range");

        boolean absolute = tag.getBoolean("relative");

        return new MusicSpeakerInfo(position, absolute, volume, range);
    }
}
