package dev.felnull.imp.nmusic;

import net.minecraft.nbt.CompoundTag;

/**
 * スピーカー固定情報
 *
 * @param channel  チャンネル数(-1で全チャンネル) 0=L 1=R
 * @param relative 相対的に再生
 */
public record MusicSpeakerFixedInfo(int channel, boolean relative) {
    public MusicSpeakerFixedInfo() {
        this(-1, false);
    }

    public CompoundTag toTag() {
        var tag = new CompoundTag();
        tag.putInt("channel", channel);
        tag.putBoolean("relative", relative);
        return tag;
    }

    public static MusicSpeakerFixedInfo loadByTag(CompoundTag tag) {
        int channel = tag.getInt("channel");
        boolean relative = tag.getBoolean("relative");
        return new MusicSpeakerFixedInfo(channel, relative);
    }
}
