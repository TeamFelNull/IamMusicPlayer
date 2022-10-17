package dev.felnull.imp.api.client;

import dev.felnull.imp.api.MusicSpeakerInfoAccess;

public interface MusicSpeakerAccess {
    /**
     * スピーカーの再生情報の取得
     *
     * @return 再生情報
     */
    MusicSpeakerInfoAccess getInfo();
}
