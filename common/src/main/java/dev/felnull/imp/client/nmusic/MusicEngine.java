package dev.felnull.imp.client.nmusic;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.lava.LavaPlayerManager;
import dev.felnull.imp.client.util.ALUtils;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.nmusic.tracker.MusicTracker;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MusicEngine {
    private static final MusicEngine INSTANCE = new MusicEngine();
    private final Map<UUID, MusicEntry> musicPlayers = new HashMap<>();
    private ExecutorService musicLoaderExecutor = createMusicLoadExecutor();
    private long lastProsesTime;
    private long lastTime;
    public boolean reloadFlag;

    public static MusicEngine getInstance() {
        return INSTANCE;
    }

    public String getDebugString() {
        return String.format("IMP Musics: %d/%d - %d/%d (%d loaders) %d ms", getCurrentMusicSpeaker(), getMaxMusicSpeaker(), getCurrentMusicLoad(), getMaxMusicLoad(), getCurrentMusicLoader(), lastProsesTime);
    }

    /**
     * 最大読み込み可能音楽数
     *
     * @return 数
     */
    public int getMaxMusicLoad() {
        return Math.max(IamMusicPlayer.CONFIG.maxPlayCont, 0);
    }

    /**
     * 最大スピーカー数
     *
     * @return 数
     */
    public int getMaxMusicSpeaker() {
        return 256;
    }

    /**
     * 現在の読み込み済み音楽数
     *
     * @return 数
     */
    public int getCurrentMusicLoad() {
        return musicPlayers.size();
    }

    /**
     * 現在のスピーカーの数
     *
     * @return 数
     */
    public int getCurrentMusicSpeaker() {
        int ct = 0;
        synchronized (musicPlayers) {
            for (MusicEntry value : musicPlayers.values()) {
                ct += value.getSpeakerCount();
            }
        }
        return ct;
    }

    /**
     * 現在の音楽読み込みスレッドの個数
     *
     * @return 数
     */
    public int getCurrentMusicLoader() {
        if (musicLoaderExecutor instanceof ThreadPoolExecutor threadPoolExecutor)
            return threadPoolExecutor.getActiveCount();
        return -1;
    }

    /**
     * ポーズされてない間毎Tick呼ばれる
     */
    public void tick() {
        lastTime = System.currentTimeMillis();

        synchronized (musicPlayers) {
            for (MusicEntry value : musicPlayers.values()) {
                value.tick();
            }
        }

        lastProsesTime = System.currentTimeMillis() - lastTime;
    }

    /**
     * ワールドから退出、再読み込み時に呼び出し
     * 再読み後すぐにも呼び出されてしまうので再読み時は無視
     */
    public void stop() {
        if (reloadFlag) {
            reloadFlag = false;
            return;
        }
    }

    /**
     * MusicEngine壊れる～
     * 再読み込みなどされたときに呼び出し
     */
    public void destroy() {
        if (musicLoaderExecutor != null) musicLoaderExecutor.shutdown();
        musicLoaderExecutor = createMusicLoadExecutor();

        LavaPlayerManager.getInstance().reload();
    }

    /**
     * ESCなどを押してポーズされたときに停止するときに呼ばれる
     */
    public void pause() {
        synchronized (musicPlayers) {
            for (MusicEntry value : musicPlayers.values()) {
                value.pause();
            }
        }
    }

    /**
     * ESCなどのポーズが解除されたときに呼ばれる
     */
    public void resume() {
        synchronized (musicPlayers) {
            for (MusicEntry value : musicPlayers.values()) {
                value.resume();
            }
        }
    }

    /**
     * 音楽読み込み用Executorを作成
     *
     * @return Executor
     */
    private ExecutorService createMusicLoadExecutor() {
        return Executors.newCachedThreadPool(new BasicThreadFactory.Builder().namingPattern("imp-music-loader-%d").daemon(true).build());
    }

    public ExecutorService getMusicLoaderExecutor() {
        return musicLoaderExecutor;
    }

    /**
     * 音楽を読み込む
     * 読み込みが完了しても再生されない
     *
     * @param musicPlayerId 音楽プレイヤーID
     * @param source        音楽情報
     * @param position      再生開始位置
     * @param listener      完了リスナー
     * @return 読み込み開始したかどうか、読み込み拒否(読み込み数が限界、すでに読み込み中、読み込み済み)などの時はfalse
     */
    public boolean load(@NotNull UUID musicPlayerId, @NotNull MusicSource source, long position, @NotNull LoadCompleteListener listener) {
        if (getCurrentMusicLoad() >= getMaxMusicLoad()) return false;

        if (isLoad(musicPlayerId)) return false;

        synchronized (musicPlayers) {
            var mpe = new MusicEntry();
            musicPlayers.put(musicPlayerId, mpe);

            mpe.loadStart(source, position, listener);
        }

        return true;
    }

    /**
     * 読み込み済み音楽の再生を開始する
     * 事前に {@link #load(UUID, MusicSource, long, LoadCompleteListener)}で読み込んでください
     * 読み込まれてない場合はfalse
     *
     * @param musicPlayerId 音楽プレイヤーID
     * @param delay         読み込んだ時間からの遅れ (最大10秒程)
     * @return 再生開始できたかどうか
     */
    public boolean play(@NotNull UUID musicPlayerId, long delay) {
        MusicEntry mpe;
        synchronized (musicPlayers) {
            mpe = musicPlayers.get(musicPlayerId);
        }
        if (mpe == null || !mpe.isLoaded())
            return false;

        ALUtils.runOnSoundThread(() -> mpe.playStart(delay));

        return true;
    }

    /**
     * スピーカーを追加
     *
     * @param musicPlayerId 音楽プレイヤーID
     * @param speakerId     スピーカーID
     * @param tracker       スピーカーのトラッカー
     * @return 追加できたかどうか
     */
    public boolean addSpeaker(@NotNull UUID musicPlayerId, @NotNull UUID speakerId, MusicTracker tracker) {
        if (getCurrentMusicSpeaker() >= getMaxMusicSpeaker()) return false;

        MusicEntry mpe;
        synchronized (musicPlayers) {
            mpe = musicPlayers.get(musicPlayerId);
        }

        if (mpe == null)
            return false;

        return mpe.addSpeaker(speakerId, tracker);
    }

    /**
     * 音楽が読み込まれてるかどうか
     *
     * @param musicPlayerId 音楽プレイヤーID
     * @return 読み込まれてるならtrue
     */
    public boolean isLoad(UUID musicPlayerId) {
        synchronized (musicPlayers) {
            if (musicPlayers.containsKey(musicPlayerId)) return true;
        }
        return false;
    }

    public static interface LoadCompleteListener {
        /**
         * 完了時の呼び出し
         *
         * @param success 読み込み成功したかどうか
         * @param time    読み込みにかかった時間
         * @param error   読み込み失敗時のエラー
         */
        void onComplete(boolean success, long time, Throwable error);
    }
}
