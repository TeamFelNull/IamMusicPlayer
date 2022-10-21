package dev.felnull.imp.client.music;

public interface LoadCompleteListener {
    /**
     * 完了時の呼び出し
     *
     * @param success 読み込み成功したかどうか
     * @param time    読み込みにかかった時間
     * @param error   読み込み失敗時のエラー
     * @param retry   再読み込みしたほうがいい場合
     */
    void onComplete(boolean success, long time, Throwable error, boolean retry);
}
