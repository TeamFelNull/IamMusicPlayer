package red.felnull.imp.client.music;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.music.loader.MusicLoaderThread;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.client.music.subtitle.SubtitleManager;
import red.felnull.imp.client.music.subtitle.SubtitleSystem;
import red.felnull.imp.client.util.SoundMath;
import red.felnull.imp.music.info.MusicPlayInfo;
import red.felnull.imp.music.info.tracker.MusicTracker;
import red.felnull.imp.music.resource.MusicSource;

import java.util.*;

public class MusicEngine {
    private static final Logger LOGGER = LogManager.getLogger(MusicEngine.class);
    private static final Minecraft mc = Minecraft.getInstance();
    private static final MusicEngine INSTANCE = new MusicEngine();
    public final Map<UUID, MusicPlayingEntry> musicPlayers = new HashMap<>();
    public final Map<UUID, MusicLoaderThread> loaders = new HashMap<>();
    private boolean reload;
    private ResourceLocation lastLocation;

    private final List<MusicRunnerEntry> runnerEntries = new ArrayList<>();

    public static MusicEngine getInstance() {
        return INSTANCE;
    }

    public void reload() {
        reload = true;
    }

    public void readyAndPlay(UUID uuid, MusicSource musicSource, long startPosition, MusicPlayInfo info, boolean delay) {
        runnerEntries.add(new MusicRunnerEntry(Priority.HIGH, true, () -> {
            MusicLoaderThread ml = new MusicLoaderThread(uuid, musicSource, startPosition, info, (x, y, z) -> {
                if (x == MusicLoaderThread.MusicLoadResult.COMPLETE) {
                    play(y, delay ? z : 0, info);
                } else {
                    LOGGER.error("Failure Play: " + musicSource.getLoaderName() + ":" + musicSource.getIdentifier());
                }
            });
            loaders.put(uuid, ml);
            ml.start();
        }));
    }

    public MusicPlayingEntry getPlyingMusic(UUID uuid) {
        return musicPlayers.get(uuid);
    }

    public void ready(UUID uuid, MusicSource musicSource, long startPosition, MusicLoaderThread.MusicLoadResultListener resultListener) {
        runnerEntries.add(new MusicRunnerEntry(Priority.LOW, true, () -> {
            MusicLoaderThread ml = new MusicLoaderThread(uuid, musicSource, startPosition, null, resultListener);
            loaders.put(uuid, ml);
            ml.start();
        }));
    }

    public void stopReady() {
        runnerEntries.add(new MusicRunnerEntry(Priority.HIGH, true, () -> {
            loaders.values().forEach(MusicLoaderThread::stopped);
            loaders.clear();
        }));
    }

    public void play(UUID uuid, long delay, MusicPlayInfo info) {
        runnerEntries.add(new MusicRunnerEntry(Priority.MIDDLE, true, () -> {
            if (musicPlayers.containsKey(uuid)) {
                musicPlayers.get(uuid).musicTracker = info.getTracker();
                MusicPlayingEntry entry = musicPlayers.get(uuid);
                entry.musicPlayer.play(delay);
            }
        }));
    }

    public void updateInfo(UUID uuid, MusicPlayInfo info) {
        runnerEntries.add(new MusicRunnerEntry(Priority.LOW, true, () -> {
            if (musicPlayers.containsKey(uuid))
                musicPlayers.get(uuid).musicTracker = info.getTracker();
        }));
    }

    public void pause() {
        runnerEntries.add(new MusicRunnerEntry(Priority.HIGH, false, () -> musicPlayers.values().forEach(n -> n.musicPlayer.pause())));
    }

    public void resume() {
        runnerEntries.add(new MusicRunnerEntry(Priority.HIGH, false, () -> musicPlayers.values().forEach(n -> n.musicPlayer.unpause())));
    }

