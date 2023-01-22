package dev.felnull.imp.client.music;

import dev.felnull.imp.client.music.loader.IMPMusicLoaders;
import dev.felnull.imp.client.music.loader.MusicLoader;
import dev.felnull.imp.client.music.player.MusicLoadChunk;
import dev.felnull.imp.client.music.player.MusicPlayer;
import dev.felnull.imp.client.music.speaker.MusicSpeaker;
import dev.felnull.imp.client.music.task.MusicDestroyRunner;
import dev.felnull.imp.client.music.task.MusicLoaderDestroyRunner;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.music.tracker.MusicTracker;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class MusicEntry {
    private final Map<UUID, MusicTracker> reserveTrackers = new HashMap<>();
    private final AtomicReference<MusicPlayer<?, ?>> musicPlayer = new AtomicReference<>();
    private final MusicSource source;
    private final long startPosition;
    private final UUID musicPlayerId;
    private boolean loaded;
    private boolean stopped;
    private boolean loadFailed;

    public float getCurrentPositionProgress() {
        if (source.isLive())
            return 0;
        return (float) getCurrentPosition() / (float) source.getDuration();
    }

    protected MusicPlayer<?, ?> getMusicPlayer() {
        return musicPlayer.get();
    }

    protected int getTaskCount() {
        if (musicPlayer.get() != null)
            return musicPlayer.get().getTaskCount();
        return 0;
    }

    protected MusicEntry(UUID musicPlayerId, MusicSource source, long position) {
        this.musicPlayerId = musicPlayerId;
        this.source = source;
        this.startPosition = position;
    }

    public MusicSource getSource() {
        return source;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public List<MusicLoadChunk> getLoadChunks() {
        if (musicPlayer.get() != null)
            return musicPlayer.get().getLoadChunks();

        return new ArrayList<>();
    }

    public int getChannels() {
        if (musicPlayer.get() != null)
            return musicPlayer.get().getAudioInfo().channel();
        return 0;
    }

    public long getCurrentPosition() {
        if (musicPlayer.get() != null) {
            long pos = musicPlayer.get().getPosition();
            if (pos >= 0) return pos;
        }
        return startPosition;
    }

    public float getCurrentAudioWave(int channel) {
        if (musicPlayer.get() != null)
            return musicPlayer.get().getCurrentAudioWave(channel);
        return 0;
    }

    protected void playStart(long delay) {
        musicPlayer.get().play(delay);
    }

    protected boolean tick() {
        if (loadFailed) {
            destroy();
            return false;
        }

        if (musicPlayer.get() != null) {
            if (musicPlayer.get().waitDestroy()) {
                destroy();
                return false;
            }

            if (musicPlayer.get().isDestroy())
                return false;

            musicPlayer.get().tick();
        }

        return true;
    }

    protected void pause() {
        if (musicPlayer.get() != null) {
            musicPlayer.get().pause();
        }
    }

    protected void resume() {
        if (musicPlayer.get() != null) {
            musicPlayer.get().resume();
        }
    }

    protected void destroy() {
        stopped = true;
        if (musicPlayer.get() != null)
            musicPlayer.get().destroyNonThrow();
    }

    public boolean isLoaded() {
        return loaded;
    }

    public boolean isPlaying() {
        if (musicPlayer.get() != null)
            return musicPlayer.get().isPlaying();
        return false;
    }

    protected int getSpeakerCount() {
        if (musicPlayer.get() != null) return musicPlayer.get().getSpeakerCount();
        return 0;
    }

    protected boolean updateMusicTracker(UUID speakerId, MusicTracker tracker) {
        var player = musicPlayer.get();
        if (player == null) {
            synchronized (reserveTrackers) {
                return reserveTrackers.put(speakerId, tracker) != null;
            }
        }
        var sp = player.getSpeaker(speakerId);
        if (sp != null) {
            sp.setTracker(tracker);
            return true;
        }
        return false;
    }

    protected boolean removeSpeaker(UUID speakerId) {
        var player = musicPlayer.get();

        if (player == null) {
            synchronized (reserveTrackers) {
                return reserveTrackers.remove(speakerId) != null;
            }
        }
        if (!player.existSpeaker(speakerId)) return false;

        player.removeSpeaker(speakerId);
        return true;
    }

    protected boolean addSpeaker(UUID speakerId, MusicTracker tracker) {
        if (musicPlayer.get() == null) {
            synchronized (reserveTrackers) {
                if (reserveTrackers.containsKey(speakerId)) return false;

                reserveTrackers.put(speakerId, tracker);
            }
            return true;
        }

        var spk = musicPlayer.get().getSpeaker(speakerId);
        if (spk != null) return false;

        musicPlayer.get().addSpeaker(speakerId, new MusicSpeaker(musicPlayerId, speakerId, tracker));
        return true;
    }

    private MusicDestroyRunner createDestroyRunner() {
        var me = MusicEngine.getInstance();
        return new MusicLoaderDestroyRunner(me.getMusicDestroyRunner(), () -> stopped);
    }

    protected void loadStart(LoadCompleteListener listener) {
        final long startTime = System.currentTimeMillis();
        var me = MusicEngine.getInstance();
        var runner = createDestroyRunner();

        var cf = CompletableFuture.supplyAsync(() -> {
            var loader = selectLoader(source);
            if (loader == null)
                throw new RuntimeException("No available loaders found");
            return loader;
        }, me.getMusicAsyncExecutor()).thenApplyAsync(ret -> {
            return ret.createMusicPlayer(musicPlayerId);
        }, me.getMusicTickExecutor()).thenApplyAsync(ret -> {
            musicPlayer.set(ret);

            runner.run(ret::destroyNonThrow);
            return ret;
        }, me.getMusicAsyncExecutor()).thenApplyAsync(ret -> {
            synchronized (reserveTrackers) {
                reserveTrackers.forEach(this::addSpeaker);
                reserveTrackers.clear();
            }
            runner.run(ret::destroyNonThrow);
            return ret;
        }, me.getMusicTickExecutor()).thenApplyAsync(ret -> {
            return new MusicLoadStartResult(ret, ret.loadStart(startPosition));
        }, me.getMusicTickExecutor()).thenApplyAsync(ret -> {
            try {
                var aret = ret.loadAsync();
                runner.run(() -> ret.musicPlayer().destroyNonThrow());
                return new MusicLoadAsyncResult(new LoadResult(true, null), aret, ret.musicPlayer());
            } catch (Exception e) {
                return new MusicLoadAsyncResult(new LoadResult(false, e), null, null);
            }
        }, me.getMusicAsyncExecutor()).thenApplyAsync(ret -> {
            runner.run(() -> {
                if (ret.musicPlayer() != null) ret.musicPlayer().destroyNonThrow();
            });

            if (ret.musicLoadEndResult() != null) {
                ret.musicLoadEndResult().apply();
                runner.run(() -> ret.musicLoadEndResult().musicPlayer().destroyNonThrow());
            }
            return ret.loadResult();
        }, me.getMusicTickExecutor());

        cf.whenCompleteAsync((ret, throwable) -> {
            loaded = ret != null && ret.success;

            if (throwable != null) {
                loadFailed = true;

                if (!(throwable instanceof RuntimeException))
                    MusicEngine.getInstance().getLogger().error("Music load error", throwable);

                listener.onComplete(false, 0, throwable, musicPlayer.get() != null);
                return;
            }

            if (ret != null)
                listener.onComplete(ret.success, System.currentTimeMillis() - startTime, ret.error, musicPlayer.get() != null);
        }, me.getMusicTickExecutor());
    }

    private MusicLoader selectLoader(MusicSource source) {
        List<MusicLoader> loaders = IMPMusicLoaders.getAllLoader().stream().map(Supplier::get).sorted(Comparator.comparingInt(MusicLoader::priority).reversed()).toList();
        for (MusicLoader loader : loaders) {
            try {
                loader.tryLoad(source);
                return loader;
            } catch (Exception ignored) {
            }
        }
        return null;
    }


    private static record LoadResult(boolean success, Throwable error) {
    }

    private static record MusicLoadStartResult<T, E>(MusicPlayer<T, E> musicPlayer, T input) {
        private MusicLoadEndResult<E> loadAsync() throws Exception {
            return new MusicLoadEndResult<>(musicPlayer, musicPlayer.loadAsync(input));
        }
    }

    private static record MusicLoadEndResult<T>(MusicPlayer<?, T> musicPlayer, T result) {
        private void apply() {
            musicPlayer.loadApply(result);
        }
    }

    private static record MusicLoadAsyncResult(LoadResult loadResult, MusicLoadEndResult<?> musicLoadEndResult,
                                               MusicPlayer<?, ?> musicPlayer) {
    }

    public static record MusicLoadRange(long first, long last) {
    }
}