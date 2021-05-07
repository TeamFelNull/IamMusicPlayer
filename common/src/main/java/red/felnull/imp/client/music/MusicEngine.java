package red.felnull.imp.client.music;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.music.info.MusicPlayInfo;
import red.felnull.imp.music.info.tracker.MusicTracker;
import red.felnull.imp.music.resource.MusicLocation;

import java.util.*;

public class MusicEngine {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final MusicEngine INSTANCE = new MusicEngine();
    protected final Map<UUID, MusicPlayingEntry> musicPlayers = new HashMap<>();
    protected final Map<UUID, MusicPlayInfo> updateInfos = new HashMap<>();
    private final Map<UUID, MusicPlayStartEntry> playedMusicIds = new HashMap<>();
    protected final Map<UUID, MusicLoaderThread> loaders = new HashMap<>();
    private boolean reload;
    private ResourceLocation lastLocation;

    public static MusicEngine getInstance() {
        return INSTANCE;
    }

    public void reload() {
        reload = true;
    }

    public void readyAndPlay(UUID uuid, MusicLocation musicLocation, long startPosition, MusicPlayInfo info) {
        MusicLoaderThread ml = new MusicLoaderThread(uuid, musicLocation, startPosition, true, info);
        loaders.put(uuid, ml);
        ml.start();
    }


    public void ready(UUID uuid, MusicLocation musicLocation, long startPosition) {
        MusicLoaderThread ml = new MusicLoaderThread(uuid, musicLocation, startPosition, false, null);
        loaders.put(uuid, ml);
        ml.start();
    }

    public void stopReady() {
        loaders.values().forEach(MusicLoaderThread::stopped);
        loaders.clear();
    }

    public void play(UUID uuid, long delay, MusicPlayInfo info) {
        playedMusicIds.put(uuid, new MusicPlayStartEntry(delay, info));
    }

    public void updateInfo(UUID uuid, MusicPlayInfo info) {
        if (updateInfos.containsKey(uuid))
            updateInfos.put(uuid, info);
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
    }

    public void tickNonPaused() {
        if (reload) {
            Map<UUID, MusicPlayingEntry> oldMPlayers = new HashMap<>(musicPlayers);
            Map<UUID, Long> times = new HashMap<>();
            musicPlayers.forEach((n, m) -> times.put(n, m.musicPlayer.getPosition()));
            stopAll();
            Map<UUID, MusicLoaderThread> oldLoaders = new HashMap<>(loaders);
            oldLoaders.forEach((n, m) -> times.put(n, m.getCurrentDelayStartPosition()));
            stopReady();
            oldMPlayers.forEach((n, m) -> readyAndPlay(n, m.musicPlayer.getMusicLocation(), times.get(n), new MusicPlayInfo(m.musicTracker)));
            oldLoaders.forEach((n, m) -> readyAndPlay(n, m.getLocation(), times.get(n), m.getAutPlayInfo()));
            reload = false;
        }
        playedMusicIds.forEach((n, m) -> {
            musicPlayers.get(n).musicTracker = m.info.getTracker();
            MusicPlayingEntry entry = musicPlayers.get(n);
            entry.musicPlayer.play(m.delay);
        });
        playedMusicIds.clear();
        updateInfos.forEach((n, m) -> {
            musicPlayers.get(n).musicTracker = m.getTracker();
        });
        updateInfos.clear();
        musicPlayers.values().forEach(n -> {
            n.musicPlayer.update();
            if (n.musicTracker != null) {
                n.musicPlayer.setSelfPosition(n.musicTracker.getTrackingPosition(mc.level));
                n.musicPlayer.setVolume(n.musicTracker.getTrackingVolume(mc.level));
                n.musicPlayer.linearAttenuation(n.musicTracker.getMaxDistance(mc.level));
            }
        });
    }

    public void pause() {
        musicPlayers.values().forEach(n -> n.musicPlayer.pause());
    }

    public void resume() {
        musicPlayers.values().forEach(n -> n.musicPlayer.unpause());
    }

    public void stopAll() {
        List<UUID> uuids = new ArrayList<>();
        musicPlayers.forEach((n, m) -> uuids.add(n));
        uuids.forEach(this::stop);
    }

    public void stop(UUID musicID) {
        if (musicPlayers.containsKey(musicID)) {
            musicPlayers.get(musicID).musicPlayer.stop();
            musicPlayers.get(musicID).musicPlayer.destroy();
            musicPlayers.remove(musicID);
        }
    }

    public static class MusicPlayStartEntry {
        public final long delay;
        public final MusicPlayInfo info;

        public MusicPlayStartEntry(long delay, MusicPlayInfo info) {
            this.delay = delay;
            this.info = info;
        }
    }

    public static class MusicPlayingEntry {
        public final IMusicPlayer musicPlayer;
        public MusicTracker musicTracker;

        public MusicPlayingEntry(IMusicPlayer musicPlayer, MusicTracker musicTracker) {
            this.musicPlayer = musicPlayer;
            this.musicTracker = musicTracker;
        }
    }
}
