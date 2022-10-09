package dev.felnull.imp.client.nmusic;

import dev.felnull.imp.client.nmusic.loader.IMPMusicLoaders;
import dev.felnull.imp.client.nmusic.loader.MusicLoader;
import dev.felnull.imp.client.nmusic.player.MusicPlayer;
import dev.felnull.imp.client.nmusic.speaker.ALMusicSpeaker;
import dev.felnull.imp.client.nmusic.speaker.MusicSpeaker;
import dev.felnull.imp.client.nmusic.speaker.buffer.MusicBuffer;
import dev.felnull.imp.client.util.ALUtils;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.nmusic.tracker.MusicTracker;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class MusicEntry {
    private final Map<UUID, MusicTracker> reserveTrackers = new HashMap<>();
    private final AtomicReference<MusicPlayer> musicPlayer = new AtomicReference<>();
    private boolean loaded;

    protected void playStart(long delay) {
        musicPlayer.get().play(delay);
    }

    protected void tick() {
        if (musicPlayer.get() != null) {
            musicPlayer.get().tick();
        }
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

    protected boolean isLoaded() {
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
        ALUtils.runOnSoundThread(() -> spker.set(new ALMusicSpeaker(tracker)));

        musicPlayer.get().addSpeaker(speakerId, spker.get());
        return true;
    }

    protected void loadStart(MusicSource source, long position, MusicEngine.LoadCompleteListener listener) {
        var me = MusicEngine.getInstance();

        var cf = CompletableFuture.supplyAsync(() -> {
            return selectLoader(source);
        }, me.getMusicLoaderExecutor()).thenApplyAsync(ret -> {
            return ret.createMusicPlayer();
        }, me.getMusicLoaderExecutor()).thenApplyAsync(ret -> {
            musicPlayer.set(ret);
            synchronized (reserveTrackers) {
                reserveTrackers.forEach(this::addSpeaker);
                reserveTrackers.clear();
            }
            return ret;
        }, me.getMusicLoaderExecutor()).thenApplyAsync(ret -> {
            long time = System.currentTimeMillis();
            try {
                ret.load(position);
                return new LoadResult(true, System.currentTimeMillis() - time, null);
            } catch (Exception e) {
                return new LoadResult(false, System.currentTimeMillis() - time, e);
            }
        }, me.getMusicLoaderExecutor());

        cf.whenCompleteAsync((ret, throwable) -> {
            loaded = ret != null && ret.success;

            if (throwable != null) {
                listener.onComplete(false, 0, throwable);
                return;
            }

            if (ret != null)
                listener.onComplete(ret.success, ret.time, ret.error);
        }, me.getMusicLoaderExecutor());
    }

    private MusicLoader selectLoader(MusicSource source) {
        var ldr = IMPMusicLoaders.getAllLoader().stream().map(Supplier::get).filter(n -> {
            try {
                n.tryLoad(source);
                return true;
            } catch (Exception ex) {
                return false;
            }
        }).min(Comparator.comparingInt(MusicLoader::priority));
        return ldr.orElse(null);
    }

    private static record LoadResult(boolean success, long time, Throwable error) {
    }
}
