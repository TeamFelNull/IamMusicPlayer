package dev.felnull.imp.api;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public interface MusicRingerAccess {
    /**
     * 音源の名前を取得
     *
     * @return 音源名
     */
    Component getName();


    /**
     * 音源の空間位置を取得
     *
     * @return 座標
     */
    Vec3 getSpatialPosition();

    /**
     * 音源が再生中かどうか
     *
     * @return 再生中かどうか
     */
    boolean isPlaying();

    /**
     * 音源のサーバーレベルを取得
     *
     * @return レベル
     */
    ServerLevel getServerLevel();
}
