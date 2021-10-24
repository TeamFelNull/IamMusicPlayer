package dev.felnull.imp.client.music;

import dev.felnull.imp.client.music.player.IMusicPlayer;
import dev.felnull.imp.client.music.tracker.IMPMusicTrackers;
import dev.felnull.imp.music.MusicPlaybackInfo;
import dev.felnull.imp.music.resource.MusicSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class MusicEngine {
    private static final Logger LOGGER = LogManager.getLogger(MusicEngine.class);
    private static final MusicEngine INSTANCE = new MusicEngine();
    private final Map<UUID, MusicPlayEntry> MUSIC_PLAYERS = new HashMap<>();
    private final Map<UUID, MusicLoadThread> MUSIC_LOADS = new HashMap<>();
    private final List<UUID> REMOVES_PLAYERS = new ArrayList<>();
    private final List<UUID> REMOVE_LOADS = new ArrayList<>();
    private long lastTime;
    private long lastProsesTime;

    public static MusicEngine getInstance() {
        return INSTANCE;
    }

    public int getCurrentMusicPlayed() {
        return MUSIC_PLAYERS.size() + MUSIC_LOADS.size();
    }

    public int getMaxMusicPlayed() {
        return 8;
    }

    public String getDebugString() {
        return String.format("Musics: %d/%d %d ms", getCurrentMusicPlayed(), getMaxMusicPlayed(), lastProsesTime);
    }

    public boolean playMusicPlayer(UUID id, long delay) {
        synchronized (MUSIC_PLAYERS) {
            if (!MUSIC_PLAYERS.containsKey(id))
                return false;
            var player = MUSIC_PLAYERS.get(id).player();
            if (!player.isPlaying())
                player.play(delay);
        }
        return true;
    }

    public boolean loadAddMusicPlayer(UUID id, MusicPlaybackInfo playbackInfo, MusicSource source, long position, MusicLoadThread.MusicLoadResultListener listener) {
        synchronized (MUSIC_LOADS) {
            if (getCurrentMusicPlayed() >= getMaxMusicPlayed() || MUSIC_LOADS.containsKey(id) || MUSIC_PLAYERS.containsKey(id))
                return false;
            var mt = new MusicLoadThread(source, position, (result, time, player, retry) -> {
                if (result)
                    addMusicPlayer(id, playbackInfo, player);
                listener.onResult(result, time, player, retry);
            });
            MUSIC_LOADS.put(id, mt);
            mt.start();
        }
        return true;
    }

    public boolean stopLoadMusicPlayer(UUID id) {
        synchronized (MUSIC_LOADS) {
            var load = MUSIC_LOADS.remove(id);
            if (load != null) {
                if (load.isAlive())
                    load.interrupt();
                return true;
            }
        }
        return false;
    }

    public boolean addMusicPlayer(UUID id, MusicPlaybackInfo playbackInfo, IMusicPlayer musicPlayer) {
        synchronized (MUSIC_PLAYERS) {
            if (getCurrentMusicPlayed() >= getMaxMusicPlayed() || MUSIC_PLAYERS.containsKey(id))
                return false;
            MUSIC_PLAYERS.put(id, new MusicPlayEntry(playbackInfo, musicPlayer));

            musicPlayer.setVolume(playbackInfo.getVolume());
            musicPlayer.setRange(playbackInfo.getRange());
        }
        return true;
    }

    public boolean updateMusicPlaybackInfo(UUID id, MusicPlaybackInfo playbackInfo) {
        synchronized (MUSIC_PLAYERS) {
            if (!MUSIC_PLAYERS.containsKey(id))
                return false;
            MUSIC_PLAYERS.put(id, new MusicPlayEntry(playbackInfo, MUSIC_PLAYERS.get(id).player()));

            var player = MUSIC_PLAYERS.get(id).player();
            player.setVolume(playbackInfo.getVolume());
            player.setRange(playbackInfo.getRange());
        }
        return true;
    }

    public boolean stopMusicPlayer(UUID id) {
        synchronized (MUSIC_PLAYERS) {
            var rmPly = MUSIC_PLAYERS.remove(id);
            if (rmPly != null) {
                rmPly.player().stop();
                rmPly.player().destroy();
                return true;
            }
            return false;
        }
    }

    public void stopAllMusicPlayer() {
        synchronized (MUSIC_PLAYERS) {
            MUSIC_PLAYERS.forEach((n, m) -> REMOVES_PLAYERS.add(n));
        }
    }

    public void reload() {

    }

    public void tick(boolean paused) {
        lastTime = System.currentTimeMillis();
        synchronized (MUSIC_PLAYERS) {
            REMOVES_PLAYERS.forEach(this::stopMusicPlayer);
            REMOVES_PLAYERS.clear();

            MUSIC_PLAYERS.forEach((n, m) -> {
                if (m.player().isFinished()) {
                    REMOVES_PLAYERS.add(n);
                    return;
                }
                var tracker = IMPMusicTrackers.getTracker(m.playbackInfo().getTracker(), m.playbackInfo().getTrackerTag());
                var ps = tracker.getPosition().get();
                m.player().setCoordinatePosition(ps);
                m.player().setFixedSound(ps == null);
                m.player().update(m.playbackInfo());
            });
        }
        synchronized (MUSIC_LOADS) {
            REMOVE_LOADS.forEach(this::stopLoadMusicPlayer);
            REMOVE_LOADS.clear();
            MUSIC_LOADS.forEach((n, m) -> {
                if (!m.isAlive())
                    REMOVE_LOADS.add(n);
            });
        }
        lastProsesTime = System.currentTimeMillis() - lastTime;
    }

    public void pause() {
        synchronized (MUSIC_PLAYERS) {
            MUSIC_PLAYERS.forEach((n, m) -> m.player().pause());
        }
    }

    public void resume() {
        synchronized (MUSIC_PLAYERS) {
            MUSIC_PLAYERS.forEach((n, m) -> m.player().unpause());
        }
    }

    public static record MusicPlayEntry(MusicPlaybackInfo playbackInfo, IMusicPlayer player) {
    }
}
