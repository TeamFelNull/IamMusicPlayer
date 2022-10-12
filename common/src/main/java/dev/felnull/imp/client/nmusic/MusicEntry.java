package dev.felnull.imp.client.nmusic;

import dev.felnull.imp.client.nmusic.loader.IMPMusicLoaders;
import dev.felnull.imp.client.nmusic.loader.MusicLoader;
import dev.felnull.imp.client.nmusic.player.MusicPlayer;
import dev.felnull.imp.client.nmusic.speaker.ALMusicSpeaker;
import dev.felnull.imp.client.nmusic.speaker.MusicSpeaker;
import dev.felnull.imp.client.nmusic.speaker.buffer.MusicBuffer;
import dev.felnull.imp.client.nmusic.task.MusicLoaderTaskRunner;
import dev.felnull.imp.client.util.MusicUtils;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.nmusic.tracker.MusicTracker;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class MusicEntry {
    private final Map<UUID, MusicTracker> reserveTrackers = new HashMap<>();
    private final AtomicReference<MusicPlayer> musicPlayer = new AtomicReference<>();
    private final MusicSource source;
    private final long startPosition;
    private boolean loaded;
    private boolean stopped;

    protected MusicEntry(MusicSource source, long position) {
        this.source = source;
        this.startPosition = position;
    }

    public MusicSource getSource() {
        return source;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public int getChannels() {
        if (musicPlayer.get() != null)
            return musicPlayer.get().getChannels();
        return 0;
    }

    public long getCurrentPosition() {
        if (musicPlayer.get() != null)
            return musicPlayer.get().getPosition();
        return 0;
    }

    public float getCurrentAudioWave(int channel) {
        if (musicPlayer.get() != null)
            return musicPlayer.get().getCurrentAudioWave(channel);
        return 0;
    }

    protected void stop() {
        stopped = true;
        if (musicPlayer.get() != null) {
            try {
                musicPlayer.get().destroy();
            } catch (Exception e) {
                MusicEngine.getInstance().getLogger().error("Failed to destroy music player", e);
            }
        }
    }

    protected void playStart(long delay) {
        musicPlayer.get().play(delay);
    }

    protected boolean tick() {
        if (musicPlayer.get() != null) {
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

    protected void destroy() throws Exception {
        if (musicPlayer.get() != null) {
            musicPlayer.get().destroy();
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    protected int getSpeakerCount() {
        if (musicPlayer.get() != null)
            return musicPlayer.get().getSpeakerCount();
        return 0;
    }

    protected boolean addSpeaker(UUID speakerId, MusicTracker tracker) {
        if (musicPlayer.get() == null) {
            synchronized (reserveTrackers) {
                if (reserveTrackers.containsKey(speakerId))
                    return false;

                reserveTrackers.put(speakerId, tracker);
            }
            return true;
        }

        var spk = musicPlayer.get().getSpeaker(speakerId);
        if (spk != null)
            return false;

        AtomicReference<MusicSpeaker<? extends MusicBuffer<?>>> spker = new AtomicReference<>();
        MusicUtils.runOnMusicTick(() -> spker.set(new ALMusicSpeaker(tracker)));

        musicPlayer.get().addSpeaker(speakerId, spker.get());
        return true;
    }

    protected void loadStart(MusicEngine.LoadCompleteListener listener) {
        var me = MusicEngine.getInstance();
        var runner = new MusicLoaderTaskRunner(me.getMusicTaskRunner(), () -> stopped);

        var cf = CompletableFuture.supplyAsync(() -> {
            var loader = selectLoader(source);
            if (loader == null)
                throw new RuntimeException("No available loaders found");
            runner.addTaskThrow(null, loader::cansel, null);
            return loader;
        }, me.getMusicLoaderExecutor()).thenApplyAsync(ret -> {
            MusicPlayer player = ret.createMusicPlayer();
            runner.addTaskThrow(() -> musicPlayer.set(player), null, () -> {
                try {
                    player.destroy();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            return player;
        }, me.getMusicLoaderExecutor()).thenApplyAsync(ret -> {
            runner.addTaskThrow(() -> {
                synchronized (reserveTrackers) {
                    reserveTrackers.forEach(this::addSpeaker);
                    reserveTrackers.clear();
                }
            }, null, null);
            return ret;
        }, me.getMusicLoaderExecutor()).thenApplyAsync(ret -> {
            long time = System.currentTimeMillis();
            try {
                ret.load(runner, startPosition);
                runner.addTaskThrow(null, null, () -> {
                    try {
                        ret.destroy();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                return new LoadResult(true, System.currentTimeMillis() - time, null);
            } catch (Exception e) {
                return new LoadResult(false, System.currentTimeMillis() - time, e);
            }
        }, me.getMusicLoaderExecutor());

        cf.whenCompleteAsync((ret, throwable) -> {
            runner.addTaskThrow(() -> {
                loaded = ret != null && ret.success;

                if (throwable != null) {
                    listener.onComplete(false, 0, throwable);
                    return;
                }

                if (ret != null)
                    listener.onComplete(ret.success, ret.time, ret.error);
            }, null, null);
        }, me.getMusicLoaderExecutor());
    }

    private MusicLoader selectLoader(MusicSource source) {
        List<MusicLoader> loaded = new ArrayList<>();
        var ldr = IMPMusicLoaders.getAllLoader().stream().map(Supplier::get).filter(n -> {
            try {
                n.tryLoad(source);
                loaded.add(n);
                return true;
            } catch (Exception ex) {
                return false;
            }
        }).min(Comparator.comparingInt(MusicLoader::priority));

        var rldr = ldr.orElse(null);

        for (MusicLoader loader : loaded) {
            if (loader != null && loader != rldr)
                loader.cansel();
        }

        return rldr;
    }

    private static record LoadResult(boolean success, long time, Throwable error) {
    }
}
