package dev.felnull.imp.api.music;

import dev.felnull.imp.server.music.ringer.IMusicRinger;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public record MusicRingerAccess(IMusicRinger ringer) {
    /**
     * 音源の名前を取得
     *
     * @return 音源名
     */
    public Component getName() {
        return ringer.getRingerName(ringer.getRingerLevel());
    }

    /**
     * 音源の空間位置を取得
     *
     * @return 座標
     */
    public Vec3 getSpatialPosition() {
        return ringer.getRingerSpatialPosition(ringer.getRingerLevel());
    }

    /**
     * 音源が再生中かどうか
     *
     * @return 再生中かどうか
     */
    public boolean isPlaying() {
        return ringer.isRingerPlaying(ringer.getRingerLevel());
    }

    /**
     * 音源のレベルを取得
     *
     * @return レベル
     */
    public ServerLevel getLevel() {
        return ringer.getRingerLevel();
    }
}
