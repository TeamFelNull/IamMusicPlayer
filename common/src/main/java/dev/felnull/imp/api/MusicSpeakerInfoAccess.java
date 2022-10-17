package dev.felnull.imp.api;

import net.minecraft.world.phys.Vec3;

public interface MusicSpeakerInfoAccess {
    /**
     * スピーカーの再生場所
     *
     * @return 場所
     */
    Vec3 getPosition();

    /**
     * スピーカーの音量
     *
     * @return 音量(0f ~ 1f)
     */
    float getVolume();

    /**
     * スピーカーの音が届く範囲
     *
     * @return ブロック数
     */
    float getRange();

    /**
     * スピーカーの再生チャンネル
     * -1の場合はすべて
     *
     * @return 0=L 1=R
     */
    int getChannel();

    /**
     * 空間的に再生してるかどうか
     *
     * @return 空間的再生の有無
     */
    boolean isSpatial();
}