    public void stopAll() {
        runnerEntries.add(new MusicRunnerEntry(Priority.LOW, false, () -> {
            musicPlayers.forEach((n, m) -> {
                m.musicPlayer.stop();
                m.musicPlayer.destroy();
            });
            musicPlayers.clear();
        }));
    }

    public void stop(UUID musicID) {
        runnerEntries.add(new MusicRunnerEntry(Priority.LOW, false, () -> {
            if (musicPlayers.containsKey(musicID)) {
                musicPlayers.get(musicID).musicPlayer.stop();
                musicPlayers.get(musicID).musicPlayer.destroy();
                musicPlayers.remove(musicID);
            }
        }));
    }

    public void tick(boolean paused) {
        if (mc.level == null) {
            stopAll();
            stopReady();
            lastLocation = null;
        } else {
            if (!mc.level.dimension().location().equals(lastLocation)) {
                lastLocation = mc.level.dimension().location();
                stopAll();
                stopReady();
            }
        }
        if (reload) {
            Map<UUID, MusicPlayingEntry> oldMPlayers = new HashMap<>(musicPlayers);
            Map<UUID, Long> times = new HashMap<>();
            musicPlayers.forEach((n, m) -> times.put(n, m.musicPlayer.getPosition()));
            stopAll();
            Map<UUID, MusicLoaderThread> oldLoaders = new HashMap<>(loaders);
            oldLoaders.forEach((n, m) -> times.put(n, m.getCurrentDelayStartPosition()));
            stopReady();
            oldMPlayers.forEach((n, m) -> readyAndPlay(n, m.musicPlayer.getMusicSource(), times.get(n), new MusicPlayInfo(m.musicTracker), true));
            oldLoaders.forEach((n, m) -> readyAndPlay(n, m.getLocation(), times.get(n), m.getAutPlayInfo(), true));
            reload = false;
        }

        List<MusicRunnerEntry> removeRunners = new ArrayList<>();
        runnerEntries.stream().filter(n -> !paused || !n.nonPaused).sorted(Comparator.comparing(n -> 10 - n.priority.num)).forEach(n -> {
            removeRunners.add(n);
            try {
                n.runnable.run();
            } catch (Exception ex) {
                LOGGER.error("Music Engine Task Error", ex);
            }
        });
        removeRunners.forEach(runnerEntries::remove);
        removeRunners.clear();

        musicPlayers.forEach((m, n) -> {
            n.musicPlayer.update();
            if (n.musicTracker != null) {
                n.musicPlayer.setSelfPosition(n.musicTracker.getTrackingPosition(mc.level));
                n.musicPlayer.linearAttenuation(n.musicTracker.getMaxDistance(mc.level));
                n.musicPlayer.setVolume(SoundMath.calculateVolume(n.musicTracker.getTrackingVolume(mc.level)));
            }
            if (n.musicPlayer.getPosition() > n.musicPlayer.getMusicSource().getDuration())
                stop(m);
        });

        if (IamMusicPlayer.CONFIG.subtitleSystem != SubtitleSystem.OFF)
            SubtitleManager.getInstance().tick(paused);

    }

    public boolean isPlaying(UUID uuid) {
        return musicPlayers.containsKey(uuid);
    }

    public boolean isExist(UUID uuid) {
        return musicPlayers.containsKey(uuid) || loaders.containsKey(uuid);
    }

    public static class MusicPlayingEntry {
        public final IMusicPlayer musicPlayer;
        public MusicTracker musicTracker;

        public MusicPlayingEntry(IMusicPlayer musicPlayer, MusicTracker musicTracker) {
            this.musicPlayer = musicPlayer;
            this.musicTracker = musicTracker;
        }
    }

    public static record MusicRunnerEntry(Priority priority, boolean nonPaused, Runnable runnable) {
    }

    public enum Priority {
        HIGH(0),
        MIDDLE(1),
        LOW(2);
        private final int num;

        Priority(int num) {
            this.num = num;
        }
    }
}
