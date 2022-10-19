package dev.felnull.imp.client.nmusic;

import com.google.common.collect.ImmutableMap;
import dev.felnull.fnjl.concurrent.InvokeExecutor;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.api.client.MusicEngineAccess;
import dev.felnull.imp.api.client.MusicPlayerAccess;
import dev.felnull.imp.client.lava.LavaPlayerManager;
import dev.felnull.imp.client.nmusic.task.MusicEngineDestroyRunner;
import dev.felnull.imp.client.util.MusicUtils;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.nmusic.tracker.MusicTracker;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MusicEngine implements MusicEngineAccess {
    private static final Logger LOGGER = LogManager.getLogger(MusicEngine.class);
    private static final MusicEngine INSTANCE = new MusicEngine();
    private final Map<UUID, MusicEntry> musicEntries = new HashMap<>();
    private final InvokeExecutor musicTickExecutor = new InvokeExecutor();
    private ExecutorService musicLoaderExecutor = createMusicLoadExecutor();
    private MusicEngineDestroyRunner musicDestroyRunner = new MusicEngineDestroyRunner();
    private long lastProsesTime;
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
        return musicEntries.size();
    }

    /**
     * 現在のスピーカーの数
     *
     * @return 数
     */
    public int getCurrentMusicSpeaker() {
        int ct = 0;
        synchronized (musicEntries) {
            for (MusicEntry value : musicEntries.values()) {
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
     * 毎Tick呼ばれる
     */
    public void tick() {
        long startTime = System.currentTimeMillis();

        MusicUtils.runInvokeTasks(musicTickExecutor, "Music Engine");

        synchronized (musicEntries) {
            List<UUID> destroys = new ArrayList<>();
            musicEntries.forEach((uuid, musicEntry) -> {
                if (!musicEntry.tick()) destroys.add(uuid);
            });
            for (UUID destroy : destroys) {
                musicEntries.remove(destroy);
            }
        }

        lastProsesTime = System.currentTimeMillis() - startTime;
    }

    /**
     * ワールドから退出、再読み込み時に呼び出し
     * 再読み後すぐにも呼び出されてしまうので再読み時は無視
     */
    public void stopAll() {
        if (reloadFlag) {
            reloadFlag = false;
            return;
        }
        List<UUID> musicPlayerIds;
        synchronized (musicEntries) {
            musicPlayerIds = new ArrayList<>(musicEntries.keySet());
        }
        for (UUID musicPlayerId : musicPlayerIds) {
            stop(musicPlayerId);
        }
    }


    /**
     * MusicEngine壊れる～
     * 再読み込みなどされたときに呼び出し
     */
    public void destroy() {
        stopAll();

        musicDestroyRunner.destroy();
        musicDestroyRunner = new MusicEngineDestroyRunner();

        if (musicLoaderExecutor != null) musicLoaderExecutor.shutdown();
        musicLoaderExecutor = createMusicLoadExecutor();

        LavaPlayerManager.getInstance().reload();
    }

    /**
     * ESCなどを押してポーズされたときに停止するときに呼ばれる
     */
    public void pause() {
        synchronized (musicEntries) {
            for (MusicEntry value : musicEntries.values()) {
                value.pause();
            }
        }
    }

    /**
     * ESCなどのポーズが解除されたときに呼ばれる
     */
    public void resume() {
        synchronized (musicEntries) {
            for (MusicEntry value : musicEntries.values()) {
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
        return Executors.newCachedThreadPool(new BasicThreadFactory.Builder().namingPattern("IMP-Music-Loader-%d").daemon(true).build());
    }

    /**
     * 非同期読み込み用Executor
     *
     * @return ExecutorService
     */
    public ExecutorService getMusicAsyncExecutor() {
        return musicLoaderExecutor;
    }


    public MusicEngineDestroyRunner getMusicDestroyRunner() {
        return musicDestroyRunner;
    }

    /**
     * Tick上での読み込み用Executor
     *
     * @return Executor
     */
    public Executor getMusicTickExecutor() {
        return musicTickExecutor;
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

        synchronized (musicEntries) {
            var mpe = new MusicEntry(source, position);
            musicEntries.put(musicPlayerId, mpe);

            mpe.loadStart(listener);
        }

        return true;
    }


    public boolean loadAndPlay(@NotNull UUID musicPlayerId, @NotNull MusicSource source, long position, boolean delayed) {
        return load(musicPlayerId, source, position, (success, time, error) -> {
            if (success) play(musicPlayerId, delayed ? time : 0);
        });
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
        synchronized (musicEntries) {
            mpe = musicEntries.get(musicPlayerId);
        }
        if (mpe == null || !mpe.isLoaded()) return false;

        MusicUtils.runOnMusicTick(() -> {
            mpe.playStart(delay);
        });

        return true;
    }

    /**
     * 指定の音楽プレイヤーの再生を停止
     * 読み込み中であっても中止される
     *
     * @param musicPlayerId 音楽プレイヤーID
     * @return 停止できたかどうか
     */
    public boolean stop(UUID musicPlayerId) {
        MusicEntry mpe;
        synchronized (musicEntries) {
            mpe = musicEntries.remove(musicPlayerId);
        }
        if (mpe == null)
            return false;

        MusicUtils.runOnMusicTick(mpe::destroy);

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
        synchronized (musicEntries) {
            mpe = musicEntries.get(musicPlayerId);
        }

        if (mpe == null) return false;

        return mpe.addSpeaker(speakerId, tracker);
    }

    public boolean removeSpeaker(@NotNull UUID musicPlayerId, @NotNull UUID speakerId) {
        MusicEntry mpe;
        synchronized (musicEntries) {
            mpe = musicEntries.get(musicPlayerId);
        }

        if (mpe == null) return false;

        return mpe.removeSpeaker(speakerId);
    }

    /**
     * 音楽が読み込まれてるかどうか
     *
     * @param musicPlayerId 音楽プレイヤーID
     * @return 読み込まれてるならtrue
     */
    public boolean isLoad(UUID musicPlayerId) {
        synchronized (musicEntries) {
            if (musicEntries.containsKey(musicPlayerId)) return true;
        }
        return false;
    }

    public Logger getLogger() {
        return LOGGER;
    }

    public Map<UUID, MusicEntry> getMusicEntries() {
        synchronized (musicEntries) {
            return ImmutableMap.copyOf(musicEntries);
        }
    }

    @Override
    public Map<UUID, MusicPlayerAccess> getMusicPlayers() {
        ImmutableMap.Builder<UUID, MusicPlayerAccess> musicPlayersBuilder = ImmutableMap.builder();
        synchronized (musicEntries) {
            for (Map.Entry<UUID, MusicEntry> entry : musicEntries.entrySet()) {
                var player = entry.getValue().getMusicPlayer();
                if (player != null) musicPlayersBuilder.put(entry.getKey(), player);
            }
        }
        return musicPlayersBuilder.build();
    }

    public boolean loadAndPlaySimple(@NotNull UUID musicPlayerId, @NotNull MusicTracker musicTracker, @NotNull MusicSource source, long position, boolean delayed) {
        return load(musicPlayerId, source, position, (success, time, error) -> {
            if (success) {
                addSpeaker(musicPlayerId, UUID.randomUUID(), musicTracker);
                play(musicPlayerId, delayed ? time : 0);
            }
        });
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
