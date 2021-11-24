package dev.felnull.imp.client.music;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.api.event.client.ClientMusicEvent;
import dev.felnull.imp.client.music.player.IMusicPlayer;
import dev.felnull.imp.client.music.subtitle.SubtitleEntry;
import dev.felnull.imp.client.music.tracker.IMPMusicTrackers;
import dev.felnull.imp.client.util.SoundMath;
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
    private final Map<UUID, Long> LAST_SUBTITLE = new HashMap<>();
    private final List<UnPauseStartEntry> UNPAUSES_STARTS = new ArrayList<>();
    private final Map<UUID, MusicReloadEntry> RESTART_LIVES = new HashMap<>();
    private final List<UUID> RESTART_CHECK = new ArrayList<>();
    private long lastTime;
    private long lastProsesTime;
    private boolean pause;
    private boolean reloading;
    public boolean reloadFlag;

    public static MusicEngine getInstance() {
        return INSTANCE;
    }

    public int getCurrentMusicPlayed() {
        return MUSIC_PLAYERS.size() + MUSIC_LOADS.size();
    }

    public int getMaxMusicPlayed() {
        return Math.max(IamMusicPlayer.CONFIG.maxPlayCont, 0);
    }

    public boolean isReloading() {
        return reloading;
    }

    public String getDebugString() {
        return String.format("Musics: %d/%d %d ms", getCurrentMusicPlayed(), getMaxMusicPlayed(), lastProsesTime);
    }

    public boolean playMusicPlayer(UUID id, long delay) {
        synchronized (UNPAUSES_STARTS) {
            if (pause) {
                UNPAUSES_STARTS.add(new UnPauseStartEntry(id, delay));
                return true;
            }
        }
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
            var mt = new MusicLoadThread(source, playbackInfo, position, (result, time, player, retry) -> {
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
            if ((getCurrentMusicPlayed() >= getMaxMusicPlayed() && !MUSIC_LOADS.containsKey(id)) || MUSIC_PLAYERS.containsKey(id))
                return false;
            MUSIC_PLAYERS.put(id, new MusicPlayEntry(playbackInfo, musicPlayer));

            musicPlayer.setVolume(SoundMath.calculateVolume(playbackInfo.getVolume()));
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
            player.setVolume(SoundMath.calculateVolume(playbackInfo.getVolume()));
            player.setRange(playbackInfo.getRange());
        }
        return true;
    }

    public boolean stopMusicPlayer(UUID id) {
        synchronized (RESTART_LIVES) {
            if (RESTART_LIVES.containsKey(id)) {
                if (RESTART_CHECK.contains(id)) {
                    RESTART_LIVES.remove(id);
                    RESTART_CHECK.remove(id);
                    return true;
                } else {
                    RESTART_CHECK.add(id);
                }
            }
        }
        synchronized (UNPAUSES_STARTS) {
            UnPauseStartEntry uss = null;
            for (UnPauseStartEntry us : UNPAUSES_STARTS) {
                if (us.id().equals(id)) {
                    uss = us;
                    break;
                }
            }
            if (uss != null) {
                UNPAUSES_STARTS.remove(uss);
                return true;
            }
        }
        synchronized (MUSIC_PLAYERS) {
            var rmPly = MUSIC_PLAYERS.remove(id);
            LAST_SUBTITLE.remove(id);
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

    public void stopAllMusicLoad() {
        synchronized (MUSIC_LOADS) {
            MUSIC_LOADS.forEach((n, m) -> REMOVE_LOADS.add(n));
        }
    }

    public boolean isPlaying(UUID uuid) {
        return (MUSIC_PLAYERS.containsKey(uuid) && MUSIC_PLAYERS.get(uuid).player().isPlaying()) || (pause && UNPAUSES_STARTS.stream().anyMatch(m -> m.id().equals(uuid))) || (pause && MUSIC_PLAYERS.get(uuid).player().isPaused());
    }

    public void stop() {
        if (!reloadFlag) {
            stopAllMusicLoad();
            stopAllMusicPlayer();
        }
        reloadFlag = false;
    }

    public void reload() {
        LOGGER.info("Music engine started");
        reloading = true;
        Map<UUID, MusicReloadEntry> RELOADS = new HashMap<>();
        synchronized (MUSIC_LOADS) {
            MUSIC_LOADS.forEach((n, m) -> RELOADS.put(n, new MusicReloadEntry(m.getPlaybackInfo(), m.getSource(), m.getPosition(), m.getListener())));
        }
        RELOADS.forEach((n, m) -> stopLoadMusicPlayer(n));

        List<UUID> REMOVEMUSICS = new ArrayList<>();
        synchronized (MUSIC_PLAYERS) {
            MUSIC_PLAYERS.forEach((n, m) -> {
                RELOADS.put(n, new MusicReloadEntry(m.playbackInfo(), m.player().getMusicSource(), m.player().getPosition(), null));
                REMOVEMUSICS.add(n);
            });
        }
        Map<UUID, Long> LASTTIMES = new HashMap<>();
        REMOVEMUSICS.forEach(n -> {
            if (isPlaying(n))
                LASTTIMES.put(n, System.currentTimeMillis());
            stopMusicPlayer(n);
        });

        RELOADS.forEach((n, m) -> loadAddMusicPlayer(n, m.playbackInfo(), m.source(), m.position(), (result, time, player, retry) -> {
            if (m.listener() != null)
                m.listener().onResult(result, time, player, retry);

            if (result && LASTTIMES.containsKey(n)) {
                long delay = System.currentTimeMillis() - LASTTIMES.get(n);
                playMusicPlayer(n, delay);
            }
        }));
        reloading = false;
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
                var tracker = IMPMusicTrackers.createTracker(m.playbackInfo().getTracker(), m.playbackInfo().getTrackerTag());
                if (tracker != null) {
                    var ps = tracker.getPosition().get();
                    m.player().setCoordinatePosition(ps);
                    m.player().setFixedSound(ps == null);
                }
                m.player().update(m.playbackInfo());
                var sub = m.player().getSubtitle();
                if (m.player().isPlaying() && sub != null) {
                    long pos = m.player().getPosition();
                    long lst = LAST_SUBTITLE.containsKey(n) ? LAST_SUBTITLE.get(n) : 0;
                    var subs = sub.getSubtitle(m.player(), m.playbackInfo(), lst, pos);
                    subs.forEach(this::addSubtitle);
                    LAST_SUBTITLE.put(n, pos);
                }
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
        this.pause = true;
        synchronized (MUSIC_PLAYERS) {
            MUSIC_PLAYERS.forEach((n, m) -> {
                if (!m.player().getMusicSource().isLive()) {
                    m.player().pause();
                } else {
                    RESTART_LIVES.put(n, new MusicReloadEntry(m.playbackInfo(), m.player().getMusicSource(), 0, null));
                    REMOVES_PLAYERS.add(n);
                }
            });
        }
    }

    public void resume() {
        this.pause = false;
        synchronized (MUSIC_PLAYERS) {
            MUSIC_PLAYERS.forEach((n, m) -> m.player().unpause());
        }
        UNPAUSES_STARTS.forEach(n -> playMusicPlayer(n.id(), n.delay()));
        UNPAUSES_STARTS.clear();
        RESTART_LIVES.forEach((n, m) -> loadAddMusicPlayer(n, m.playbackInfo(), m.source(), 0, (result, time, player, retry) -> {
            if (result)
                playMusicPlayer(n, 0);
        }));
        RESTART_LIVES.clear();
        RESTART_CHECK.clear();
    }

    public void addSubtitle(SubtitleEntry subtitle) {
        ClientMusicEvent.ADD_SUBTITLE.invoker().add(subtitle);
    }

    private static record MusicPlayEntry(MusicPlaybackInfo playbackInfo, IMusicPlayer player) {
    }

    private static record MusicReloadEntry(MusicPlaybackInfo playbackInfo, MusicSource source, long position,
                                           MusicLoadThread.MusicLoadResultListener listener) {
    }

    private static record UnPauseStartEntry(UUID id, long delay) {
    }

}
