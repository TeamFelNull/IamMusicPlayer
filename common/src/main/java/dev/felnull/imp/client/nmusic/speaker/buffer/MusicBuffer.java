package dev.felnull.imp.client.nmusic.speaker.buffer;

/**
 * スピーカーに送る音楽データバッファー情報
 */
public interface MusicBuffer<T> {
    /**
     * 非同期で処理される
     *
     * @param data       音声データ
     * @param sampleRate サンプルレート
     * @param channel    チャンネル数
     * @param bit        ビッド
     * @return 変換済みデータ
     */
    T asyncConvertBuffer(byte[] data, int sampleRate, int channel, int bit);

    /**
     * 変換済みデータをバッファーに入れる
     * ClientTick上で呼ばれる
     *
     * @param data 変換済みデータ
     */
    void putBuffer(T data, MusicBufferSpeakerData bufferSpeakerData);

    /**
     * データ解放
     * MusicPlayerでのみ呼び出し
     */
    void release();
}
