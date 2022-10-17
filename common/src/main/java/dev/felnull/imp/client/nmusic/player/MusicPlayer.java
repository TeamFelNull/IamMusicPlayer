package dev.felnull.imp.client.nmusic.player;

import dev.felnull.imp.api.client.MusicPlayerAccess;
import dev.felnull.imp.client.nmusic.AudioInfo;
import dev.felnull.imp.client.nmusic.MusicEngine;
import dev.felnull.imp.client.nmusic.speaker.MusicSpeaker;

import java.util.UUID;

public interface MusicPlayer<T, E> extends MusicPlayerAccess {
    /**
     * 音楽を再生開始する
     * Tick上で実行される
     *
     * @param delay 遅延
     */
    void play(long delay);

    /**
     * 音楽プレイヤーを終了
     * リソースなどを解放
     * Tick上で実行される
     */
    void destroy() throws Exception;

    default void destroyNonThrow() {
        try {
            destroy();
        } catch (Exception ex) {
            MusicEngine.getInstance().getLogger().error("Failed to destroy music player", ex);
        }
    }

    /**
     * ESCなどで一時停止時に呼び出し
     * Tick上で実行される
     */
    void pause();

    /**
     * ESCなどの一時停止の解除時に呼び出し
     * Tick上で実行される
     */
    void resume();

    /**
     * 毎Tick呼び出し
     */
    void tick();

    /**
     * スピーカーを追加する
     * Tick上で実行される
     *
     * @param speakerId スピーカーID
     * @param speaker   スピーカー
     */
    void addSpeaker(UUID speakerId, MusicSpeaker speaker);

    /**
     * スピーカーを取得する
     *
     * @param uuid スピーカーID
     * @return 存在しない場合はnull
     */
    MusicSpeaker getSpeaker(UUID uuid);

    /**
     * 指定のスピーカーを削除する
     *
     * @param uuid スピーカーID
     */
    void removeSpeaker(UUID uuid);

    /**
     * 読み込み開始時に呼び出し
     * Tick上で呼び出されるので重い処理はNG
     *
     * @param position 再生位置
     * @return #loadAsyncに渡す情報
     */
    T loadStart(long position);

    /**
     * 非同期で実行される読み込み
     * ここではなるべく外部に影響を与えないでください
     *
     * @param input {@link #loadStart(long)}の結果
     */
    E loadAsync(T input) throws Exception;

    /**
     * 非同期読み込みが完了し適用される
     * Tick上で呼び出されるので重い処理はNG
     */
    void loadApply(E loadedData);

    /**
     * 音声情報を取得
     *
     * @return 音声情報
     */
    AudioInfo getAudioInfo();

    /**
     * 現在の波形の大きさを取得
     *
     * @return 波形情報(0 ~ 1)
     */
    float getCurrentAudioWave(int channel);

    /**
     * 現在のスピーカー数
     *
     * @return スピーカーの数
     */
    int getSpeakerCount();

    /**
     * 破壊待ち
     * 再生が終了、途中で読み込みに失敗などの理由で利用不可になった時にTrue
     *
     * @return 破壊待ちかどうか
     */
    boolean waitDestroy();
}
