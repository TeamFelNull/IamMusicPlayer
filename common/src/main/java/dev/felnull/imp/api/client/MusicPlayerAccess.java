package dev.felnull.imp.api.client;

import java.util.Map;
import java.util.UUID;

public interface MusicPlayerAccess {
    /**
     * 読み込み中かどうか確認
     *
     * @return 読み込み中かどうか
     */
    boolean isLoading();

    /**
     * 再生中かどうか確認
     *
     * @return 再生中かどうか
     */
    boolean isPlaying();

    /**
     * 破棄済みかどうか確認
     *
     * @return 破棄済みかどうか
     */
    boolean isDestroy();

    /**
     * 一時停止されてるかどうか確認
     *
     * @return 一時停止されてるかどうか
     */
    boolean isPause();

    /**
     * 現在の再生位置を確認
     *
     * @return 再生位置(ms)
     */
    long getPosition();

    /**
     * スピーカーの一覧
     *
     * @return スピーカーとIDのMap
     */
    Map<UUID, MusicSpeakerAccess> getSpeakers();
}
