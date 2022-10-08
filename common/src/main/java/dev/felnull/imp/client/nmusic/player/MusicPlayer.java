package dev.felnull.imp.client.nmusic.player;

import dev.felnull.imp.client.nmusic.speaker.MusicSpeaker;

import java.util.UUID;

/**
 * 音楽を再生
 */
public interface MusicPlayer {
    /**
     * 音楽を読み込む
     *
     * @param position 再生開始秒
     * @throws Exception 読み込み失敗
     */
    void load(long position) throws Exception;

    /**
     * 音楽を再生開始する
     *
     * @param delay 遅延
     */
    void play(long delay);

    /**
     * 音楽プレイヤーを終了
     * リソースなどを解放
     */
    void destroy() throws Exception;

    /**
     * ESCなどで一時停止時に呼び出し
     */
    void pause();

    /**
     * ESCなどの一時停止の解除時に呼び出し
     */
    void resume();

    /**
     * 毎Tick呼び出し
     */
    void tick();

    /**
     * スピーカーを追加する
     *
     * @param speakerId スピーカーID
     * @param speaker   スピーカー
     */
    void addSpeaker(UUID speakerId, MusicSpeaker<?> speaker);

    /**
     * スピーカーを取得する
     *
     * @param uuid スピーカーID
     * @return 存在しない場合はnull
     */
    MusicSpeaker<?> getSpeaker(UUID uuid);
}
