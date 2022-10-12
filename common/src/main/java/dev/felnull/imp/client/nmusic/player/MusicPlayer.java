package dev.felnull.imp.client.nmusic.player;

import dev.felnull.imp.client.nmusic.speaker.MusicSpeaker;
import dev.felnull.imp.client.nmusic.task.MusicTaskRunner;

import java.util.UUID;

/**
 * 音楽を再生
 */
public interface MusicPlayer {
    /**
     * 音楽を読み込む
     * 非同期で呼び出し
     *
     * @param position 再生開始秒
     * @throws Exception 読み込み失敗
     */
    void load(MusicTaskRunner runner, long position) throws Exception;

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
     * 終了済みか確認
     *
     * @return 終了済みかどうか
     */
    boolean isDestroy();

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

    /**
     * 読み込み中か確認
     *
     * @return 読み込み中かどうか
     */
    boolean isLoading();

    /**
     * 再生の用意が完了したか確認
     *
     * @return 用意が完了したかどうか
     */
    boolean isReady();

    /**
     * 再生中か確認
     *
     * @return 再生中かどうか
     */
    boolean isPlaying();

    /**
     * 現在存在するスピーカーの数
     *
     * @return スピーカーの数
     */
    int getSpeakerCount();

    /**
     * 現在の再生位置
     *
     * @return 再生位置
     */
    long getPosition();

    /**
     * チャンネル数を取得
     *
     * @return チャンネル数
     */
    int getChannels();

    /**
     * 現在の波形の大きさを取得
     *
     * @return 波形情報(0 ~ 1)
     */
    float getCurrentAudioWave(int channel);
}
