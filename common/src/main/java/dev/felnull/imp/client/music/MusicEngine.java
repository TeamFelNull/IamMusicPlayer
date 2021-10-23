package dev.felnull.imp.client.music;

import dev.felnull.imp.client.music.player.IMusicPlayer;
import dev.felnull.imp.client.music.tracker.IMPMusicTrackers;
import dev.felnull.imp.music.MusicPlaybackInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class MusicEngine {
    private static final Logger LOGGER = LogManager.getLogger(MusicEngine.class);
    private static final MusicEngine INSTANCE = new MusicEngine();
    private final Map<UUID, MusicPlayEntry> MUSIC_PLAYERS = new HashMap<>();
    private final List<UUID> REMOVES = new ArrayList<>();
    private long lastTime;
    private long lastProsesTime;

    public static MusicEngine getInstance() {
        return INSTANCE;
    }

    public int getCurrentMusicPlayed() {
        return MUSIC_PLAYERS.size();
    }

    public int getMaxMusicPlayed() {
        return 8;
    }

    public String getDebugString() {
        return String.format("Musics: %d/%d %d ms", getCurrentMusicPlayed(), getMaxMusicPlayed(), lastProsesTime);
    }

    public boolean addMusicPlayer(UUID id, MusicPlaybackInfo playbackInfo, IMusicPlayer musicPlayer) {
        synchronized (MUSIC_PLAYERS) {
            if (MUSIC_PLAYERS.size() >= getMaxMusicPlayed() || MUSIC_PLAYERS.containsKey(id))
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

    public boolean removeMusicPlayer(UUID id) {
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

    public void removeAllMusicPlayer() {
        synchronized (MUSIC_PLAYERS) {
            MUSIC_PLAYERS.forEach((n, m) -> REMOVES.add(n));
        }
    }

    public void reload() {

    }

    public void tick(boolean paused) {
        lastTime = System.currentTimeMillis();
        synchronized (MUSIC_PLAYERS) {
            REMOVES.forEach(this::removeMusicPlayer);
            REMOVES.clear();

            MUSIC_PLAYERS.forEach((n, m) -> {
                if (m.player().isFinished()) {
                    REMOVES.add(n);
                    return;
                }
                var tracker = IMPMusicTrackers.getTracker(m.playbackInfo().getTracker(), m.playbackInfo().getTrackerTag());
                m.player().setCoordinatePosition(tracker.getPosition().get());
                m.player().update(m.playbackInfo());
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
