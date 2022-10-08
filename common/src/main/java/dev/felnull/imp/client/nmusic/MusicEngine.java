package dev.felnull.imp.client.nmusic;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.lava.LavaPlayerManager;
import dev.felnull.imp.music.resource.MusicSource;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MusicEngine {
    private static final MusicEngine INSTANCE = new MusicEngine();
    private final Map<UUID, MusicPlayerEntry> musicPlayers = new HashMap<>();
    private ExecutorService musicLoaderExecutor = createMusicLoadExecutor();
    private long lastProsesTime;
    private long lastTime;
    public boolean reloadFlag;

    public static MusicEngine getInstance() {
        return INSTANCE;
    }

    public String getDebugString() {
        return String.format("IMP Musics: %d/%d - %d/%d (%d loaders) %d ms", getCurrentMusicPlayer(), getMaxMusicPlayer(), getCurrentMusicLoad(), getMaxMusicLoad(), getCurrentMusicLoader(), lastProsesTime);
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
     * 最大再生可能音楽数
     *
     * @return 数
     */
    public int getMaxMusicPlayer() {
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
     * 現在の再生音楽数
     *
     * @return 数
     */
    public int getCurrentMusicPlayer() {
        return 0;
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

    }

    /**
     * ESCなどのポーズが解除されたときに呼ばれる
     */
    public void resume() {

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
     * @param listener      完了リスナー
     * @return 読み込み開始したかどうか、読み込み拒否(読み込み数が限界、すでに読み込み中、読み込み済み)などの時はfalse
     */
    public boolean load(UUID musicPlayerId, MusicSource source, LoadCompleteListener listener) {
        if (getCurrentMusicLoad() >= getMaxMusicLoad())
            return false;
        synchronized (musicPlayers) {
            if (musicPlayers.containsKey(musicPlayerId))
                return false;
        }

        return true;
    }

    public static interface LoadCompleteListener {
        void onComplete(boolean success, long time);
    }

    private static class MusicPlayerEntry {

    }
}
