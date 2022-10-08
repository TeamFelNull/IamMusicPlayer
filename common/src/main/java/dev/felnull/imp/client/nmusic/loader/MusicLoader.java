package dev.felnull.imp.client.nmusic.loader;

import dev.felnull.imp.client.nmusic.player.MusicPlayer;
import dev.felnull.imp.music.resource.MusicSource;
import org.jetbrains.annotations.NotNull;

/**
 * 音楽ローダー
 * 音楽プレイヤーを作成する前に読み込みが利用可能か試行する
 */
public interface MusicLoader {
    /**
     * 音楽プレイヤーを作成
     * {@link #tryLoad(MusicSource)}を前もって呼び出し必須
     *
     * @return MusicPlayer
     */
    @NotNull
    MusicPlayer createMusicPlayer();

    /**
     * 試行時に読み込む
     * 読み込み失敗時はこのローダーは使用しない
     * 例外がスローされた場合は優先度順に次のローダを試行する
     *
     * @param source 音楽情報
     * @throws Exception 読み込み失敗
     */
    void tryLoad(@NotNull MusicSource source) throws Exception;

    /**
     * このローダを利用する優先度
     * 数値が大きいほど先に試行される
     *
     * @return 優先権
     */
    int priority();
}
