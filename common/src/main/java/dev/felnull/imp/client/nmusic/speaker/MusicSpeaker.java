package dev.felnull.imp.client.nmusic.speaker;

import dev.felnull.imp.client.nmusic.speaker.buffer.MusicBuffer;
import dev.felnull.imp.client.nmusic.speaker.buffer.MusicBufferSpeakerData;
import dev.felnull.imp.nmusic.tracker.MusicTracker;

import java.util.List;

/**
 * 音楽を実際に再生するスピーカー
 * {@link #insertAudio(MusicBuffer)}でデータを挿入<br>
 * ↓<br>
 * {@link #play()}で再生開始<br>
 * ↓<br>
 * {@link #insertAudio(MusicBuffer)}でデータを挿入し続けてください<br>
 * <p>
 * {@link #isDead()}がTrueになってしまったら新しくスピーカーを生成してください
 */
public interface MusicSpeaker<T extends MusicBuffer<?>> {
    /**
     * 音楽トラッカーを更新
     *
     * @param tracker トラッカー
     */
    void update(MusicTracker tracker);

    /**
     * 毎Tick呼び出し
     */
    void tick();

    /**
     * 曲データを挿入
     * Little Endianの16bitのPCM波形
     *
     * @param data LittleEndianの16bitのPCM波形データ
     */
    void insertAudio(T data);

    /**
     * バッファーを作成(空)
     *
     * @return バッファー
     */
    T createBuffer();


    /**
     * 再生開始
     */
    void play();

    /**
     * すでに再生し終わって利用不可かどうか
     *
     * @return 枯れてるかどうか
     */
    boolean isDead();

    /**
     * ESCなどで一時停止時に呼び出し
     */
    void pause();

    /**
     * ESCなどの一時停止の解除時に呼び出し
     */
    void resume();

    /**
     * リソース破棄
     *
     * @throws Exception 失敗
     */
    void destroy() throws Exception;

    /**
     * 使用済みバッファーデータを取得
     *
     * @return 使用済みバッファーデータ
     */
    List<T> pollBuffers();

    /**
     * スピーカー生成時に渡す変数データ
     *
     * @return データ
     */
    MusicBufferSpeakerData getBufferSpeakerData();
}
