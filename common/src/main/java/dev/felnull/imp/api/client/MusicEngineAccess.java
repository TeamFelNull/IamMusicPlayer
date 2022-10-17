package dev.felnull.imp.api.client;

import java.util.Map;
import java.util.UUID;

public interface MusicEngineAccess {
    /**
     * 音楽プレイヤー一覧取得
     *
     * @return 音楽プレイヤーとID
     */
    Map<UUID, MusicPlayerAccess> getMusicPlayers();
}
